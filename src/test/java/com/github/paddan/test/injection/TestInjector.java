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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;

import org.junit.Test;

import com.github.paddan.test.annotations.MyFirstAnnotation;
import com.github.paddan.test.annotations.MySecondAnnotation;

/**
 * @author patrik.lindefors
 */
public class TestInjector {

    @Test
    public void shouldInjectObjectForMyFirstAnnotation() throws Exception {
        InjectTarget dummy = new InjectTarget();
        ClassToInject inject = mock(ClassToInject.class);
        Injector.inject(inject, ClassToInject.class, dummy, MyFirstAnnotation.class);
        assertEquals(inject, dummy.getClassToInject());
    }

    @Test
    public void shouldInjectObjectForAnnotationIntoSuperClass() throws Exception {
        InjectTarget dummy = new InjectTarget();
        ClassToInject inject = mock(ClassToInject.class);
        Injector.inject(inject, ClassToInject.class, dummy, MySecondAnnotation.class);
        assertEquals(inject, dummy.getSuperClassToInject());
    }

    @Test
    public void shouldInjectObjectForName() throws Exception {
        InjectTarget dummy = new InjectTarget();
        ClassToInject inject = mock(ClassToInject.class);
        Injector.inject(inject, ClassToInject.class, dummy, "dummy");
        assertEquals(inject, dummy.getDummy());
    }

    @Test
    public void shouldInjectIntoSuperClass() throws Exception {
        InjectTarget dummy = new InjectTarget();
        ClassToInject inject = mock(ClassToInject.class);
        Injector.inject(inject, ClassToInject.class, dummy, "superDummy");
        assertEquals(inject, dummy.getSuperDummy());
    }

    @Test
    public void shouldGetAllFieldsIncludingSuperClasses() throws Exception {
        InjectTarget dummy = new InjectTarget();
        Field[] fields = getFields(dummy.getClass());
        assertEquals(6, fields.length);
    }

    @Test
    public void shouldInjectIntoStaticField() throws Exception {
        InjectTarget dummy = new InjectTarget();
        Injector.inject("Injected", String.class, dummy, "staticField");

        assertEquals("Injected", InjectTarget.getStaticField());
    }

    @Test
    public void shouldInjectNullIntoStaticField() throws Exception {
        InjectTarget dummy = new InjectTarget();
        Injector.inject(null, String.class, dummy, "staticField");

        assertNull(InjectTarget.getStaticField());
    }

    @SuppressWarnings("static-access")
    @Test
    public void shouldInjectToClass() throws Exception {
        Injector.inject("Injected", String.class, InjectTarget.class, "staticField");

        assertEquals("Injected", InjectTarget.getStaticField());
        assertEquals("Injected", (new InjectTarget()).getStaticField());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenStaticFieldIsMissing() throws Exception {
        Injector.inject("Injected", String.class, InjectTarget.class, "notAField");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenFieldIsMissing() throws Exception {
        Injector.inject("Injected", String.class, new InjectTarget(), "notAField");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenAnnotatedFieldIsMissing() throws Exception {
        Injector.inject("Injected", String.class, InjectTarget.class, Test.class);
    }
}
