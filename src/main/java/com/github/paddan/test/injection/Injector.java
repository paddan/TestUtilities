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

import static com.github.paddan.test.utils.FieldHelper.getFields;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author patrik.lindefors
 */
public final class Injector {

    private Object value;
    private Object target;

    private Injector() {
    }

    public static Injector inject(Object value) {
        Injector injector = new Injector();
        injector.setValue(value);
        return injector;
    }

    /**
     * Injects a value into a annotated field.
     *
     * @param value               The value to inject
     * @param into                The object into which the value is injected
     * @param withAnnotationClass The annotation class with which the field is annotated
     * @return The value injected
     * @throws IllegalAccessException If the field cannot be accessed
     * @throws NoSuchFieldException   If the field doesn't exist with the specified type and annotation
     */
    public static <T> T inject(T value, Object into, Class<? extends Annotation> withAnnotationClass) throws IllegalAccessException, NoSuchFieldException {
        Field[] fields = getFields(into.getClass());
        for (Field field : fields) {
            if (field.getAnnotation(withAnnotationClass) != null && (value == null || field.getType().isAssignableFrom(value
                .getClass()))) {
                setField(value, into, field);
                return value;
            }
        }
        throw new IllegalArgumentException("Couldn't inject a " + value.getClass().getName() + " into "
            + into.getClass().getName() + " using annotation " + withAnnotationClass.getName());
    }

    /**
     * Injects a value into a annotated field.
     *
     * @param value               The value to inject
     * @param valueClass          The class of the value to inject
     * @param into                The object into which the value is injected
     * @param withAnnotationClass The annotation class with which the field is annotated
     * @return The value injected
     * @throws IllegalAccessException If the field cannot be accessed
     * @throws NoSuchFieldException   If the field doesn't exist with the specified type and annotation
     */
    public static <T> T inject(T value, Class<? extends T> valueClass, Object into,
                               Class<? extends Annotation> withAnnotationClass) throws IllegalAccessException, NoSuchFieldException {
        Field[] fields = getFields(into.getClass());
        for (Field field : fields) {
            if (field.getAnnotation(withAnnotationClass) != null) {
                if ((value == null && field.getType().equals(valueClass)) || (value != null &&
                    field.getType().isAssignableFrom(value.getClass()))) {
                    setField(value, into, field);
                    return value;
                }
            }
        }
        throw new IllegalArgumentException("Couldn't inject a " + value.getClass().getName() + " into "
            + into.getClass().getName() + " using annotation " + withAnnotationClass.getName());
    }

    private static <T> void setField(T value, Object into, Field field) throws IllegalAccessException,
        NoSuchFieldException {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(into, value);
    }

    /**
     * Injects a value into a with field.
     *
     * @param value      The value to inject
     * @param valueClass This argument has been deprecated
     * @param into       The object into which the value is injected
     * @param name       The name of the field
     * @return The value injected
     * @throws IllegalAccessException If the field cannot be accessed
     * @throws NoSuchFieldException   If the field doesn't exist with the specified type and annotation
     */
    @Deprecated
    public static <T> T inject(T value, Class<? extends T> valueClass, Object into, String name) throws NoSuchFieldException, IllegalAccessException {
        return inject(value, into, name);
    }

    /**
     * Injects a value into a with field.
     *
     * @param value      The value to inject
     * @param into       The object into which the value is injected
     * @param name       The name of the field
     * @return The value injected
     * @throws IllegalAccessException If the field cannot be accessed
     * @throws NoSuchFieldException   If the field doesn't exist with the specified type and annotation
     */
    public static <T> T inject(T value, Object into, String name)
        throws IllegalAccessException, NoSuchFieldException {
        Field[] fields = getFields(into.getClass());

        for (Field field : fields) {
            if (field.getName().equals(name) && (value == null || field.getType().isAssignableFrom(value.getClass()) || field
                .getType().isPrimitive())) {
                setField(value, into, field);
                return value;
            }
        }
        throw new IllegalArgumentException("Couldn't inject a " + value.getClass().getName() + " into "
            + into.getClass().getName() + " using field " + name);
    }

    /**
     * Injects a value into a static field.
     *
     * @param value      The value to inject
     * @param valueClass This argument has been deprecated
     * @param into       The class into which the value is injected
     * @param name       The name of the field
     * @return The value injected
     * @throws IllegalAccessException If the field cannot be accessed
     * @throws NoSuchFieldException   If the field doesn't exist with the specified type and annotation
     */
    @Deprecated
    public static <T> T inject(T value, Class<? extends T> valueClass, Class<?> into, String name) throws NoSuchFieldException, IllegalAccessException {
        return inject(value, into, name);
    }

    /**
     * Injects a value into a static field.
     *
     * @param value      The value to inject
     * @param into       The class into which the value is injected
     * @param name       The name of the field
     * @return The value injected
     * @throws IllegalAccessException If the field cannot be accessed
     * @throws NoSuchFieldException   If the field doesn't exist with the specified type and annotation
     */
    public static <T> T inject(T value, Class<?> into, String name)
        throws IllegalAccessException, NoSuchFieldException {
        Field[] fields = getFields(into);

        for (Field field : fields) {
            if (field.getName().equals(name) && field.getType().isAssignableFrom(value.getClass())) {
                setField(value, into, field);
                return value;
            }
        }
        throw new IllegalArgumentException("Couldn't inject a " + value.getClass().getName() + " into "
            + into.getClass().getName() + " using field " + name);
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Injector into(Object target) {
        this.target = target;
        return this;
    }

    public Object with(Class<? extends Annotation> annotation) throws NoSuchFieldException, IllegalAccessException {
        return inject(value, target, annotation);
    }

    public Object with(String name) throws NoSuchFieldException, IllegalAccessException {
        return inject(value, target, name);
    }
}
