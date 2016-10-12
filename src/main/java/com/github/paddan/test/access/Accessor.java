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

import static com.github.paddan.test.utils.FieldHelper.getFields;

import java.lang.reflect.Field;

public final class Accessor {

    @SuppressWarnings("unchecked")
    public static <T> T access(String name, Class<? extends T> valueClass, Object from) throws IllegalAccessException {
        Field[] fields = getFields(from.getClass());
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (name.equals(field.getName()) && field.getType().equals(valueClass)) {
                field.setAccessible(true);
                return (T) field.get(from);
            }
        }

        throw new IllegalArgumentException("Couldn't access " + name + " from " + from.getClass().getName());
    }

    private Accessor() {
    }
}
