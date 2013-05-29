mockito-sample
==============

一个使用mockito和spring-test的例子

引言
	Java单元测试框架在业界非常多，以JUnit为事实上的标准，而JUnit只是解决了单元测试的基本骨干，而对于Mock的支持却没有。而同样，在Mock方面，Java也有很多开源的选择，诸如JMock、EasyMock和Mockito，而Mockito也同样为其中的翘楚，二者能够很好的完成单元测试的工作。本文就是介绍如何使用二者来完成单元测试。
	献给Molly。
存在的问题
	如果公司自己搞一个单元测试框架，维护将成为一个大问题，而使用业界成熟的解决方案，将会是一个很好的方式。因为会有一组非常专业的人替你维护，而且不断地有新的Feature可以使用，同样你熟悉这些之后你可以不断的复用这些知识，而不会由于局限在某个特定的框架下（其实这些特定的框架也只是封装了业界的开源方案）。
解决方案
	使用JUnit做单元测试的主体框架，如果有Spring的支持，可以使用spring-test进行支持，对于层与层之间的Mock，则使用Mockito来完成。

使用spring-test和Mockito进行单元测试
	以下例子，可以访问https://github.com/weipeng2k/mockito-sample获取。下面的例子，主要介绍如何使用Mockito和spring-test来完成测试。
使用Mockito进行mock
	Mockito可以使用简单的mock方法来完成一个mock对象的构造，并通过when和then来友好的加入mock逻辑。看一个例子：
/**
 * @author weipeng2k
 * 
 */
public class VerifyTest {

