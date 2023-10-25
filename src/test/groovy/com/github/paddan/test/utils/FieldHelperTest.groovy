package com.github.paddan.test.utils

import com.github.paddan.test.injection.test_classes.InjectTarget
import spock.lang.Specification

class FieldHelperTest extends Specification {
    def "Should getFields including super classes fields"() {
        when:
        def fields = FieldHelper.getFields(InjectTarget)

        then:
        fields.size() == 9
    }
}
