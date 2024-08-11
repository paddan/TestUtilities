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

package com.github.paddan.test.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Provides utility methods for working with Java reflection and class fields.
 *
 * The {@link #getFields(Class)} method retrieves all fields, including those from
 * superclasses, for the given class.
 */
public final class FieldHelper {

    private FieldHelper() {
    }

    /**
     * Retrieves all fields, including those from superclasses, for the given class.
     *
     * @param thisClass the class to get the fields for
     * @return an array of all fields, including those from superclasses, for the given class
     */
    public static Field[] getFields(Class<?> thisClass) {
        Field[] fields = thisClass.getDeclaredFields();
        Class<?> superClass = thisClass.getSuperclass();

        if (superClass != Object.class) {
            List<Field> superClassFields = new LinkedList<>(Arrays.asList(getFields(superClass)));
            superClassFields.addAll(Arrays.asList(fields));
            return superClassFields.toArray(new Field[0]);
        }

        return fields;
    }
}
