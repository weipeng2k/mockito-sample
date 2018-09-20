# 使用Spring-Test来进行单元测试

> 以下例子可以在`classic-spring-test`中找到。

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;spring-test是springframework中一个模块，主要也是由spring作者`Juergen Hoeller`来完成的，它可以方便的测试基于spring的代码。

## 引入spring-test

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`spring-test`只需要引入依赖就可以完成测试，非常简单。它能够帮助我们启动一个测试的spring容器，完成属性的装配，但是它如何同`Mockito`集成起来是一个问题，我们采用配置的方式进行。

### 加入依赖

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;增加依赖：

> 该版本一般和你使用的spring版本一致

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-test</artifactId>
    <scope>test</scope>
</dependency>
```

### 配置

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;由于`Mockito`支持`mock`方法构造，所以我们可以将它通过spring factory bean的形式融入到 spring 的体系中。我们针对`MemberService`进行测试，需要对`UserDAO`进行Mock，我们只需要在配置中配置即可。

> 配置在MemberService.xml中，这里需要说明一下 **没有使用共用的配置文件**， 目的就是让大家在测试的时候能够相互独立，而且在一个配置文件中配置的Bean越多，就证明你要测试的类依赖越复杂，也就是越不合理，**逼迫自己做重构**。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd"
       default-autowire="byName">

    <bean id="memberService" class="com.murdock.tools.mockito.service.MemberServiceImpl"/>

    <bean id="userDAO" class="org.mockito.Mockito" factory-method="mock">
        <constructor-arg>
            <value>com.murdock.tools.mockito.dao.UserDAO</value>
        </constructor-arg>
    </bean>
</beans>
```

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;在进行spring测试之前，我们必须有一个spring的配置文件，用来构造`applicationContext`，注意上面红色的部分，这个`UserDAO`就是`MemberServiceImpl`需要的，而它利用了spring的`FactoryBean`方式，通过mock工厂方法完成了Mock对象的构造，其中的构造函数表明了这个Mock是什么类型的。只用在配置文件中声明一下就可以了。

## 构造Mock

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;先看一下使用spring-test如何写单元测试：

```java
@ContextConfiguration(locations = {"classpath:MemberService.xml"})
public class MemberSpringTest extends AbstractJUnit4SpringContextTests {
    @Autowired
    private MemberService memberService;
    @Autowired
    private UserDAO userDAO;

    /**
     * 可以选择在测试开始的时候来进行mock的逻辑编写
     */
    @Before
    public void mockUserDAO() {
        Mockito.when(userDAO.insertMember(Mockito.any())).thenReturn(
                System.currentTimeMillis());

        ((MemberServiceImpl) memberService).setUserDAO(userDAO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void insertMemberError() {
        memberService.insertMember(null, "123");

        memberService.insertMember(null, null);
    }

    /**
     * 也可以选择在方法中进行mock
     */
    @Test(expected = IllegalArgumentException.class)
    public void insertExistMember() {
        Member member = new Member();
        member.setName("weipeng");
        member.setPassword("123456abcd");
        Mockito.when(userDAO.findMember("weipeng")).thenReturn(member);

        memberService.insertMember("weipeng", "1234abc");
    }

    @Test(expected = IllegalArgumentException.class)
    public void insertIllegalArgument() {
        StringBuilder sb = new StringBuilder();
        IntStream.range(0, 32).forEach(sb::append);
        
        memberService.insertMember(sb.toString(), "abcdcsfa123");
    }

    @Test
    public void insertMember() {
        System.out.println(memberService.insertMember("windowsxp", "abc123"));
        Assert.assertNotNull(memberService.insertMember("windowsxp", "abc123"));
    }
}
```

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;可以看到，通过继承`AbstractJUnit4SpringContextTests`就可以完成构造`applicationContext`的功能。当然通过`ContextConfiguration`指明当前的配置文件所在地，就可以完成`applicationContext`的初始化，同时利用`Autowired`完成配置文件中的Bean的获取。

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;由于在`MemberService.xml`中针对`UserDAO`的mock配置，对应的mock对象会被注入到`MemberSpringTest`中，而后续的测试方法就可以针对它来编排mock逻辑。

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;我们在`Before`逻辑中以及方法中均可以自由的裁剪mock逻辑，这样`JUnit`、`spring-test`和`Mockito`完美的统一到了一起。