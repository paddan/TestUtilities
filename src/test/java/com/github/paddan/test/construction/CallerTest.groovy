package com.github.paddan.test.construction

import spock.lang.Specification

import static com.github.paddan.test.construction.Caller.callMethod
import static com.github.paddan.test.construction.Caller.callStatic
import static com.github.paddan.test.construction.Caller.construct

/**
 * Created by patrik.lindefors on 2016-10-17.
 */
class CallerTest extends Specification {

    def "Should create object from no-args constructor"() {
        when:
        def object = construct(PrivateClass)

        then:
        object instanceof PrivateClass
    }

    def "Should create object from constructor with arguments"() {
        when:
        def object = construct(PrivateClass, "flirp", 10L)

        then:
        object instanceof PrivateClass
        object.number == 10L
        object.string == "flirp"
    }

    def "Should call private method"() {
        setup:
        def object = construct(PrivateClass, "flirp", 10L)

        when:
        def string = callMethod(object, "getPrivateString")

        then:
        string == "flirp"
    }

    def "Should call private method with arguments"() {
        setup:
        def object = construct(PrivateClass)

        when:
        def string = callMethod(object, "returnStringFromPrivate", "Blaj")

        then:
        string == "Blaj"
    }

    def "Should call private static method"() {
        when:
        def string = callStatic(PrivateClass, "returnStringFromStatic")

        then:
        string == "hej"
    }

    def "Should call private static method with arguments"() {
        when:
        def string = callStatic(PrivateClass, "returnStringFromStatic", "Blaj")

        then:
        string == "Blaj"
    }
}
