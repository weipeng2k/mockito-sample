package com.murdock.tools.mockito.dao.impl;

import com.murdock.tools.mockito.dao.UserDAO;
import com.murdock.tools.mockito.dao.config.MyBatisConfig;
import com.murdock.tools.mockito.dao.dataobject.MemberDO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @author weipeng2k 2022-11-08 20:39:14
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserDAOImplTest.Config.class)
@TestPropertySource(locations = "classpath:test-application.properties")
@EnableAutoConfiguration
public class UserDAOImplTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private UserDAO userDAO;

    @Test
    public void findMember() {
        MemberDO member = new MemberDO();
        member.setId(1L);
        member.setName("name");
        member.setPassword("password");
        member.setGmtCreate(new Date());
        member.setGmtModified(new Date());
        userDAO.insertMember(member);
        MemberDO name = userDAO.findMember("name");
        Assert.assertNotNull(name);
        Assert.assertEquals("password", name.getPassword());
    }

    @Import(MyBatisConfig.class)
    @Configuration
    static class Config {
        @Bean
        UserDAO userDAO() {
            return new UserDAOImpl();
        }
    }
}