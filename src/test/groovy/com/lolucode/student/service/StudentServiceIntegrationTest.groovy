package com.lolucode.student.service

import com.lolucode.student.model.Student
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.hateoas.EntityModel
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import spock.lang.Specification

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class StudentServiceIntegrationTest extends Specification {

    @Autowired
    private StudentService studentService

    @Sql(['/clear-db.sql', '/insert-students.sql'])
    def 'Testing GetStudentJson() reads a student from the database'() {

        when: 'we read the student from the database'
        def result = studentService.getStudentByIdJson(1L)

        then: 'all the attributes are fetched correctly'

        result.content.id == 1L
        result.content.forename == 'Walter'
        result.content.surname == 'White'
        result.content.externalStudentId == 'c7453423'
    }

    @Sql(['/clear-db.sql', '/insert-students.sql'])
    def 'Testing getAllStudentsJson reads all student from the database'() {

        when: 'we read All students from the database'
        def result = studentService.getAllStudentsJson()

        then: 'all students are fetched'
        result.getContent().size() == 2

        and: 'their attributes are fetched'
        result.content
        .collect(EntityModel::getContent)
        .collect(Student::getExternalStudentId) ==['c7453423', 'c3908978']
    }

    @Sql('/clear-db.sql')
    def 'Testing createNewStudentJson() inserts a new record in the database'() {

        given: 'a new student'
        def student = new Student()
        student.forename = 'Steven'
        student.surname = 'Peters'
        student.externalStudentId = 'c7453433'

        when: 'we insert student in the database'
        def result = studentService.createNewStudentJson(student)

        then: 'the student is persisted'
        def returnedStudent = result.body.content
        returnedStudent.id > 0

        and: 'its attributes are as expected'

        returnedStudent.forename == 'Steven'
        returnedStudent.surname == 'Peters'
        returnedStudent.externalStudentId == 'c7453433'
    }

    @Sql(['/clear-db.sql', '/insert-students.sql'])
    def 'Testing updateStudentJson() modifies a record on the database' () {

        given: 'an existing student from the database'
        def student = studentService.getStudentByIdJson(1).content

        when: 'we modify the student'
        student.forename = 'Chudley'

        and: 'save the changes to the database'
        def result = studentService.updateStudentJson(1, student)

        then: 'the changes are persisted'
        result.body.content.id == 1
        result.body.content.forename == 'Chudley'

    }

}
