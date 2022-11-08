/**
 *
 */
package com.murdock.tools.mockito;

import com.murdock.tools.mockito.dao.dataobject.MemberDO;
import com.murdock.tools.mockito.dao.UserDAO;
import com.murdock.tools.mockito.service.MemberService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.util.stream.IntStream;

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
    }

    @Test(expected = IllegalArgumentException.class)
    public void insert_member_error() {
        memberService.insertMember(null, "123");

        memberService.insertMember(null, null);
    }

    /**
     * 也可以选择在方法中进行mock
     */
    @Test(expected = IllegalArgumentException.class)
    public void insert_exist_member() {
        MemberDO member = new MemberDO();
        member.setName("weipeng");
        member.setPassword("123456abcd");
        Mockito.when(userDAO.findMember("weipeng")).thenReturn(member);

        memberService.insertMember("weipeng", "1234abc");
    }

    @Test(expected = IllegalArgumentException.class)
    public void insert_illegal_argument() {
        StringBuilder sb = new StringBuilder();
        IntStream.range(0, 32).forEach(sb::append);

        memberService.insertMember(sb.toString(), "abcdcsfa123");
    }

    @Test
    public void insert_member() {
        System.out.println(memberService.insertMember("windowsxp", "abc123"));
        Assert.assertNotNull(memberService.insertMember("windowsxp", "abc123"));
    }
}
