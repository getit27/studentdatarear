package cn.edu.nju.studentdaterear;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
class DatabaseLoader {

	/**
	 * Use Spring to inject a {@link EmployeeRepository} that can then load data.
	 * Since this will run only after the app is operational, the database will be
	 * up.
	 *
	 * @param repository
	 */
	@Bean
	CommandLineRunner init(StudentRepository repository) {

		return args -> {
			repository.save(new Student((long)1,"Mike", "male", "20000101","USA","computer","00001"));
			repository.save(new Student((long)2,"Amy", "female", "19991231","UK","bio","00002"));
		};
	}

}
