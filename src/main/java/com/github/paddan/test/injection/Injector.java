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
import java.util.Arrays;

/**
 * @author patrik.lindefors
 */
public final class Injector {

  private Injector() {
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

  /**
   * Injects a value into a named field.
   *
   * @param value      The value to inject
   * @param valueClass The class of the value to inject
   * @param into       The object into which the value is injected
   * @param name       The name of the field
   * @return The value injected
   * @throws IllegalAccessException If the field cannot be accessed
   * @throws NoSuchFieldException   If the field doesn't exist with the specified type and annotation
   */
  public static <T> T inject(T value, Class<? extends T> valueClass, Object into, String name)
      throws IllegalAccessException, NoSuchFieldException {
    Field[] fields = getFields(into.getClass());

    for (Field field : fields) {
      if (field.getName().equals(name) && (field.getType().equals(valueClass) || field.getType().isPrimitive())) {
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
   * @param valueClass The class of the value to inject
   * @param into       The class into which the value is injected
   * @param name       The name of the field
   * @return The value injected
   * @throws IllegalAccessException If the field cannot be accessed
   * @throws NoSuchFieldException   If the field doesn't exist with the specified type and annotation
   */
  public static <T> T inject(T value, Class<? extends T> valueClass, Class<?> into, String name)
      throws IllegalAccessException, NoSuchFieldException {
    Field[] fields = getFields(into);

    for (Field field : fields) {
      if (matches(valueClass, name, field)) {
        setField(value, into, field);
        return value;
      }
    }
    throw new IllegalArgumentException("Couldn't inject a " + value.getClass().getName() + " into "
        + into.getClass().getName() + " using field " + name);
  }

  private static <T> boolean matches(Class<? extends T> valueClass, String name, Field field) {
    return field.getName().equals(name) && field.getType().equals(valueClass);
  }

  /**
   * Injects a value into a named field of a specific super class.
   *
   * @param value      The value to inject
   * @param valueClass The class of the value to inject
   * @param into       The class into which the value is injected
   * @param name       The name of the field
   * @param declared   The super class where the field is declared
   * @return The value injected
   * @throws IllegalAccessException If the field cannot be accessed
   * @throws NoSuchFieldException   If the field doesn't exist with the specified type and annotation
   */
  public static <T> T inject(T value, Class<? extends T> valueClass, Object into, String name, Class<?> declared)
      throws IllegalAccessException, NoSuchFieldException {
    Field[] fields = getFields(into.getClass());

    for (Field field : fields) {
      if (field.getName().equals(name) && field.getType().equals(valueClass)
          && field.getDeclaringClass().equals(declared)) {
        setField(value, into, field);
        return value;
      }
    }
    throw new IllegalArgumentException("Couldn't inject a " + value.getClass().getName() + " into "
        + into.getClass().getName() + " using field " + name + " declared in " + declared.getName());
  }
}
