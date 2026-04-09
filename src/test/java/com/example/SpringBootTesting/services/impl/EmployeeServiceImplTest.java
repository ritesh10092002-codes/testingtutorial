package com.example.SpringBootTesting.services.impl;

import com.example.SpringBootTesting.TestContainerConfiguration;
import com.example.SpringBootTesting.dto.EmployeeDto;
import com.example.SpringBootTesting.entities.Employee;
import com.example.SpringBootTesting.exceptions.ResourceNotFoundException;
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

import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@Import(TestContainerConfiguration.class)
//@ExtendWith(MockitoExtension.class)--springBootTest Handle this
class EmployeeServiceImplTest {
    //All mock placed under inject mocks
    @Mock
    private EmployeeRepository employeeRepository;

    @Spy //creating real model mapper
    private ModelMapper modelMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee mockEmployee;
    private EmployeeDto mockEmployeeDto;

    @BeforeEach
     void beforeEach(){
        mockEmployee=Employee.builder().id(1L).email("ritesh@gmail.com").name("Ritesh").salary(200L).build();
mockEmployeeDto=modelMapper.map(mockEmployee,EmployeeDto.class);
    }



    @Test
    void testGetEmpployeeById_whenEmployeeIsNotPresent_thenthrowException(){
        //arrange
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());
       //act and assert

        assertThatThrownBy(()->employeeService.getEmployeeById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 1");

        verify(employeeRepository).findById(1L);
    }

    @Test
    void testCreateNewEmployee_whenAttemptingToCreateEmployeeWithExistingEmail_thenThrowException() {
//        arrange
        when(employeeRepository.findByEmail(mockEmployeeDto.getEmail())).thenReturn(List.of(mockEmployee));
//        act and assert

        assertThatThrownBy(() -> employeeService.createNewEmployee(mockEmployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Employee already exists with email: "+mockEmployee.getEmail());

        verify(employeeRepository).findByEmail(mockEmployeeDto.getEmail());
        verify(employeeRepository, never()).save(any());
    }

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

    void testUpdateEmployee_whenEmployeeDoesNotExists_thenThrowException() {
//        arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

//        act and assert
        assertThatThrownBy(() -> employeeService.updateEmployee(1L, mockEmployeeDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 1");

        verify(employeeRepository).findById(1L);
        verify(employeeRepository, never()).save(any());
    }
    @Test
    void testUpdateEmployee_whenAttemptingToUpdateEmail_ThenThrowException(){
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(mockEmployee));

        mockEmployeeDto.setName("Random");
        mockEmployeeDto.setEmail("random@gmail.com");

        //act and assert

        assertThatThrownBy(()->employeeService.updateEmployee(mockEmployeeDto.getId(), mockEmployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("The email of the employee cannot be updated");

        verify(employeeRepository).findById(mockEmployeeDto.getId());
        verify(employeeRepository,never()).save(any());



    }

    @Test
    void testUpdateEmployee_whenValidEmployee_thenUpdateEmployee(){
        //arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(mockEmployee));

        mockEmployeeDto.setName("Random");
        mockEmployeeDto.setSalary(199L);

        Employee newEmployee=modelMapper.map(mockEmployeeDto,Employee.class);
        when(employeeRepository.save(any(Employee.class))).thenReturn(newEmployee);

        //act
        EmployeeDto updateEmployeeDto=employeeService.updateEmployee(mockEmployeeDto.getId(),mockEmployeeDto);

        assertThat(updateEmployeeDto).isEqualTo(mockEmployeeDto);
        verify(employeeRepository).findById(1L);
        verify(employeeRepository).save(any());




    }

    @Test
    void testDeleteEmployee_whenEmployeeDoesNotExist_thenThrowException(){
        when(employeeRepository.existsById(1L)).thenReturn(false);

        //act
        assertThatThrownBy(()->employeeService.deleteEmployee(1L)).isInstanceOf(
                ResourceNotFoundException.class
        ).hasMessage("Employee not found with id: 1");

        verify(employeeRepository,never()).deleteById(anyLong());
    }

    @Test
    void testDeleteEmployee_whenEmployeeIsValid_thenDeleteEmployee(){
        when(employeeRepository.existsById(1L)).thenReturn(true);

        assertThatCode(()->employeeService.deleteEmployee(1L)).doesNotThrowAnyException();

        verify(employeeRepository).deleteById(1L);
    }
}