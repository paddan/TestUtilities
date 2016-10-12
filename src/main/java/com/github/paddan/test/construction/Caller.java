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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author patrik.lindefors
 *
 */
public final class Caller {

    private Caller() {
    }

    public static <T> T callConstructor(Class<? extends T> clazz) throws NoSuchMethodException, InstantiationException,
        InvocationTargetException, IllegalAccessException {
        return callConstructor(clazz, null, null);
    }

    public static <T> T callConstructor(Class<? extends T> clazz, Class<?>[] types, Object[] args)
        throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor<? extends T> constructor = clazz.getDeclaredConstructor(types);
        constructor.setAccessible(true);

        return constructor.newInstance(args);
    }

    public static Object callMethod(Class<?> invokeOn, String name) throws NoSuchMethodException, IllegalAccessException,
        InvocationTargetException {
        return callMethod(invokeOn, name, null, null);
    }

    public static Object callMethod(Class<?> invokeOn, String name, Class<?>[] types, Object[] args)
        throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = getMethod(name, types, invokeOn);

        method.setAccessible(true);
        return method.invoke(invokeOn, args);
    }

    public static Object callMethod(Object invokeOn, String name) throws NoSuchMethodException, IllegalAccessException,
        InvocationTargetException {
        return callMethod(invokeOn, name, null, null);
    }

    public static Object callMethod(Object invokeOn, String name, Class<?>[] types, Object[] args)
        throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        Method method = getMethod(name, types, invokeOn.getClass());
        method.setAccessible(true);
        return method.invoke(invokeOn, args);
    }

    private static Method getMethod(String name, Class<?>[] types, Class<?> type) {
        Method method = null;
        Class<?> classType = type;

        while (method == null) {
            try {
                method = classType.getDeclaredMethod(name, types);
            }
            catch (NoSuchMethodException e) {
                classType = classType.getSuperclass();
                if (classType == Object.class) {
                    throw new IllegalArgumentException("Couldn't find method " + name, e);
                }
            }
        }
        return method;
    }
}