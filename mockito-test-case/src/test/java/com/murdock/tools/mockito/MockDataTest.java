package com.murdock.tools.mockito;

import com.github.jsonzou.jmockdata.JMockData;
import org.junit.Test;

/**
 * @author weipeng2k 2020年06月22日 下午13:04:56
 */
public class MockDataTest {

    @Test
    public void mockMember() {
        Member mock = JMockData.mock(Member.class);
        System.out.println(mock);
    }

    @Test
    public void mockStudent() {
        Student mock = JMockData.mock(Student.class);
        System.out.println(mock);
    }
}
