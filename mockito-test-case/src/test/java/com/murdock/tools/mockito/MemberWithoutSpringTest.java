/**
 * 
 */
package com.murdock.tools.mockito;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.murdock.tools.mockito.dao.UserDAO;
import com.murdock.tools.mockito.service.MemberService;
import com.murdock.tools.mockito.service.MemberServiceImpl;

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
