package com.lolucode.student.service;

import com.lolucode.student.model.*;
import com.lolucode.student.repository.StudentRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class EnrolmentService {
    private final StudentRepository studentRepository;
    private final IntegrationService integrationService;

    public EnrolmentService(StudentRepository studentRepository,
                            IntegrationService integrationService) {
        this.studentRepository = studentRepository;
        this.integrationService = integrationService;
    }

    public void enrollStudentInCourse(Student student, Course course){
        student.enrollInCourse(course);
        boolean courseExist = studentRepository.findById(course.getId())
                .isPresent();
        if (courseExist){
            throw new IllegalStateException("You have been Enrolled in this Course");
        }
        studentRepository.save(student);
        integrationService.createCourseFeeInvoice(createInvoice(student, course));
    }

    private Invoice createInvoice(Student student, Course course) {
        Account account = new Account();
        account.setStudentId(student.getExternalStudentId());
        Invoice invoice = new Invoice();
        invoice.setAccount(account);
        invoice.setType(Invoice.Type.TUITION_FEES);
        invoice.setAmount(course.getFee());
        invoice.setDueDate(LocalDate.now().plusMonths(1));
        return invoice;
    }
}
