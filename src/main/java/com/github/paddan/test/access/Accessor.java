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

package com.github.paddan.test.access;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static com.github.paddan.test.utils.FieldHelper.getFields;

public final class Accessor {
    private String namedField;
    private Class<? extends Annotation> annotation;
    private Class<?> type;

    private Accessor() {
    }

    public static Accessor get(String namedField) {
        Accessor accessor = new Accessor();
        accessor.setNamedField(namedField);
        return accessor;
    }

    public static Accessor get(Class<? extends Annotation> annotation) {
        Accessor accessor = new Accessor();
        accessor.setAnnotation(annotation);
        return accessor;
    }

    public static Object get(String field, Object from) throws IllegalAccessException {
        Field privateField = null;
        Class<?> type = from.getClass();

        while (privateField == null) {
            try {
                privateField = type.getDeclaredField(field);
            } catch (NoSuchFieldException e) {
                type = type.getSuperclass();
                if (type == Object.class) {
                    throw new IllegalArgumentException("Couldn't find field " + field, e);
                }
            }
        }

        privateField.setAccessible(true);

        return privateField.get(from);
    }

    public static Object get(Class<? extends Annotation> annotation, Class<?> type, Object from) throws
            IllegalAccessException {

        Field[] fields = getFields(from.getClass());
        for (Field field : fields) {
            if (field.getAnnotation(annotation) != null && field.getType().equals(type)) {
                field.setAccessible(true);

                return field.get(from);
            }
        }

        throw new IllegalArgumentException("Couldn't find field annotated with " + annotation);
    }

    private void setNamedField(String namedField) {
        this.namedField = namedField;
    }

    public Object from(Object from) throws IllegalAccessException {
        if (namedField != null) {
            return get(namedField, from);
        } else if (annotation != null && type != null) {
            return get(annotation, type, from);
        } else {
            throw new IllegalArgumentException("Couldn't find field! Missing information!");
        }
    }

    private void setAnnotation(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    public Accessor ofType(Class<?> type) {
        this.type = type;
        return this;
    }
}
