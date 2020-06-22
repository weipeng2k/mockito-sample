/**
 *
 */
package com.murdock.tools.mockito;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * @author weipeng2k
 */
public class VerifyTest {

    private static final int TWO_DAYS = 2 * 24 * 60 * 60 * 1000;

    @Test
    public void get_time() {
        Random random = new Random();
        int delta = random.nextInt(TWO_DAYS);
        Date date = new Date(System.currentTimeMillis() + delta);
        System.out.println(date);
    }

    @SuppressWarnings("all")
    @Test
    public void mock_one() {
        List<String> list = Mockito.mock(List.class);

        Mockito.when(list.get(0)).thenReturn("one");

        System.out.println(list.get(0));

        Assert.assertEquals("one", list.get(0));
    }

    @SuppressWarnings("all")
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


    @SuppressWarnings("all")
    @Test
    public void mock_seq() {
        List<String> list = Mockito.mock(List.class);

        Mockito.when(list.get(Mockito.anyInt())).then(
                invocation -> invocation.getArguments()[0].toString());

        IntStream.range(0, 10)
                .forEach(index -> {
                    try {
                        System.out.println("call list.get(" + index + ") = " + list.get(index));
                        Assert.assertEquals(String.valueOf(index), list.get(index));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
    }

}
