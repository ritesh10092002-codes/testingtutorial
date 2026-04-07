package com.example.SpringBootTesting.repositories;

import com.example.SpringBootTesting.TestContainerConfiguration;
import com.example.SpringBootTesting.entities.Employee;
import com.example.SpringBootTesting.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Import(TestContainerConfiguration.class)
@DataJpaTest
//@ActiveProfiles("test")
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    Employee employee;

    @BeforeEach
    void setUp(){
        employee = Employee.builder()
                .id(1L)
                .email("ritesh10@gmail.com")
                .name("Ritesh")
                .salary(100L)
                .build();
    }

    @Test
    void testFindByEmail_whenEmailIsValid_thenReturnEmployee(){

        employeeRepository.save(employee);

        List<Employee> employeeList = employeeRepository.findByEmail(employee.getEmail());

        assertThat(employeeList).isNotNull();
        assertThat(employeeList).isNotEmpty();
        assertThat(employeeList.get(0).getEmail()).isEqualTo(employee.getEmail());
    }

    @Test
    void testFindByEmail_whenEmailIsNotFound_thenReturnEmployee(){

        String email = "Ritesh1@gmail.com";

        List<Employee> employeeList = employeeRepository.findByEmail(email);

        assertThat(employeeList).isNotNull();
        assertThat(employeeList).isEmpty();

    }
}