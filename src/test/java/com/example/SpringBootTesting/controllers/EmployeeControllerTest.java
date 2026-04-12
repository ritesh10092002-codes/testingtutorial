package com.example.SpringBootTesting.controllers;

import com.example.SpringBootTesting.TestContainerConfiguration;
import com.example.SpringBootTesting.dto.EmployeeDto;
import com.example.SpringBootTesting.entities.Employee;
import com.example.SpringBootTesting.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;



class EmployeeControllerTest extends AbstractintegrationClass {



    @Autowired
    private EmployeeRepository employeeRepository;




    @BeforeEach
    void setup(){
        testEmployee=Employee.builder().id(1L).email("ritesh@gmail.com").name("Ritesh").salary(200L).build();
        testEmployeeDto=EmployeeDto.builder().id(1L).email("ritesh@gmail.com").name("Ritesh").salary(200L).build();
        employeeRepository.deleteAll();
    }


    @Test
    void testGetEmployeeById_success(){
      Employee savedEmployee=  employeeRepository.save(testEmployee);
      webTestClient.get().uri("/employees/{id}",savedEmployee.getId())
              .exchange()
              .expectStatus().isOk()
              .expectBody()
              .jsonPath("$.id").isEqualTo(savedEmployee.getId())
              .jsonPath("$.email").isEqualTo(savedEmployee.getEmail());

//              .value(employeeDto -> {
//                  assertThat(employeeDto.getEmail()).isEqualTo(savedEmployee.getEmail());
//                  assertThat(employeeDto.getId()).isEqualTo(savedEmployee.getId());
//              });

    }

    @Test
    void testGetEmployeeById_failure(){
        webTestClient.get().uri("/employees/1"
        ).exchange().expectStatus().isNotFound();
    }

    @Test
    void testCreateNewEmployee_whenEmployeeAlreadyExist_thenThrowException(){
        Employee savedEmployee=employeeRepository.save(testEmployee);
        webTestClient.post().uri("/employees").bodyValue(testEmployeeDto)
                .exchange().expectStatus().is5xxServerError();


    }

    @Test
    void testCreateNewEmployee_WhenEmployeeDoesNotExist_thenCreateNewEmployee(){
        webTestClient.post().uri("/employees").bodyValue(testEmployeeDto)
                .exchange().expectStatus().isCreated().expectBody().jsonPath("$.email")
                .isEqualTo(testEmployeeDto.getEmail())
                .jsonPath("$.name").isEqualTo(testEmployeeDto.getName());
        ;
    }
    @Test
    void testupdateEmployee_whenEmployeeDoesnotExist_thenthrowException(){
        webTestClient.put().uri("/employees/999")
                .bodyValue(testEmployeeDto).exchange().expectStatus()
                .isNotFound();
    }

    @Test
    void testUpdateEmployee_whenAttemptingToUpdateTheEmail_thenThrowException(){
        Employee savedEmployee=employeeRepository.save(testEmployee);
        testEmployeeDto.setName("Random Name");
        testEmployeeDto.setEmail("random@gmail.com");
        webTestClient.put().uri("/employees/{id}",savedEmployee.getId())
                .bodyValue(testEmployeeDto).exchange().expectStatus().is5xxServerError();

    }

    @Test
    void testUpdateEmployee_whenEmployeeIdValid_thenUpdateEmployee(){
        Employee savedEmployee=employeeRepository.save(testEmployee);
        testEmployeeDto.setName("Random Name");
        testEmployeeDto.setSalary(250l);
        webTestClient.put().uri("/employees/{id}",savedEmployee.getId())
                .bodyValue(testEmployeeDto).exchange().expectStatus().isOk()
                .expectBody(EmployeeDto.class)
                .isEqualTo(testEmployeeDto);


    }
    @Test
    void testDeleteEmployee_whenEmployeeDoestNotExist_thenThrowException(){
        webTestClient.delete().uri("/employees/1").exchange()
                .expectStatus().isNotFound();

    }

    @Test
    void testDeleteEmployee_whenEmployeeExist_thenDeleteEmployee(){
        Employee savedEmployee=employeeRepository.save(testEmployee);

        webTestClient.delete().uri("/employees/{id}",savedEmployee.getId()).exchange().expectStatus().isNoContent().expectBody(Void.class);
        webTestClient.delete().uri("/employees/{id}",savedEmployee.getId()).exchange()
                .expectStatus().isNotFound();

    }
}