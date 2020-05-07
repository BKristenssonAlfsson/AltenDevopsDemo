package se.alten.demo.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import se.alten.demo.DemoApplication;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Testcontainers
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {AbstractTestContainer.Initializer.class})
@SpringBootTest(classes = DemoApplication.class, webEnvironment = RANDOM_PORT)
public abstract class AbstractTestContainer {

    @Autowired
    public MockMvc mockMvc;

    public static PostgreSQLContainer postgreSQLContainer;
    public String userToken = null;
    public String adminToken = null;

    static {
        postgreSQLContainer = new PostgreSQLContainer("postgres:latest")
                .withDatabaseName("book")
                .withUsername("sa")
                .withPassword("sa");
        postgreSQLContainer.start();
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url="+ postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username="+ postgreSQLContainer.getUsername(),
                    "spring.datasource.password="+ postgreSQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
