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


package com.github.paddan.test.injection;

import static org.mockito.Mockito.mock;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class AutoInjector {

    private AutoInjector() {
    }

    /**
     * Container for all the mocks created by the AutoInjector
     */
    public interface Mocks {
        /**
         * Gets a mock by its type
         *
         * @param clazz The type of the injected mock
         * @return The mocked instance
         */
        <T> T get(Class<? extends T> clazz);

        /**
         * Gets a mock by its name, only works for fields
         *
         * @param name The name of the injected field
         * @return The mocked instance
         */
        <T> T get(String name);
    }

    /**
     * Automatically create and inject mocks based on annotations and names.
     * Annotations work for both fields and methods, names work only for fields.
     *
     * @param target           The instance to inject into
     * @param injectionTargets A list of annotations used for injection (e.g EJB.class,
     *                         Inject.class) and names of specific fields.
     * @return A Mocks instance from which you can get the mocks that are
     * injected by name or by type.
     */
    public static Mocks autoMock(Object target, Object... injectionTargets) throws IllegalAccessException, InvocationTargetException {
        if (injectionTargets.length == 0) {
            throw new IllegalArgumentException("Needs at least one annotation to use as inject targets");
        }

        Map<Class<?>, Object> typeMocks = new HashMap<Class<?>, Object>();
        Map<String, Object> namedMocks = new HashMap<String, Object>();
        Class<?> targetClass = target.getClass();
        List<Object> injectionTargetsList = Arrays.asList(injectionTargets);

        while (targetClass != Object.class) {
            injectMocks(target, targetClass, injectionTargetsList, typeMocks, namedMocks);
            targetClass = targetClass.getSuperclass();
        }

        verifyNamedMocks(injectionTargetsList, namedMocks.keySet());

        return createMocksInstance(typeMocks, namedMocks);
    }

    private static void verifyNamedMocks(List<Object> injectionTargets, Set<String> injectedNames) {
        for (Object target : injectionTargets) {
            if (target instanceof String && !injectedNames.contains(target)) {
                throw new IllegalArgumentException("Could not find a field with " + target);
            }
        }
    }

    private static Mocks createMocksInstance(final Map<Class<?>, Object> typeMocks, final Map<String, Object> namedMocks) {
        return new Mocks() {
            @SuppressWarnings("unchecked")
            public <T> T get(Class<? extends T> clazz) {
                Object mocked = typeMocks.get(clazz);
                if (mocked == null) {
                    throw new IllegalArgumentException("Could not find mock for " + clazz.getName());
                }
                return (T) mocked;
            }

            @SuppressWarnings("unchecked")
            public <T> T get(String name) {
                Object mocked = namedMocks.get(name);
                if (mocked == null) {
                    throw new IllegalArgumentException("Could not find mock for field with " + name);
                }
                return (T) mocked;
            }
        };
    }

    private static void injectMocks(Object target, Class<?> targetClass, List<Object> injectionTargets, Map<Class<?>, Object> typeMocks,
                                    Map<String, Object> namedMocks) throws IllegalAccessException, InvocationTargetException {
        Field[] declaredFields = targetClass.getDeclaredFields();
        for (Field field : declaredFields) {
            if (shouldInject(field.getName(), field, injectionTargets)) {
                field.setAccessible(true);
                Class<?> classToMock = field.getType();
                field.set(target, addMock(field.getName(), classToMock, typeMocks, namedMocks));
            }
        }
        Method[] declaredMethods = targetClass.getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (shouldInject(null, method, injectionTargets)) {
                Class<?> classToMock = method.getParameterTypes()[0];
                method.invoke(target, addMock(null, classToMock, typeMocks, namedMocks));
            }
        }
    }

    private static Object addMock(String name, Class<?> classToMock, Map<Class<?>, Object> typeMocks, Map<String, Object> namedMocks) {
        Object mocked = typeMocks.get(classToMock);
        if (mocked == null) {
            mocked = mock(classToMock);
            typeMocks.put(classToMock, mocked);
        }
        if (name != null) {
            namedMocks.put(name, mocked);
        }
        return mocked;
    }

    @SuppressWarnings("unchecked")
    private static boolean shouldInject(String objectName, AccessibleObject accessibleObject, List<Object> injectionTargets) {
        for (Object injectionTarget : injectionTargets) {
            if (injectionTarget instanceof Class) {
                if (!((Class<?>) injectionTarget).isAnnotation()) {
                    throw new IllegalArgumentException(((Class<?>) injectionTarget).getName() + " is a class but not an annotation");
                }
                if (accessibleObject.isAnnotationPresent((Class<? extends Annotation>) injectionTarget)) {
                    return true;
                }
            } else if (injectionTarget instanceof String) {
                String name = (String) injectionTarget;
                return name.equals(objectName);
            } else {
                throw new IllegalArgumentException("Injection targets can only be annotations (e.g EJB.class) and strings with the name of fields");
            }
        }
        return false;
    }

}
