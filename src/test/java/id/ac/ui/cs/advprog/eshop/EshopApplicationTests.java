package id.ac.ui.cs.advprog.eshop;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mockStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EshopApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testMainMethod() {
        try (var mockSpringApplication = mockStatic(SpringApplication.class)) {
            String[] args = { "test", "args" };

            assertDoesNotThrow(() -> EshopApplication.main(args));

            mockSpringApplication.verify(() -> SpringApplication.run(EshopApplication.class, args));
        }
    }

    @Test
    void testMainMethodWithEmptyArgs() {
        try (var mockSpringApplication = mockStatic(SpringApplication.class)) {
            String[] args = {};

            assertDoesNotThrow(() -> EshopApplication.main(args));

            mockSpringApplication.verify(() -> SpringApplication.run(EshopApplication.class, args));
        }
    }

}
