package com.murdock.tools.mockito;

import com.murdock.tools.mockito.dao.UserDAO;
import com.murdock.tools.mockito.service.MemberService;
import com.murdock.tools.mockito.service.MemberServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author weipeng2k 2018年09月19日 下午16:14:16
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MemberJavaConfigTest.MemberServiceConfig.class)
public class MemberJavaConfigTest {

    @Autowired
    private MemberService memberService;

    @Test
    public void insert_member() {
        System.out.println(memberService.insertMember("windowsxp", "abc123"));
        Assert.assertNotNull(memberService.insertMember("windowsxp", "abc123"));
    }

    @Configuration
    static class MemberServiceConfig {
        @Bean
        public MemberService memberService() {
            return new MemberServiceImpl();
        }
        @Bean
        public UserDAO userDAO() {
            UserDAO mock = Mockito.mock(UserDAO.class);
            Mockito.when(mock.insertMember(Mockito.any())).thenReturn(System.currentTimeMillis());
            return mock;
        }
    }
}
