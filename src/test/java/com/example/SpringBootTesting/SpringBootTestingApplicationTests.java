package com.example.SpringBootTesting;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Slf4j
class SpringBootTestingApplicationTests {

	@Test
	void contextLoads() {

	}


//	@BeforeAll
//   void beforeAll(){
//		System.out.println("Started");
//	}

	@BeforeAll
	static void started(){
		log.info("Started....");
	}

	@AfterAll
	static void ended(){
		log.info("Ended all.....");
	}


	@BeforeEach
	void beforeEach(){
		log.info("Started method");
	}


	@AfterEach
	void afterEach(){
		log.info("Ended method");
	}

	@Test
	@DisplayName("Two method")
//	@Disabled
	void displayTwo(){
		log.info("this is 2 method");
	}
	@Test
	@DisplayName("Three method")
	void displaythree(){
		log.info("this is 3 method");

//		int a=5,b=3;
//
//		int result=a+b;
//
//		assertThat(result).isEqualTo(8).isCloseTo(9, Offset.offset(1));
//
//		assertThat("Apple").isEqualTo("Apple").startsWith("App").endsWith("le").hasSize(5);

		int a=5,b=0;

//		double result=divideTwoNumber(a,b);
		assertThatThrownBy(()->divideTwoNumber(a,b)).isInstanceOf(ArithmeticException.class)
				.hasMessage("Divide by zero exception");


	}

	public int sum(int a, int b){
		return a+b;
	}

	double divideTwoNumber(int a,int b){
		try{
			return a/b;
		}catch (ArithmeticException e){
			log.error("Arithmetic expression"+e.getLocalizedMessage());
			throw  new ArithmeticException("Divide by zero exception");
		}

	}


}
