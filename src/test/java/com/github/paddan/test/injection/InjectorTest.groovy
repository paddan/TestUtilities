package com.github.paddan.test.injection

import com.github.paddan.test.annotations.MyFirstAnnotation
import com.github.paddan.test.injection.test_classes.ClassToInject
import com.github.paddan.test.injection.test_classes.InjectTarget
import org.mockito.Mockito
import spock.lang.Specification

import static com.github.paddan.test.injection.Injector.autoInject
import static com.github.paddan.test.injection.Injector.inject

class InjectorTest extends Specification {
    InjectTarget target
    ClassToInject classToInject

    void setup() {
        target = new InjectTarget()
        classToInject = Mock(ClassToInject)
    }

    def "should inject with annotated builder"() {
        when:
        inject(classToInject).into(target).with(MyFirstAnnotation)

        then:
        !target.namedField
        target.annotatedField == classToInject
    }

    def "should inject with named builder"() {
        when:
        inject(classToInject).into(target).with("namedField")

        then:
        target.namedField == classToInject
        !target.annotatedField
    }

    def "Should inject a mock object for field annotated with MyFirstAnnotation"() {
        when:
        inject(classToInject, target, MyFirstAnnotation)

        then:
        !target.namedField
        target.annotatedField == classToInject
    }

    def "Should inject null for named field"() {
        when:
        inject(null, String.class, target, "privateField")

        then:
        !target.privateField
    }

    def "Should inject null for annotated field"() {
        when:
        inject(null, String, target, MyFirstAnnotation)

        then:
        !target.privateField
    }

    def "Should not inject a mock object for field annotated with MyFirstAnnotation"() {
        when: "injecting an object of the wrong type"
        inject(1000, target, MyFirstAnnotation)

        then: "An exception should be thrown"
        thrown IllegalArgumentException
    }

    def "Should inject a mock object into field named namedField"() {
        when:
        inject(classToInject, ClassToInject, target, "namedField")

        then:
        !target.annotatedField
        target.namedField == classToInject
    }

    def "Should inject a string object into the final field named finalField"() {
        when:
        inject("Hello!", String, target, "finalField")

        then:
        !target.annotatedField
        !target.namedField
        target.finalField == "Hello!"
    }

    def "Should inject an int into the field named intField"() {
        when:
        inject(10, target, "intField")

        then:
        target.intField == 10
    }

    def "Should mock all fields with mockito"() {
        when:
        def mocks = autoInject(Mockito.&mock, target)

        then:
        mocks.size() == 5
        target.superSuperDummy == mocks."superSuperDummy"
        target.namedField == mocks."namedField"
        target.superAnnotatedField == mocks."superAnnotatedField"
        target.annotatedField == mocks."annotatedField"
        target.superNamedField == mocks."superNamedField"
        ClassToInject.isAssignableFrom(mocks."superSuperDummy".class)
        ClassToInject.isAssignableFrom(mocks."namedField".class)
        ClassToInject.isAssignableFrom(mocks."superAnnotatedField".class)
        ClassToInject.isAssignableFrom(mocks."annotatedField".class)
        ClassToInject.isAssignableFrom(mocks."superNamedField".class)
    }

    def "Should mock all fields with spock"() {
        when:
        def mocks = autoInject({Mock(it)}, target)

        then:
        mocks.size() == 5
        target.superSuperDummy == mocks."superSuperDummy"
        target.namedField == mocks."namedField"
        target.superAnnotatedField == mocks."superAnnotatedField"
        target.annotatedField == mocks."annotatedField"
        target.superNamedField == mocks."superNamedField"
        ClassToInject.isAssignableFrom(mocks."superSuperDummy".class)
        ClassToInject.isAssignableFrom(mocks."namedField".class)
        ClassToInject.isAssignableFrom(mocks."superAnnotatedField".class)
        ClassToInject.isAssignableFrom(mocks."annotatedField".class)
        ClassToInject.isAssignableFrom(mocks."superNamedField".class)
    }
}
