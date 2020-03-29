package cn.edu.nju.studentdaterear;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
class Student{

	@Id
	@GeneratedValue
	private long id;
	private String name;
	private String sex;
	private String bd;
	private String np;
	private String department;
	private String sn;

}
