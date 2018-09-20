# 现代化的spring-test使用方式

> 以下例子可以在`javaconfig-spring-test`中找到。

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;在`classic-spring-test`中演示的单元测试，还是用配置文件的方式，但是从Spring4之后，官方就鼓励使用Java的方式对spring进行配置，而不是用以前那样的xml配置形式了，因此我们基于注解可以来简化单元测试的编写，我们称之为现代化的`spring-test`方式。

## 修改单元测试

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;测试不用继承`AbstractJUnit4SpringContextTests`，通过注解即可，然后对于bean的配置，可以通过Java配置风格完成

### 注解

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;使用`RunWith`和`ContextConfiguration`配置即可将一个类声明为支持Spring容器的测试用例。

```java
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MemberJavaConfigTest.MemberServiceConfig.class)
public class MemberJavaConfigTest {
}
```

|注解|说明|
|-----|-----|
|`RunWith`|该注解是`junit`提供的，表示用那种方式来执行这个测试，这里是`SpringRunner`，由`spring-test`提供|
|`ContextConfiguration`|对测试的Spring容器的配置，比如：配置的位置等|

### 配置

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;通过注解可以声明按照何种方式去执行测试，以及测试的Spring容器如何组装，还或缺在Spring容器中如何配置Bean。

```java
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MemberJavaConfigTest.MemberServiceConfig.class)
public class MemberJavaConfigTest {

    @Autowired
    private MemberService memberService;

    @Test
    public void insertMember() {
        System.out.println(memberService.insertMember("windowsxp", "abc123"));
        Assert.assertNotNull(memberService.insertMember("windowsxp", "abc123"));
    }

    @Configuration
    static class MemberServiceConfig {

        @Bean
        public MemberService memberService(UserDAO userDAO) {
            MemberServiceImpl memberService =  new MemberServiceImpl();
            memberService.setUserDAO(userDAO);
            return memberService;
        }

        @Bean
        public UserDAO userDAO() {
            UserDAO mock = Mockito.mock(UserDAO.class);
            Mockito.when(mock.insertMember(Mockito.any())).thenReturn(System.currentTimeMillis());
            return mock;
        }
    }
}
```

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;可以看到只需要有一个类，被注解了`Configuration`，该类就是一个配置类型，而这种**Java Config Style**已经是Spring官方推荐的方式了。

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Bean`注解类似xml中的`bean`标签，这里配置了两个Bean一个`MemberService`的实现，另外一个是mock的`UserDAO`。其中对`MemberService`的配置需要依赖`UserDAO`。

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;剩下的测试过程就和之前`classic-spring-test`完全一致了，可以看到新的方式没有了恼人的xml配置，变得更加直接和高效。