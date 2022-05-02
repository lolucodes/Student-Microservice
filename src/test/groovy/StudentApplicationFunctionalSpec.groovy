package com.lolucode.student

import geb.spock.GebSpec
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles('test')
class StudentApplicationFunctionalSpec extends GebSpec {

    def setupSpec() {
        System.setProperty('webdriver.gecko.driver', '/usr/local/bin/geckodriver')
    }
    @Sql(['/clear-db.sql', '/insert-student.sql'])
    def 'Getting the student profile' () {

        when: 'I go to the student profile page'
        browser.drive {
            go 'http://localhost:8094/students/1'
        }

        then: 'I land on the right page'
        title == 'Student Profile'

        and: 'It has the correct heading'
        $('h2').text() == 'Student profile'

        and: 'The student details are displayed correctly'
        $('p').size() == 3
        $('p').first().text() == 'First Name: Walter'
        $('p')[1].text() == 'Surname: White'
        $('p').last().text() == 'Student ID: c7453423'
    }
}
