# SpringBoot环境下的测试方法

> 以下例子可以在`spring-boot-test`中找到。

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Spring`框架实际上是依靠**SpringBoot**完成了续命，由它焕发了第二春，打开了一个全新的战场。在今天微服务大放异彩的环境下，针对**SpringBoot**的测试也会有所不同。

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`SpringBoot`实际是用来启动你的应用，所以它会有配置以及一系列约定大于配置的环境准备，所以需要依赖`spring-boot-test`支持来完成单元测试。

## 修改单元测试

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;如果需要在单元测试启动时启动**SpringBoot**，需要做一下相关的配置，增加一些注解。

### 依赖

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;增加依赖：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-test</artifactId>
    <scope>test</scope>
</dependency>
```

### 注解

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;和`JavaConfig`的方式非常类似，通过注解可以声明该测试是`SpringBootTest`，并且可以指定运行的`SpringBoot`容器的配置。

```java
@SpringBootTest(classes = SpringBootMemberTest.Config.class)
@TestPropertySource(locations = "classpath:test-application.properties")
@RunWith(SpringRunner.class)
public class SpringBootMemberTest {
```

|注解|说明|
|-----|-----|
|`SpringBootTest`|描述了该SpringBoot单元测试是根据哪个配合来启动容器|
|`TestPropertySource`|应用的配置使用哪个|

### 配置

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;通过注解可以声明按照何种方式去执行测试，以及测试的Spring容器如何组装，还或缺在Spring容器中如何配置Bean。

```java
@SpringBootTest(classes = SpringBootMemberTest.Config.class)
@TestPropertySource(locations = "classpath:test-application.properties")
@RunWith(SpringRunner.class)
public class SpringBootMemberTest {

    @Autowired
    private Environment env;
    @MockBean
    private UserDAO userDAO;
    @Autowired
    private MemberService memberService;

    @Test
    public void environment() {
        Assert.assertEquals("Alibaba", env.getProperty("brand-owner.name"));
    }

    @Before
    public void init() {
        Mockito.when(userDAO.insertMember(Mockito.any())).thenReturn(System.currentTimeMillis());
    }

    @Test
    public void insert_member() {
        System.out.println(memberService.insertMember("windowsxp", "abc123"));
        Assert.assertNotNull(memberService.insertMember("windowsxp", "abc123"));
    }

    @Configuration
    static class Config {

        @Bean
        public MemberService memberService(UserDAO userDAO) {
            MemberServiceImpl memberService =  new MemberServiceImpl();
            memberService.setUserDAO(userDAO);
            return memberService;
        }
    }

}
```

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;可以看到新增了一个注解`MockBean`，这个用来帮助我们创建一个Mock的`UserDAO`，而不用通过编码来进行创建，回忆之前在`classic`以及`javaconfig`中的Mock方式，都需要调用`Mockito.mock(Class type)`方法来创建一个Mock对象，而在`SpringBootTest`中就不需要了，直接在成员变量上增加`MockBean`的注解就可以了。

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;同时可以看到在单元测试中增加了一个注入属性，`Environment`，它代表Spring运行的环境，可以从中获取配置，以下是`test-application.application`中的内容：

```sh
brand-owner.name=Alibaba
brand-owner.company=Alibaba-inc.
```

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;在`environment`测试方法中，可以访问测试的配置内容，从这里可以看到`SpringBootTest`在`spring-test`基础上，除了启动一个Spring容器，还准备好了一个`SpringBoot`运行时环境。

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;但是从侧面上讲，使用`SpringBootTest`就依赖了运行时环境，这不是一个好的选择，所以在大多数情况下，对于代码的单元测试`spring-test`就可以完全应对。