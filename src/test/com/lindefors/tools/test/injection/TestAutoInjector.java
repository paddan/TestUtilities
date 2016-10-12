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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.junit.Test;

import com.lindefors.tools.test.injection.AutoInjector;
import com.lindefors.tools.test.injection.AutoInjector.Mocks;

public class TestAutoInjector {

    @Test
    public void shouldInjectMocksAutomatically() throws Exception {
        AutoInjectorTarget target = new AutoInjectorTarget();
        Mocks mocks = AutoInjector.autoMock(target, InjectAnnotation.class, AnotherInjectAnnotation.class);

        assertEquals(mocks.get(A.class), target.a());
        assertEquals(mocks.get(B.class), target.getB());
    }


    @Test
    public void shouldInjectIntoParent() throws Exception {
        AutoInjectorChildTarget target = new AutoInjectorChildTarget();
        Mocks mocks = AutoInjector.autoMock(target, InjectAnnotation.class, AnotherInjectAnnotation.class);

        assertEquals(mocks.get(A.class), target.a());
        assertEquals(mocks.get(B.class), target.getB());
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForRequestForUnknownInterface() throws Exception {
        AutoInjectorTarget target = new AutoInjectorTarget();
        Mocks mocks = AutoInjector.autoMock(target, InjectAnnotation.class, AnotherInjectAnnotation.class);
        mocks.get(C.class);
    }


    @Test
    public void shouldHandleDuplicateFields() throws Exception {
        AutoInjectorTargetWithDuplicateFields target = new AutoInjectorTargetWithDuplicateFields();
        Mocks mocks = AutoInjector.autoMock(target, InjectAnnotation.class, AnotherInjectAnnotation.class);
        A mockedA = mocks.get(A.class);
        assertEquals(mockedA, target.a1);
        assertEquals(mockedA, target.a2);
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForAnnotationThatIsntAnnotation() throws Exception {
        AutoInjectorTarget target = new AutoInjectorTarget();
        AutoInjector.autoMock(target, Object.class);
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForMissingAnnotations() throws Exception {
        AutoInjectorTarget target = new AutoInjectorTarget();
        AutoInjector.autoMock(target);
    }


    @Test
    public void shouldNotInjectIntoFieldWithoutCorrectAnnotation() throws Exception {
        AutoInjectorTargetWithDifferentAnnotations target = new AutoInjectorTargetWithDifferentAnnotations();
        AutoInjector.autoMock(target, InjectAnnotation.class);
        assertNotNull(target.a);
        assertNull(target.b);
    }


    @Test
    public void shouldInjectNamedFields() throws Exception {
        AutoInjectorTargetWithNamedField target = new AutoInjectorTargetWithNamedField();
        Mocks mocks = AutoInjector.autoMock(target, "namedField");
        assertEquals(mocks.get("namedField"), target.namedField());
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForUnsupportedInjectionTargetType() throws Exception {
        AutoInjectorTargetWithNamedField target = new AutoInjectorTargetWithNamedField();
        AutoInjector.autoMock(target, 42);
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForGettingOfUnknownNamedField() throws Exception {
        AutoInjectorTargetWithNamedField target = new AutoInjectorTargetWithNamedField();
        Mocks mocks = AutoInjector.autoMock(target, "namedField");
        mocks.get("unkownNamedField");
    }


    @Test
    public void shouldBeAbleToGetMocksByNameEvenIfItWasInjectedByAnnotation() throws Exception {
        AutoInjectorTarget target = new AutoInjectorTarget();
        Mocks mocks = AutoInjector.autoMock(target, InjectAnnotation.class);
        assertEquals(mocks.get("a"), target.a());
    }


    @Test
    public void shouldBeAbleToGetMocksByTypeEvenIfItWasInjectedByName() throws Exception {
        AutoInjectorTargetWithNamedField target = new AutoInjectorTargetWithNamedField();
        Mocks mocks = AutoInjector.autoMock(target, "namedField");
        assertEquals(mocks.get(A.class), target.namedField());
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForNamedFieldThatDoesNotExist() throws Exception {
        AutoInjectorTargetWithNamedField target = new AutoInjectorTargetWithNamedField();
        AutoInjector.autoMock(target, "unknownNamedField");
    }

    @Retention(RetentionPolicy.RUNTIME)
    public static @interface InjectAnnotation {
    }

    @Retention(RetentionPolicy.RUNTIME)
    public static @interface AnotherInjectAnnotation {
    }

    public static interface A {
    }

    public static interface B {
    }

    public static interface C {
    }

    public static class AutoInjectorTarget {
        @InjectAnnotation
        private A a;

        private B b;


        public A a() {
            return a;
        }


        public B getB() {
            return b;
        }


        @InjectAnnotation
        public void setB(B b) {
            this.b = b;
        }
    }

    public static class AutoInjectorChildTarget extends AutoInjectorTarget {
        @InjectAnnotation
        private A a;


        @Override
        public A a() {
            return a;
        }
    }

    public static class AutoInjectorTargetWithDuplicateFields {
        @InjectAnnotation
        public A a1;
        @InjectAnnotation
        public A a2;
    }

    public static class AutoInjectorTargetWithDifferentAnnotations {
        @InjectAnnotation
        public A a;
        @AnotherInjectAnnotation
        public B b;
    }

    public static class AutoInjectorTargetWithNamedField {
        private A namedField;


        public A namedField() {
            return namedField;
        }
    }
}
