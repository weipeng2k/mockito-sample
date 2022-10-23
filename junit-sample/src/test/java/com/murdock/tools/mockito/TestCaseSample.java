package com.murdock.tools.mockito;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * UnitTest Case
 * </pre>
 *
 * @author weipeng2k 2022年10月22日 晚上 10:34:31
 */
public class TestCaseSample {

    @Test
    public void stringRUtilsTest() {
        Assert.assertTrue(StringUtils.isNumeric("12345"));
        Assert.assertTrue(StringUtils.isBlank("    "));
        Assert.assertEquals("XXXXX", StringUtils.upperCase("xxXxx"));
        Assert.assertTrue(StringUtils.leftPad("123", 5).length() >= 5);
    }

    @Test(timeout = 330)
    public void timeoutTest() throws Exception {
        TimeUnit.MILLISECONDS.sleep(300);
    }

    @Test(expected = NullPointerException.class)
    public void exception() {
        String s = null;
        s.length();
    }
}
