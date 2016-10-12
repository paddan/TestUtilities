package com.github.paddan.test.injection

import com.github.paddan.test.annotations.MyFirstAnnotation
import spock.lang.Specification

import static com.github.paddan.test.injection.Injector.inject

/**
 * Created by patrik.lindefors on 2016-10-12.
 */
class InjectorTest extends Specification {

    def "Should inject object for MyFirstAnnotation"() {
        setup:
        def target = new InjectDummy()
        def toBeInjected = Mock(ClassToInject)

        when:
        inject(toBeInjected, ClassToInject, target, MyFirstAnnotation)

        then:
        target.classToInject == toBeInjected
    }
}
