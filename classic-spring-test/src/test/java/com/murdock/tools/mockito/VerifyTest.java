/**
 *
 */
package com.murdock.tools.mockito;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

/**
 * @author weipeng2k
 */
public class VerifyTest {

    @SuppressWarnings("unchecked")
    @Test
    public void test() {
        List<String> list = Mockito.mock(List.class);

        Mockito.when(list.get(0)).thenReturn("one");

        System.out.println(list.get(0));

        Assert.assertEquals("one", list.get(0));

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

        for (int i = 0; i < 10; i++) {
            try {
                System.out.print("call list.get " + i + " ");
                System.out.println(list.get(i));
                Assert.assertEquals(String.valueOf(i), list.get(i));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
