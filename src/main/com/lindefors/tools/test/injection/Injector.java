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

package com.lindefors.tools.test.injection;

import static com.lindefors.tools.test.utils.FieldHelper.getFields;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 
 * @author patrik.lindefors
 * 
 */
public final class Injector {

    public static <T> void inject(T value, Class<? extends T> valueClass, Class<?> into, String name) throws IllegalAccessException {
        setField(value, valueClass, into, name, getFields(into));
    }

    public static <T> void inject(T value, Class<? extends T> valueClass, Object into, Class<? extends Annotation> withAnnotationClass)
            throws IllegalAccessException {
        Field[] fields = getFields(into.getClass());
        
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (field.getAnnotation(withAnnotationClass) != null && field.getType().equals(valueClass)) {
                field.setAccessible(true);
                field.set(into, value);
                return;
            }
        }
        
        throw new IllegalArgumentException("Couldn't inject a " + value.getClass().getName() + " into " + into.getClass().getName() + " using annotation "
                + withAnnotationClass.getName());
    }

    public static <T> void inject(T value, Class<? extends T> valueClass, Object into, String name) throws IllegalAccessException {
        setField(value, valueClass, into, name, getFields(into.getClass()));
    }

    private static <T> void setField(T value, Class<? extends T> valueClass, Object into, String name, Field[] fields) throws IllegalAccessException {
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (field.getName().equals(name) && field.getType().equals(valueClass)) {
                field.setAccessible(true);
                field.set(into, value);
                return;
            }
        }

        throw new IllegalArgumentException("Couldn't inject a " + value.getClass().getName() + " into " + into.getClass().getName() + " using field " + name);
    }

    private Injector() {
    }

}
