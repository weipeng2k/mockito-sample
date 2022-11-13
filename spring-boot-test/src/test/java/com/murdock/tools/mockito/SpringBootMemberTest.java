package com.murdock.tools.mockito;

import com.murdock.tools.mockito.dao.UserDAO;
import com.murdock.tools.mockito.service.MemberService;
import com.murdock.tools.mockito.service.MemberServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author weipeng2k 2018年09月20日 下午19:28:57
 */
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
        public MemberService memberService() {
            return new MemberServiceImpl();
        }
    }

}
