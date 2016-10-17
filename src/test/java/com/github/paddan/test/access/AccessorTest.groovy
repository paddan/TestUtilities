package com.github.paddan.test.access

import com.github.paddan.test.annotations.MyFirstAnnotation
import spock.lang.Specification

import static com.github.paddan.test.access.Accessor.get

/**
 * Created by patrik.lindefors on 2016-10-17.
 */
class AccessorTest extends Specification {
    def "Should retrieve a private field"() {
        setup:
        PrivateClass privateClass = new PrivateClass()

        when:
        def field = get("privateField", privateClass)

        then:
        field == "this is private"
    }

    def "Should retrieve a private field using builder"() {
        setup:
        PrivateClass privateClass = new PrivateClass()

        when:
        def field = get("privateField").from(privateClass)

        then:
        field == "this is private"
    }

    def "Should retrieve a private field using annotation"() {
        setup:
        PrivateClass privateClass = new PrivateClass()

        when:
        def field = get(MyFirstAnnotation, String.class, privateClass)

        then:
        field == "This is annotated!"
    }

    def "Should retrieve a private field using builder and annotation"() {
        setup:
        PrivateClass privateClass = new PrivateClass()

        when:
        def field = get(MyFirstAnnotation).ofType(String).from(privateClass)

        then:
        field == "This is annotated!"
    }
}
