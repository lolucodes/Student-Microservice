package com.lolucode.student

import geb.spock.GebSpec
import groovyx.net.http.RESTClient
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles('test')
class StudentApiFunctionalSpec extends GebSpec{
    def path = 'http://localhost:8094/api/'
    def client

    def setup() {
        client = new RESTClient(path, MediaType.APPLICATION_JSON)
    }

    @Sql(['/clear-db.sql', '/insert-students.sql'])
    def 'Test GET a student by ID returns the correct student'() {

        when: 'a GET request is sent to get a student by id'
        def response = client.get(path: 'students/1')

        then: 'the correct response is returned'
        with(response) {
            status == 200
            data.id == 1
            data.forename == 'Walter'
            data.surname == 'White'
            data.externalStudentId == 'c7453423'
            data._links.containsKey 'self'
        }
    }

    @Sql(['/clear-db.sql', '/insert-students.sql'])
    def 'Test GET all students returns all the students in the database' () {

        when: 'a GET request is sent to get all students'
        def response = client.get(path: 'students')

        then: 'the correct response is returned'
        with(response) {
            status == 200
            def students = data._embedded.studentList
            students.size == 2
            students.collect { s -> s.forename } == ['Walter', 'Jesse']
            students.collect { s -> s.surname } == ['White', 'Pinkman']
            students.collect { s -> s.externalStudentId } == ['c7453423', 'c3908978']
            students.collect { y -> y._links }.size() == 2
            data._links.containsKey 'self'

        }
    }

    @Sql('/clear-db.sql')
    def 'Test POST a student creates a new student on the database'() {

        given: 'a JSON representation of a student'
        def studentJson = / 
            {
                "forename": "Jeff",
                "surname": "Bezos",
                "externalStudentId": "c3491297"
               
            }/

        when: 'a POST request is sent to the students endpoint'
        def response = client.post(path: 'students', body: studentJson)

        then: 'the student is created and the correct response is returned'
        with(response) {
            status == 201
            data.id > 0
            data.forename == 'Jamie'
            data.surname == 'Smart'
            data.externalStudentId == 'c3491297'
            data._links.containsKey 'self'
            data._links.containsKey 'students'
        }
    }

    @Sql(['/clear-db.sql', '/insert-students.sql'])
    def 'Test PUT a student updates an existing student on the database' () {

        given: 'the desired modification on an existing student'
        def studentJson = /
{
                "forename": "Jamie",
                "surname": "Smart",
                "externalStudentId": "c3491297"
                "CoursesEnrolledIn": "SESC", "ASE"
            }/

        when: 'a PUT request is sent to the API to modify the student with ID = 1'
        def response = client.put(path: 'students/1', body: studentJson)

        then: 'the student is updated and the correct response is returned'
        with(response) {
            status == 200
            data.id == 1
            data.forename == 'Jamie'
            data.surname == 'Smart'
            data.externalStudentId == 'c3491297'
            data.coursesEnrolledIn == ['SESC', 'ASE']
        }
    }
}
