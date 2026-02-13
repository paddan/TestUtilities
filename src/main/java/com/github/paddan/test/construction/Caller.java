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

        Object[] safeArgs = args == null ? new Object[0] : args;
        Constructor<? extends T> constructor;
        if (args == null) {
            try {
                constructor = findConstructor(clazz, safeArgs);
            } catch (NoSuchMethodException noArgsMissing) {
                safeArgs = new Object[]{null};
                constructor = findConstructor(clazz, safeArgs);
            }
        } else {
            constructor = findConstructor(clazz, safeArgs);
        }
        constructor.setAccessible(true);

        return constructor.newInstance(safeArgs);
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

        Object[] safeArgs = args == null ? new Object[0] : args;
        Method method;
        if (args == null) {
            try {
                method = getMethod(name, safeArgs, invokeOn);
            } catch (IllegalArgumentException noArgsMissing) {
                safeArgs = new Object[]{null};
                method = getMethod(name, safeArgs, invokeOn);
            }
        } else {
            method = getMethod(name, safeArgs, invokeOn);
        }

        method.setAccessible(true);
        return method.invoke(null, safeArgs);
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

        Object[] safeArgs = args == null ? new Object[0] : args;
        Method method;
        if (args == null) {
            try {
                method = getMethod(name, safeArgs, invokeOn.getClass());
            } catch (IllegalArgumentException noArgsMissing) {
                safeArgs = new Object[]{null};
                method = getMethod(name, safeArgs, invokeOn.getClass());
            }
        } else {
            method = getMethod(name, safeArgs, invokeOn.getClass());
        }
        method.setAccessible(true);

        return method.invoke(invokeOn, safeArgs);
    }

    private static Method getMethod(String name, Object[] args, Class<?> type) {
        Class<?> classType = type;

        while (classType != null) {
            Method[] methods = classType.getDeclaredMethods();
            for (Method candidate : methods) {
                if (!candidate.getName().equals(name)) {
                    continue;
                }
                if (areParametersCompatible(candidate.getParameterTypes(), args)) {
                    return candidate;
                }
            }
            classType = classType.getSuperclass();
        }

        throw new IllegalArgumentException("Couldn't find method " + name);
    }

    private static <T> Constructor<? extends T> findConstructor(Class<? extends T> clazz, Object[] args)
            throws NoSuchMethodException {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> candidate : constructors) {
            if (areParametersCompatible(candidate.getParameterTypes(), args)) {
                @SuppressWarnings("unchecked")
                Constructor<? extends T> ctor = (Constructor<? extends T>) candidate;
                return ctor;
            }
        }
        throw new NoSuchMethodException("Couldn't find constructor for " + clazz.getName());
    }

    private static boolean areParametersCompatible(Class<?>[] parameterTypes, Object[] args) {
        if (parameterTypes.length != args.length) {
            return false;
        }
        for (int i = 0; i < parameterTypes.length; i++) {
            if (!isCompatible(parameterTypes[i], args[i])) {
                return false;
            }
        }
        return true;
    }

    private static boolean isCompatible(Class<?> parameterType, Object arg) {
        if (arg == null) {
            return !parameterType.isPrimitive();
        }
        Class<?> argType = arg.getClass();
        if (parameterType.isPrimitive()) {
            parameterType = wrapPrimitive(parameterType);
        }
        return parameterType.isAssignableFrom(argType);
    }

    private static Class<?> wrapPrimitive(Class<?> type) {
        if (type == boolean.class) {
            return Boolean.class;
        }
        if (type == byte.class) {
            return Byte.class;
        }
        if (type == char.class) {
            return Character.class;
        }
        if (type == short.class) {
            return Short.class;
        }
        if (type == int.class) {
            return Integer.class;
        }
        if (type == long.class) {
            return Long.class;
        }
        if (type == float.class) {
            return Float.class;
        }
        if (type == double.class) {
            return Double.class;
        }
        return type;
    }
}
