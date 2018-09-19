# 使用Mockito进行单元测试

> 以下例子可以在`mockito-test-case`中找到。

## 使用Mockito进行mock

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;先看一下怎样使用Mockito进行一个对象的Mock，首先添加依赖：

```xml
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-all</artifactId>
</dependency>
```

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;接下来尝试对`java.util.List`进行Mock，Mock对于List操作的内容进行构造。

### 构造Mock

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;先看一下最简的使用方式。

```java
public void mock_one() {
    List<String> list = Mockito.mock(List.class);

    Mockito.when(list.get(0)).thenReturn("one");

    System.out.println(list.get(0));

    Assert.assertEquals("one", list.get(0));
}
```

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;上面代码中`Mockito.mock`可以构造一个Mock对象，这个对象没有任何作用，如果调用它的方法，如果有返回值的话，它会返回null。这个时候可以向其中加入mock逻辑，比如：`Mockito.when(xxx.somemethod()).thenReturn(xxx)`，这段逻辑就会在当有外界调用`xxx.somemethod()`时，返回那个在thenReturn中的对象。

### 构造一个复杂的Mock

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;有时我们需要针对输入来构造Mock的输出，简单的when和thenReturn无法支持，这时就需要较为复杂的`Answer`。

```java
@Test(expected = RuntimeException.class)
public void mock_answer() {
    List<String> list = Mockito.mock(List.class);
    Mockito.when(list.get(Mockito.anyInt())).thenAnswer(
            invocation -> {
                Object[] args = invocation.getArguments();
                int index = Integer.parseInt(args[0].toString());
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
            });

    Assert.assertEquals("0", list.get(0));
    Assert.assertEquals("1", list.get(1));
    list.get(2);
}
```

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;有时候需要构造复杂的返回逻辑，比如参数为1的时候，返回一个值，为2的时候，返回另一个值。那么when和thenAnswer就可以满足要求。

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;上面代码可以看到当对于List的任意的输入`Mockito.anyInt()`，会进行`Answer`回调的处理，任何针对List的输入都会经过它的处理。这可以让我完成更加柔性和定制化的Mock操作。

## 断言选择

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;当然我们可以使用System.out.println来完成目测，但是有时候需要让JUnit插件或者maven的surefire插件能够捕获住测试的失败，这个时候就需要使用断言了。我们使用org.junit.Assert来完成断言的判断，可以看到通过简单的assertEquals就可以了，当然该类提供了一系列的assertXxx来完成断言。

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;使用IDEA在进行断言判断时非常简单，比Eclipse要好很多，比如：针对一个`int x`判断它等于0，就可以直接写`x == 0`，然后代码提示生成断言。

## 真实案例

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;下面我们看一个较为真实的例子，比如：我们有个`MemberService`用来insertMember。

```java
public interface MemberService {
    /**
     * <pre>
     * 插入一个会员，返回会员的主键
     * 如果有重复，则会抛出异常
     * </pre>
     *
     * @param name     name不能超过32个字符，不能为空
     * @param password password不能全部是数字，长度不能低于6，不超过16
     * @return PK
     */
    Long insertMember(String name, String password) throws IllegalArgumentException;
}
```

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;其对应的实现。

```java
public class MemberServiceImpl implements MemberService {

	private UserDAO userDAO;

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
			throw new IllegalArgumentException("duplicate member.");
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
```

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;可以看到实现通过聚合了userDAO，来完成操作，而业务层的代码的单元测试代码，就必须隔离UserDAO，也就是说要Mock这个UserDAO。
	
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;下面我们就使用Mockito来完成Mock操作。

```java
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
	public void insert_member_error() {
		memberService.insertMember(null, "123");

		memberService.insertMember(null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void insert_exist_member() {
		memberService.insertMember("weipeng", "1234abc");
	}

	@Test(expected = IllegalArgumentException.class)
	public void insert_illegal_argument() {
		memberService
				.insertMember(
						"akdjflajsdlfjaasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfsadfasdfasf",
						"abcdcsfa123");
	}

	@Test
	public void insert_member() {
		System.out.println(memberService.insertMember("windowsxp", "abc123"));
		Assert.assertNotNull(memberService.insertMember("windowsxp", "abc123"));
	}
}
```

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;可以看到，在测试开始的时候，利用了Before来完成Mock对象的构建，也就是说在test执行之前完成了Mock对象的初始化工作。

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;但仔细看上述代码中，`MemberService`的实现`MemberServiceImpl`是直接构造出来的，它依赖了实现，但是我们的测试最好不要依赖实现进行测试的。同时`UserDAO`也是硬塞给`MemberService`的实现，这是因为我们常用Spring来装配类之间的关系，而单元测试没有Spring的支持，这就使得测试代码需要硬编码的方式来进行组装。

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;那么我们如何避免这样的强依赖和组装代码的出现呢？结论就是使用spring-test来完成。