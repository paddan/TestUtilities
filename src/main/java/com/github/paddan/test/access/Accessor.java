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

/**
 * The `Accessor` class provides a convenient way to access private fields of an object using reflection.
 * It supports two modes of operation:
 * 1. Accessing a field by name
 * 2. Accessing a field annotated with a specific annotation and of a specific type
 *
 * The class provides static factory methods to create `Accessor` instances, and an `from()` method to
 * retrieve the value of the specified field from the given object.
 */
public final class Accessor {
    private String namedField;
    private Class<? extends Annotation> annotation;
    private Class<?> type;

    private Accessor() {
    }

    /**
     * Creates a new `Accessor` instance and sets the named field to be accessed.
     *
     * @param namedField the name of the field to be accessed
     * @return a new `Accessor` instance with the named field set
     */
    public static Accessor get(String namedField) {
        Accessor accessor = new Accessor();
        accessor.namedField = namedField;
        return accessor;
    }

    /**
     * Creates a new `Accessor` instance and sets the annotation to be used for field lookup.
     *
     * @param annotation the annotation type to be used for field lookup
     * @return a new `Accessor` instance with the annotation set
     */
    public static Accessor get(Class<? extends Annotation> annotation) {
        Accessor accessor = new Accessor();
        accessor.annotation = annotation;
        return accessor;
    }

    /**
     * Retrieves the value of the specified private field from the given object.
     *
     * @param field the name of the private field to retrieve
     * @param from the object containing the private field
     * @return the value of the private field
     * @throws IllegalAccessException if the private field cannot be accessed
     */
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

    /**
     * Retrieves the value of the first field annotated with the specified annotation and of the given type from the provided object.
     *
     * @param annotation the annotation type to search for on the fields
     * @param type the type of the field to search for
     * @param from the object to retrieve the field value from
     * @return the value of the annotated field
     * @throws IllegalAccessException if the field cannot be accessed
     * @throws IllegalArgumentException if no field matching the criteria is found
     */
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

    /**
     * Retrieves the value of a field from the provided object based on the specified criteria.
     *
     * @param from the object to retrieve the field value from
     * @return the value of the field
     * @throws IllegalAccessException if the field cannot be accessed
     * @throws IllegalArgumentException if no field matching the criteria is found
     */
    public Object from(Object from) throws IllegalAccessException {
        if (namedField != null) {
            return get(namedField, from);
        } else if (annotation != null && type != null) {
            return get(annotation, type, from);
        } else {
            throw new IllegalArgumentException("Couldn't find field! Missing information!");
        }
    }

    /**
     * Sets the type of the field to be retrieved.
     *
     * @param type the type of the field to be retrieved
     * @return the current Accessor instance for method chaining
     */
    public Accessor ofType(Class<?> type) {
        this.type = type;
        return this;
    }
}
