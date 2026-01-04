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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.github.paddan.test.utils.FieldHelper.getFields;

/**
 * @author patrik.lindefors
 */
public final class Injector {

    private Object value;
    private Object target;
    private Class<?> classTarget;

    private Injector() {
    }

    public static Injector inject(Object value) {
        Injector injector = new Injector();
        injector.setValue(value);
        return injector;
    }

    /**
     * Automatically inject objects in all fields that's created by the supplied function. It can be a mock function
     * like mockitos mock() or any other function that takes a class as argument and returns an object.
     *
     * @param mock The function used to mock
     * @param into The object into which mocks is inserted
     * @return A map with all fields that's been mocked (name of field -> mock object)
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Map<String, Object> autoInject(Function mock, Object into) {
        Map<String, Object> mocks = new HashMap<>();

        Field[] fields = getFields(into.getClass());
        for (Field field : fields) {
            try {
                Object mockedObject = mock.apply(field.getType());
                mocks.put(field.getName(), mockedObject);
                setField(mockedObject, into, field);
            } catch (Exception e) {
                // Skip fields that cannot be mocked/injected (e.g., final types, primitives, security restrictions)
            }
        }

        return mocks;
    }

    /**
     * Injects a value into a annotated field.
     *
     * @param value               The value to autoInject
     * @param into                The object into which the value is injected
     * @param withAnnotationClass The annotation class with which the field is annotated
     * @return The value injected
     * @throws IllegalAccessException If the field cannot be accessed
     */
    public static <T> T inject(T value, Object into, Class<? extends Annotation> withAnnotationClass)
            throws IllegalAccessException {
        Field[] fields = getFields(into.getClass());
        for (Field field : fields) {
            if (field.getAnnotation(withAnnotationClass) != null && (value == null || field.getType()
                    .isAssignableFrom(value.getClass()))) {
                setField(value, into, field);
                return value;
            }
        }
        throw new IllegalArgumentException(
                "Couldn't inject a " + (value != null ? value.getClass().getName() : "null value") + " into " + into.getClass().getName()
                        + " using annotation " + withAnnotationClass.getName());
    }

    /**
     * Injects a value into a annotated field.
     *
     * @param value               The value to autoInject
     * @param valueClass          The class of the value to autoInject
     * @param into                The object into which the value is injected
     * @param withAnnotationClass The annotation class with which the field is annotated
     * @return The value injected
     * @throws IllegalAccessException If the field cannot be accessed
     */
    public static <T> T inject(T value, Class<? extends T> valueClass, Object into,
                               Class<? extends Annotation> withAnnotationClass) throws IllegalAccessException {
        Field[] fields = getFields(into.getClass());
        for (Field field : fields) {
            if (field.getAnnotation(withAnnotationClass) != null) {
                if ((value == null && field.getType().equals(valueClass)) || (value != null && field.getType()
                        .isAssignableFrom(value.getClass()))) {
                    setField(value, into, field);
                    return value;
                }
            }
        }
        throw new IllegalArgumentException(
                "Couldn't inject a " + (value != null ? value.getClass().getName() : "null value") + " into " + into.getClass()
                        .getName() + " using annotation " + withAnnotationClass.getName());
    }

    /**
     * Injects a value into a with field.
     *
     * @param value The value to autoInject
     * @param into  The object into which the value is injected
     * @param name  The name of the field
     * @return The value injected
     * @throws IllegalAccessException If the field cannot be accessed
     */
    public static <T> T inject(T value, Object into, String name) throws IllegalAccessException {
        Field[] fields = getFields(into.getClass());

        for (Field field : fields) {
            if (field.getName().equals(name) && (value == null || field.getType().isAssignableFrom(value.getClass())
                    || field.getType().isPrimitive())) {
                setField(value, into, field);
                return value;
            }
        }

        throw new IllegalArgumentException(
                "Couldn't inject a " + (value == null ? "null value" : value.getClass().getName()) + " into " + into
                        .getClass().getName() + " using field " + name);
    }

    /**
     * Injects a value into a static field.
     *
     * @param value The value to autoInject
     * @param into  The class into which the value is injected
     * @param name  The name of the field
     * @return The value injected
     * @throws IllegalAccessException If the field cannot be accessed
     */
    public static <T> T inject(T value, Class<?> into, String name) throws IllegalAccessException {
        return injectIntoStatic(value, into, name);
    }

    private static <T> T injectIntoStatic(T value, Class<?> into, String name) throws IllegalAccessException {
        Field[] fields = getFields(into);

        for (Field field : fields) {
            if (field.getName().equals(name) && (value == null || field.getType().isAssignableFrom(value.getClass()))) {
                setField(value, into, field);
                return value;
            }
        }
        throw new IllegalArgumentException(
                "Couldn't inject a " + (value != null ? value.getClass().getName() : "null value") + " into " + into.getName()
                        + " using field " + name);
    }

    private static <T> void setField(T value, Object into, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(into, value);
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Injector into(Object target) {
        this.target = target;
        return this;
    }

    public Injector into(Class<?> target) {
        this.classTarget = target;
        return this;
    }

    public Object with(Class<? extends Annotation> annotation) throws NoSuchFieldException, IllegalAccessException {
        return inject(value, target == null ? classTarget : target, annotation);
    }

    public Object with(String name) throws NoSuchFieldException, IllegalAccessException {
        if (target != null) {
            return inject(value, target, name);
        } else {
            return injectIntoStatic(value, classTarget, name);
        }
    }
}
