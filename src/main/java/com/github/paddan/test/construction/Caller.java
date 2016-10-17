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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author patrik.lindefors
 */
public final class Caller {

    private Caller() {
    }

    /**
     * Constructs an object from the specified class.
     *
     * @param clazz Type of object to create
     * @param args Any arguments required by the constructor
     *
     * @return The newly created object
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static <T> T construct(Class<? extends T> clazz, Object... args)
        throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

        Constructor<? extends T> constructor;
        if (args != null) {
            List<Class<?>> types = Arrays.stream(args).map(Object::getClass).collect(Collectors.<Class<?>>toList());
            constructor = clazz.getDeclaredConstructor(types.toArray(new Class[0]));
        } else {
            constructor = clazz.getDeclaredConstructor((Class<?>[]) null);
        }

        constructor.setAccessible(true);

        return constructor.newInstance(args);
    }

    public static Object callStatic(Class<?> invokeOn, String name, Object... args)
        throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        Class<?>[] types = null;
        if (args != null) {
            types = Arrays.stream(args).map(Object::getClass).collect(Collectors.<Class<?>>toList()).toArray(new Class[0]);
        }

        Method method = getMethod(name, types, invokeOn);

        method.setAccessible(true);
        return method.invoke(invokeOn, args);
    }

    public static Object callMethod(Object invokeOn, String name, Object... args)
        throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        Class<?>[] types = null;
        if (args != null) {
            types = Arrays.stream(args).map(Object::getClass).collect(Collectors.<Class<?>>toList()).toArray(new Class[0]);
        }

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
            } catch (NoSuchMethodException e) {
                classType = classType.getSuperclass();
                if (classType == Object.class) {
                    throw new IllegalArgumentException("Couldn't find method " + name, e);
                }
            }
        }
        return method;
    }
}