	@SuppressWarnings("unchecked")
	@Test
	public void test() {
		List<String> list = Mockito.mock(List.class);

		Mockito.when(list.get(0)).thenReturn("one");

		System.out.println(list.get(0));

		Assert.assertEquals("one", list.get(0));

		Mockito.when(list.get(Mockito.anyInt())).thenAnswer(
				new Answer<String>() {

					@Override
					public String answer(InvocationOnMock invocation)
							throws Throwable {
						Object[] args = invocation.getArguments();
						int index = Integer.parseInt((String) args[0]
								.toString());
						// int index = (int) args[0];
						if (index == 0) {
							return "0";
						} else if (index == 1) {
							return "1";
						} else if (index == 2) {
							throw new RuntimeException();
						} else {
							return String.valueOf(index);
						}
					}

				});

		for (int i = 0; i < 10; i++) {
			try {
				System.out.print("call list.get " + i + " ");
				System.out.println(list.get(i));
				Assert.assertEquals(String.valueOf(i), list.get(i));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}
构造Mock
	上面代码中Mockito.mock可以构造一个Mock对象，这个对象没有任何作用，如果调用它的方法，如果有返回值的话，它会返回null。这个时候可以向其中加入mock逻辑，比如：Mockito.when(xxx.somemethod()).thenReturn(xxx)，这段逻辑就会在当有外界调用xxx.somemethod时，返回那个在thenReturn中的对象。
复杂的Mock逻辑
	有时候需要构造复杂的返回逻辑，比如参数为1的时候，返回一个值，为2的时候，返回另一个值。那么when和thenAnswer就可以满足要求。
断言选择
	当然我们可以使用System.out.println来完成目测，但是有时候需要让JUnit插件或者maven的surefire插件能够捕获住测试的失败，这个时候就需要使用断言了。我们使用org.junit.Assert来完成断言的判断，可以看到通过简单的assertEquals就可以了，当然该类提供了一系列的assertXxx来完成断言。

下面我们看一个较为真实的例子，比如我们有个MemberService用来insertMember。
/**
 * @author weipeng2k
 * 
 */
public interface MemberService {
	/**
	 * 插入一个会员
	 * 
	 * 其中 name不能超过32个字符，不能为空 password不能全部是数字，长度不能低于6，不超过16
	 * 
	 * @param name
	 * @param password
	 * @return
	 */
	Long insertMember(String name, String password)
			throws IllegalArgumentException;
}
其对应的实现
/**
 * @author weipeng2k
 * 
 */
public class MemberServiceImpl implements MemberService {

	private UserDAO userDAO;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.murdock.tools.mockito.service.MemberService#insertMember(java.lang
	 * .String, java.lang.String)
	 */
	@Override
	public Long insertMember(String name, String password)
			throws IllegalArgumentException {
		if (name == null || password == null) {
			throw new IllegalArgumentException();
		}

		if (name.length() > 32 || password.length() < 6
				|| password.length() > 16) {
			throw new IllegalArgumentException();
		}

		boolean pass = false;
		for (Character c : password.toCharArray()) {
			if (!Character.isDigit(c)) {
				pass = true;
				break;
			}
		}
		if (!pass) {
			throw new IllegalArgumentException();
		}

		Member member = userDAO.findMember(name);
		if (member != null) {
			throw new IllegalArgumentException();
		}

		member = new Member();
		member.setName(name);
		member.setPassword(password);
		Long id = userDAO.insertMember(member);

		return id;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

}
	可以看到实现通过聚合了userDAO，来完成操作，而业务层的代码的单元测试代码，就必须隔离UserDAO，也就是说要Mock这个UserDAO。
	下面我们就使用Mockito来完成Mock操作。
/**
 * @author weipeng2k
 * 
 */
public class MemberWithoutSpringTest {
	private MemberService memberService = new MemberServiceImpl();

	@Before
	public void mockUserDAO() {
		UserDAO userDAO = Mockito.mock(UserDAO.class);
		Member member = new Member();
		member.setName("weipeng");
		member.setPassword("123456abcd");
		Mockito.when(userDAO.findMember("weipeng")).thenReturn(member);

		Mockito.when(userDAO.insertMember((Member) Mockito.any())).thenReturn(
				System.currentTimeMillis());

		((MemberServiceImpl) memberService).setUserDAO(userDAO);
	}

	@Test(expected = IllegalArgumentException.class)
	public void insertMemberError() {
		memberService.insertMember(null, "123");

		memberService.insertMember(null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void insertExistMember() {
		memberService.insertMember("weipeng", "1234abc");
	}

	@Test(expected = IllegalArgumentException.class)
	public void insertIllegalArgument() {
		memberService
				.insertMember(
						"akdjflajsdlfjaasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfsadfasdfasf",
						"abcdcsfa123");
	}

	@Test
	public void insertMember() {
		System.out.println(memberService.insertMember("windowsxp", "abc123"));
		Assert.assertNotNull(memberService.insertMember("windowsxp", "abc123"));
	}
}
	可以看到，在测试开始的时候，利用了Before来完成Mock对象的构建，也就是说在test执行之前完成了Mock对象的初始化工作。
	但仔细看上述代码中，红色的部分，就不是很好了，它依赖了实现，也就是说我们的测试是不能依赖接口的实现的。这在Martin Flower的文章多次提到，那么我们如何避免这样的强依赖出现呢？结论就是使用spring-test来完成。
	
加入spring-test与之整合
	spring-test是springframework中一个模块，主要也是由spring作者Juergen Hoeller来完成的，它可以方便的测试基于spring的代码。
配置文件
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:aop="http://www.springframework.org/schema/aop"
	xmlns:tx="http://www.springframework.org/schema/tx"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd
            http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-2.5.xsd
            http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-2.5.xsd"
	default-autowire="byName">

	<bean id="memberService" class="com.murdock.tools.mockito.service.MemberServiceImpl" />

	<bean id="userDAO" class="org.mockito.Mockito" factory-method="mock">
		<constructor-arg>
			<value>com.murdock.tools.mockito.dao.UserDAO</value>
		</constructor-arg>
	</bean>
</beans>
	在进行spring测试之前，我们必须有一个spring的配置文件，用来构造applicationContext，注意上面红色的部分，这个UserDAO就是MemberServiceImpl需要的，而它利用了spring的FactoryBean方式，通过mock工厂方法完成了Mock对象的构造，其中的构造函数表明了这个Mock是什么类型的。只用在配置文件中声明一下就可以了。
测试代码
/**
 * @author weipeng2k
 * 
 */
@ContextConfiguration(locations = { "classpath:MemberService.xml" })
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
		Mockito.when(userDAO.insertMember((Member) Mockito.any())).thenReturn(
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
		memberService
				.insertMember(
						"akdjflajsdlfjaasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfsadfasdfasf",
						"abcdcsfa123");
	}

	@Test
	public void insertMember() {
		System.out.println(memberService.insertMember("windowsxp", "abc123"));
		Assert.assertNotNull(memberService.insertMember("windowsxp", "abc123"));
	}
}
	可以看到，通过继承AbstractJUnit4SpringContextTests就可以完成构造applicationContext的功能。当然通过ContextConfiguration指明当前的配置文件所在地，就可以完成applicationContext的初始化，同时利用Autowired完成配置文件中的Bean的获取。
	我们在Before逻辑中以及方法中均可以自由的裁剪mock逻辑，这样JUnit、spring-test和Mockito完美的统一到了一起。
