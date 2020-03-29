/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.edu.nju.studentdaterear;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin
class StudentController {

	private final StudentRepository repository;

	StudentController(StudentRepository repository) {
		this.repository = repository;
	}

	@GetMapping("/students")
	ResponseEntity<CollectionModel<EntityModel<Student>>> findAll() {

		List<EntityModel<Student>> Students = StreamSupport.stream(repository.findAll().spliterator(), false)
				.map(Student -> new EntityModel<>(Student, //
						linkTo(methodOn(StudentController.class).findOne(Student.getId())).withSelfRel(), //
						linkTo(methodOn(StudentController.class).findAll()).withRel("Students"))) //
				.collect(Collectors.toList());

		return ResponseEntity.ok( //
				new CollectionModel<>(Students, //
						linkTo(methodOn(StudentController.class).findAll()).withSelfRel()));
	}

	@PostMapping("/students")
	ResponseEntity<?> newStudent(@RequestBody Student Student) {

		try {

			Student savedStudent = repository.save(Student);

			EntityModel<Student> StudentResource = new EntityModel<>(savedStudent, //
					linkTo(methodOn(StudentController.class).findOne(savedStudent.getId())).withSelfRel());

			return ResponseEntity //
					.created(new URI(StudentResource.getRequiredLink(IanaLinkRelations.SELF).getHref())) //
					.body(StudentResource);
		} catch (URISyntaxException e) {
			return ResponseEntity.badRequest().body("Unable to create " + Student);
		}
	}

	/**
	 * Look up a single {@link Employee} and transform it into a REST resource. Then
	 * return it through Spring Web's {@link ResponseEntity} fluent API.
	 *
	 * @param id
	 */
	@GetMapping("/students/{id}")
	ResponseEntity<EntityModel<Student>> findOne(@PathVariable long id) {
		
		return repository.findById(id) //
				.map(Student -> new EntityModel<>(Student, //
						linkTo(methodOn(StudentController.class).findOne(Student.getId())).withSelfRel(), //
						linkTo(methodOn(StudentController.class).findAll()).withRel("Students"))) //
				.map(ResponseEntity::ok) //
				.orElse(ResponseEntity.notFound().build());
	}

	/**
	 * Update existing employee then return a Location header.
	 * 
	 * @param employee
	 * @param id
	 * @return
	 */
	@PutMapping("/students/{id}")
	ResponseEntity<?> updateStudent(@RequestBody Student Student, @PathVariable long id) {

		Student StudentToUpdate = Student;
		StudentToUpdate.setId(id);
		repository.save(StudentToUpdate);

		Link newlyCreatedLink = linkTo(methodOn(StudentController.class).findOne(id)).withSelfRel();

		try {
			return ResponseEntity.noContent().location(new URI(newlyCreatedLink.getHref())).build();
		} catch (URISyntaxException e) {
			return ResponseEntity.badRequest().body("Unable to update " + StudentToUpdate);
		}
	}

	@DeleteMapping("/students/{id}")
	ResponseEntity<?> deleteStudent(@PathVariable Long id) {
		try {
			if (id == null || repository.findById(id) == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}

			repository.deleteById(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

		} catch (Exception e) {
			e.printStackTrace();

		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
}
