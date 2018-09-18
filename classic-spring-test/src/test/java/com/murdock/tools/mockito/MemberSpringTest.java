/**
 *
 */
package com.murdock.tools.mockito;

import com.murdock.tools.mockito.dao.UserDAO;
import com.murdock.tools.mockito.service.MemberService;
import com.murdock.tools.mockito.service.MemberServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * @author weipeng2k
 */
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
        memberService.insertMember(
                "akdjflajsdlfjaasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfsadfasdfasf", "abcdcsfa123");
    }

    @Test
    public void insertMember() {
        System.out.println(memberService.insertMember("windowsxp", "abc123"));
        Assert.assertNotNull(memberService.insertMember("windowsxp", "abc123"));
    }
}
