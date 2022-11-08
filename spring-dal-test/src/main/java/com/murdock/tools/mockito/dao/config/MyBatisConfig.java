package com.murdock.tools.mockito.dao.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;

/**
 * @author weipeng2k 2022-11-08 19:58:47
 */
@Configuration("mybatisConfig")
public class MyBatisConfig {
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(
            @Qualifier("dataSource") DataSource dataSource,
            @Value("classpath:mybatis-config.xml") Resource configLocation) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setConfigLocation(configLocation);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(
            @Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
