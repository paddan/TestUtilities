//MIT License
//
//    Copyright (c) 2016 TestUtilities by Patrik Lindefors
//
//    Permission is hereby granted, free of charge, to any person obtaining a copy
//    of this software and associated documentation files (the "Software"), to deal
//    in the Software without restriction, including without limitation the rights
//    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//    copies of the Software, and to permit persons to whom the Software is
//    furnished to do so, subject to the following conditions:
//
//    The above copyright notice and this permission notice shall be included in all
//    copies or substantial portions of the Software.
//
//    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
//    SOFTWARE.

package com.github.paddan.test.construction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * @author patrik.lindefors
 */
public class TestInvoker {

    @Test
    public void shouldInvokeNoArgsPrivateConstructor() throws Exception {
        PrivateClass privateClass = Caller.callConstructor(PrivateClass.class);
        assertNotNull(privateClass);
    }


    @Test
    public void shouldInvokePrivateConstructorWithArgs() throws Exception {
        PrivateClass privateClass = Caller.callConstructor(PrivateClass.class, new Class<?>[] {String.class, Long.class}, new Object[] {
            "hej", new Long(10)});
        assertNotNull(privateClass);
    }


    @Test
    public void shouldInvokeNoArgsPrivateMethod() throws Exception {
        PrivateClass privateClass = Caller.callConstructor(PrivateClass.class, new Class<?>[] {String.class, Long.class}, new Object[] {
            "flirp", new Long(10)});
        String retVal = (String) Caller.callMethod(privateClass, "returnString");

        assertNotNull(retVal);
        assertEquals("flirp", retVal);
    }


    @Test
    public void shouldInvokePrivateMethodWithArgs() throws Exception {
        PrivateClass privateClass = Caller.callConstructor(PrivateClass.class);
        String retVal = (String) Caller.callMethod(privateClass, "returnString", new Class<?>[] {String.class}, new String[] {"flopp"});

        assertNotNull(retVal);
        assertEquals("flopp", retVal);

    }


    @Test
    public void shouldInvokeNoArgsPrivateStaticMethod() throws Exception {
        String retVal = (String) Caller.callMethod(PrivateClass.class, "returnStringFromStatic");

        assertNotNull(retVal);
        assertEquals("hej", retVal);
    }


    @Test
    public void shouldInvokePrivateStaticMethodWithArgs() throws Exception {
        String retVal = (String) Caller.callMethod(PrivateClass.class, "returnStringFromStatic", new Class<?>[] {String.class},
            new String[] {"flipp"});

        assertNotNull(retVal);
        assertEquals("flipp", retVal);
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionDueToStaticModifier() throws Exception {
        Caller.callMethod(PrivateClass.class, "returnString");
    }
}
