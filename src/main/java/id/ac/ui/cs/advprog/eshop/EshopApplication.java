package id.ac.ui.cs.advprog.eshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SuppressWarnings({ "PMD.UseUtilityClass", "PMD.ClassWithOnlyPrivateConstructorsShouldBeFinal" })
public class EshopApplication {
    private EshopApplication() {
    }

    public static void main(String[] args) {
        SpringApplication.run(EshopApplication.class, args);
    }

}
