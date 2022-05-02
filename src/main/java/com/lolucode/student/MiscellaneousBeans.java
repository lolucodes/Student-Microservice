package com.lolucode.student;

import com.lolucode.student.model.Course;
import com.lolucode.student.model.Student;
import com.lolucode.student.repository.CourseRepository;
import com.lolucode.student.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@Configuration
public class MiscellaneousBeans {

    @Bean
    CommandLineRunner initDatabase(StudentRepository studentRepository, CourseRepository courseRepository) {
        return args -> {
            Course sesc = new Course();
            sesc.setTitle("SESC");
            sesc.setDescription("Software Engineering for Service Computing");
            sesc.setFee(10.00);

            Course rema = new Course();
            rema.setTitle("REMA");
            rema.setDescription("Reverse Engineering and Malware Analysis");
            rema.setFee(15.00);

            Course ase = new Course();
            ase.setTitle("ASE");
            ase.setDescription("Advanced Software Engineering");
            ase.setFee(20.00);

            Student lolu = new Student();
            lolu.setForename("lolu");
            lolu.setSurname("Ade");
            lolu.setExternalStudentId("c9999999");
            lolu.enrollInCourse(sesc);

            Student Sarat = new Student();
            Sarat.setForename("sarat");
            Sarat.setSurname("Sadiq");
            Sarat.setExternalStudentId("c2222222");
            Sarat.setCoursesEnrolledIn(Set.of(sesc, ase));

            studentRepository.saveAllAndFlush(Set.of(lolu, Sarat));
//            Account account = integrationService.getStudentAccount("c3781247");
//            Student tom = new Student();
//            tom.setForename("Tom");
//            tom.setSurname("Shaw");
//            tom.setExternalStudentId("c3781247");
//            studentRepository.save(tom);
//            enrolmentService.enrolStudentInCourse(tom, ase);
        };
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {

        return builder.build();
    }

//    @Bean(initMethod = "start", destroyMethod = "stop")
//    public Server h2Server() throws SQLException {
//        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
//    }
}