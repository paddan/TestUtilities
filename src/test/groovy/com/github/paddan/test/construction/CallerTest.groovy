package com.github.paddan.test.construction

import spock.lang.Specification

import static com.github.paddan.test.construction.Caller.callMethod
import static com.github.paddan.test.construction.Caller.callStatic
import static com.github.paddan.test.construction.Caller.construct

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

    def "Should create object using primitive constructor"() {
        when:
        def object = construct(PrimitiveClass, 5)

        then:
        object instanceof PrimitiveClass
        object.count == 5
    }

    def "Should call private method with primitive argument"() {
        setup:
        def object = construct(PrimitiveClass, 3)

        when:
        def result = callMethod(object, "echoInt", 7)

        then:
        result == 7
    }

    def "Should call private static method with primitive argument"() {
        when:
        def result = callStatic(PrimitiveClass, "echoStaticInt", 9)

        then:
        result == 9
    }

    def "Should construct object with null argument"() {
        when:
        def object = construct(NullableClass, (Object) null)

        then:
        object instanceof NullableClass
        object.value == null
    }

    def "Should call private method with null argument"() {
        setup:
        def object = construct(NullableClass, (Object) null)

        when:
        def result = callMethod(object, "echo", (Object) null)

        then:
        result == null
    }
}
