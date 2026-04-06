package com.example.SpringBootTesting.repositories;

import com.example.SpringBootTesting.entities.Employee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;



@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    Employee employee;

    @BeforeEach
    void setUp(){
        employee=Employee.builder()
                .email("ritesh10@gmail.com")
                .name("Ritesh")
                .salary(100L)
                .build();


    }

    @Test
    void testFindByEmail_whenEmailIsPresent_returnEmployee(){
        //Arrange , Act , Assert --follow this 3 steps always

        employeeRepository.save(employee);

        List<Employee> employeeList=employeeRepository.findByEmail(employee.getEmail());
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).isNotEmpty();
        assertThat(employeeList.get(0).getEmail()).isEqualTo(employee.getEmail());


    }

    @Test
    void testFindByEmail_whenEmailIsNotPresent_returnEmployee(){
        String email="Ritesh1@gmail.com";


        List<Employee> employeeList=employeeRepository.findByEmail(email);

        assertThat(employeeList).isNotNull();
        assertThat(employeeList).isEmpty();


    }

}