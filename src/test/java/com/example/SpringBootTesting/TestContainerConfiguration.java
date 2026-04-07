package com.example.SpringBootTesting;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class TestContainerConfiguration {

    @Bean
    @ServiceConnection
    MySQLContainer<?> mysqlContainer() {
        return new MySQLContainer<>(DockerImageName.parse("mysql:8.0")) // 8.0 is often more stable than 'latest'
                .withDatabaseName("testdb")
                .withUsername("test")
                .withPassword("test")
                .withEnv("MYSQL_ROOT_PASSWORD", "root")
                // Add this specific modifier for Mac Silicon support
                .withCreateContainerCmdModifier(cmd -> cmd.withPlatform("linux/amd64"));
    }

}
