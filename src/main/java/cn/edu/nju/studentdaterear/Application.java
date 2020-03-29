
package cn.edu.nju.studentdaterear;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.server.core.EvoInflectorLinkRelationProvider;

@SpringBootApplication
public class Application {

	public static void main(String... args) {
		SpringApplication app = new SpringApplication(Application.class);
		app.setDefaultProperties(Collections.singletonMap("server.port", "8083"));
		app.run(args);
	}

	/**
	 * Format embedded collections by pluralizing the resource's type.
	 * 
	 * @return
	 */
	@Bean
	EvoInflectorLinkRelationProvider relProvider() {
		return new EvoInflectorLinkRelationProvider();
	}
}
