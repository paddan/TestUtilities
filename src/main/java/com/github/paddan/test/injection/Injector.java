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

    private Injector() {
    }

    public static <T> T inject(T value, Class<? extends T> valueClass, Object into,
                               Class<? extends Annotation> withAnnotationClass) throws IllegalAccessException, NoSuchFieldException {
        Field[] fields = getFields(into.getClass());
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (field.getAnnotation(withAnnotationClass) != null && field.getType().equals(valueClass)) {
                setField(value, into, field);
                return value;
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

    public static <T> T inject(T value, Class<? extends T> valueClass, Object into, String name)
        throws IllegalAccessException, NoSuchFieldException {
        Field[] fields = getFields(into.getClass());

        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getName().equals(name)) {
                if (fields[i].getType().equals(valueClass)) {
                    setField(value, into, fields[i]);
                    return value;
                }
                else if (fields[i].getType().isPrimitive()) {
                    setField(value, into, fields[i]);
                    return value;
                }
            }
        }
        throw new IllegalArgumentException("Couldn't inject a " + value.getClass().getName() + " into "
            + into.getClass().getName() + " using field " + name);
    }

    public static <T> T inject(T value, Class<? extends T> valueClass, Class<?> into, String name)
        throws IllegalAccessException, NoSuchFieldException {
        Field[] fields = getFields(into);

        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getName().equals(name) && fields[i].getType().equals(valueClass)) {
                setField(value, into, fields[i]);
                return value;
            }
        }
        throw new IllegalArgumentException("Couldn't inject a " + value.getClass().getName() + " into "
            + into.getClass().getName() + " using field " + name);
    }

    public static <T> T inject(T value, Class<? extends T> valueClass, Object into, String name, Class<?> declared)
        throws IllegalAccessException, NoSuchFieldException {
        Field[] fields = getFields(into.getClass());

        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getName().equals(name) && fields[i].getType().equals(valueClass)
                && fields[i].getDeclaringClass().equals(declared)) {
                setField(value, into, fields[i]);
                return value;
            }
        }
        throw new IllegalArgumentException("Couldn't inject a " + value.getClass().getName() + " into "
            + into.getClass().getName() + " using field " + name + " declared in " + declared.getName());
    }

}
