package com.example.SpringBootTesting.services.impl;

import com.example.SpringBootTesting.TestContainerConfiguration;
import com.example.SpringBootTesting.dto.EmployeeDto;
import com.example.SpringBootTesting.entities.Employee;
import com.example.SpringBootTesting.repositories.EmployeeRepository;
import com.example.SpringBootTesting.services.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.ui.Model;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@Import(TestContainerConfiguration.class)
//@ExtendWith(MockitoExtension.class)--springBootTest Handle this
class EmployeeServiceImplTest {

    private Employee mockEmployee;
    private EmployeeDto mockEmployeeDto;

    @BeforeEach
     void beforeEach(){
        mockEmployee=Employee.builder().id(1L).email("ritesh@gmail.com").name("Ritesh").salary(200L).build();
mockEmployeeDto=modelMapper.map(mockEmployee,EmployeeDto.class);
    }

    //All mock placed under inject mocks
    @Mock
    private EmployeeRepository employeeRepository;

    @Spy //creating real model mapper
    private ModelMapper modelMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void getEmployeeById_whenEmployeeIdPresent_returnEmployeeDto() {

        //assign

        Long id=1L;

        when(employeeRepository.findById(id)).thenReturn(Optional.of(mockEmployee));//stubbing

        //act

        EmployeeDto employeeDto=employeeService.getEmployeeById(id);

        //assert

        assertThat(employeeDto.getId()).isEqualTo(id);
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmployee.getEmail());
        verify(employeeRepository).findById(id); //will pass  as it check  does this method is called by repo class

    }

    @Test
    void testCreateNewEmployee_whenValidEmployee_thenCreateNewEmployee(){

        //assign
       when(employeeRepository.findByEmail(anyString())).thenReturn(List.of());
       when(employeeRepository.save(any(Employee.class))).thenReturn(mockEmployee);

        //act
        EmployeeDto employeeDto=employeeService.createNewEmployee(mockEmployeeDto);

        //assert
        ArgumentCaptor<Employee> employeeArgumentCaptor=ArgumentCaptor.forClass(Employee.class);

        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmployee.getEmail());
//        verify(employeeRepository).save(any(Employee.class));
        verify(employeeRepository).save(employeeArgumentCaptor.capture());

        Employee capturedEmployee=employeeArgumentCaptor.getValue();
        assertThat(capturedEmployee.getEmail()).isEqualTo(mockEmployee.getEmail());
//        assertThat(employeeList.get(0)).isEmpty();


    }
//    @Test
//    void createNewEmployee() {
//    }
//
//    @Test
//    void updateEmployee() {
//    }
//
//    @Test
//    void deleteEmployee() {
//    }
}