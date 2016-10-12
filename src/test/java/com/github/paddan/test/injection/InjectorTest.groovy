package com.github.paddan.test.injection

import com.github.paddan.test.annotations.MyFirstAnnotation
import com.github.paddan.test.injection.test_classes.ClassToInject
import com.github.paddan.test.injection.test_classes.InjectTarget
import spock.lang.Ignore
import spock.lang.Specification

import static com.github.paddan.test.injection.Injector.inject

class InjectorTest extends Specification {
    InjectTarget target
    ClassToInject classToInject

    void setup() {
        target = new InjectTarget()
        classToInject = Mock(ClassToInject)
    }

    def "Should inject a mock object for field annotated with MyFirstAnnotation"() {
        when:
        inject(classToInject, ClassToInject, target, MyFirstAnnotation)

        then:
        !target.namedField
        target.annotatedField == classToInject
    }

    def "Should inject a mock object into field named namedField"() {
        when:
        inject(classToInject, ClassToInject, target, "namedField")

        then:
        !target.annotatedField
        target.namedField == classToInject
    }

    @Ignore
    def "Should inject a string object into the final field named finalField"() {
        when:
        inject("Hello!", String, target, "finalField")

        then:
        !target.annotatedField
        !target.namedField
        target.finalField == "Hello!"
    }
}
