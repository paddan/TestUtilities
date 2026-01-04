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

/**
 * Provides utility methods for constructing objects and invoking methods using reflection.
 * <p>
 * The {@link #construct(Class, Object...)} method allows creating an instance of a class with the specified constructor arguments.
 * The {@link #callStatic(Class, String, Object...)} method allows invoking a static method on a class with the specified arguments.
 * The {@link #callMethod(Object, String, Object...)} method allows invoking an instance method on an object with the specified arguments.
 *
 * @author patrik.lindefors
 */
public final class Caller {

    private Caller() {
    }

    /**
     * Constructs an object from the specified class.
     *
     * @param clazz Type of object to create
     * @param args  Any arguments required by the constructor
     * @return The newly created object
     */
    public static <T> T construct(Class<? extends T> clazz, Object... args)
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

        Constructor<? extends T> constructor;
        if (args != null) {
            constructor = clazz.getDeclaredConstructor(Arrays.stream(args).map(Object::getClass).toArray(Class[]::new));
        } else {
            constructor = clazz.getDeclaredConstructor((Class<?>[]) null);
        }

        constructor.setAccessible(true);

        return constructor.newInstance(args);
    }

    /**
     * Invokes the specified static method on the given class with the provided arguments.
     *
     * @param invokeOn The class on which to invoke the method.
     * @param name The name of the method to invoke.
     * @param args The arguments to pass to the method.
     * @return The result of invoking the method.
     * @throws IllegalAccessException If the method cannot be accessed.
     * @throws InvocationTargetException If the method throws an exception.
     */
    public static Object callStatic(Class<?> invokeOn, String name, Object... args)
            throws IllegalAccessException, InvocationTargetException {

        Class<?>[] types = null;
        if (args != null) {
            types = Arrays.stream(args).map(Object::getClass).toArray(Class[]::new);
        }

        Method method = getMethod(name, types, invokeOn);

        method.setAccessible(true);
        return method.invoke(null, args);
    }

    /**
     * Invokes the specified method on the given object with the provided arguments.
     *
     * @param invokeOn The object on which to invoke the method.
     * @param name The name of the method to invoke.
     * @param args The arguments to pass to the method.
     * @return The result of invoking the method.
     * @throws IllegalAccessException If the method cannot be accessed.
     * @throws InvocationTargetException If the method throws an exception.
     */
    public static Object callMethod(Object invokeOn, String name, Object... args)
            throws IllegalAccessException, InvocationTargetException {

        Class<?>[] types = null;
        if (args != null) {
            types = Arrays.stream(args).map(Object::getClass).toArray(Class[]::new);
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
