package com.github.paddan.test.injection

import com.github.paddan.test.annotations.MyFirstAnnotation
import spock.lang.Specification

import static com.github.paddan.test.injection.Injector.inject

class InjectorTest extends Specification {
    InjectTarget target
    ClassToInject classToInject

    void setup() {
        target = new InjectTarget()
        classToInject = Mock(ClassToInject)
    }

    def "Should inject mock for field annotated with MyFirstAnnotation"() {
        when:
        inject(classToInject, ClassToInject, target, MyFirstAnnotation)

        then:
        !target.dummy
        target.classToInject == classToInject
    }

    def "Should inject mock into field named dummy"() {
        when:
        inject(classToInject, ClassToInject, target, "dummy")

        then:
        !target.classToInject
        target.dummy == classToInject
    }
}
