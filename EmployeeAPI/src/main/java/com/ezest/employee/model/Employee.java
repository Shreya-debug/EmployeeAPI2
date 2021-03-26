package com.ezest.employee.model;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name="Employee_Table")
public class Employee {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Emp_Id")
	private long id;
	

	@Column(name="Emp_FirstName")
	@NotBlank
	@NotEmpty(message = "Employee First Name may not be empty")
	@NotNull(message = "Employee First Name may not be null")
	private String firstName;
	
	@Column(name="Emp_LastName")
	@NotBlank
	@NotEmpty(message = "Employee Last Name may not be empty")
	@NotNull(message = "Employee Last Name may not be null")
	private String lastName;
/*	
	@Column(name="Emp_Department")
	@NotBlank
	@NotEmpty(message = "Employee department may not be empty")
	private String department;
*/	
	@Column(name="Emp_DepartmentId")
	@Min(1)
	private long deptId;
	
	@Column(name="Emp_Designation")
	@NotBlank
	@NotEmpty(message = "Employee designation may not be empty")
	private String designation;
	
	@Column(name="Emp_UserName")
	@NotBlank
	@NotEmpty(message = "Employee userame may not be empty")
	@NotNull(message = "Employee userame may not be null")
	private String userName;
	
	
	@Column(name="Emp_Password")
	@NotEmpty(message = "Employee  password may not be empty")
	@NotNull(message = "Employee password may not be null")
	private String password;
	
	@Email
	@Column(name="Emp_Email")
	@NotEmpty(message = "Employee email may not be empty")
	@NotBlank
	private String email;
	
	@Column(name="Emp_Salary")
	private int salary;

	@Column(name="Emp_JoiningDate")
	@JsonFormat(pattern = "dd-MM-yyyy")
	@NotNull(message = "Please provide a date.")
	private Date joiningDate;
	
	
	@Override
	public String toString() {
		return "Employee [id=" + id + ", FirstName=" + firstName + ",  LastName=" + lastName+ ", email=" + email + ",Designation= "+ designation + ", Salary=" + salary +" Joining Date= "+ joiningDate+" ]";
	}

}
