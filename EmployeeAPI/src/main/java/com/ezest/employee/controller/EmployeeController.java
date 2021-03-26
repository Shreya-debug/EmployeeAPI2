package com.ezest.employee.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ezest.employee.exceptionHandler.CustomException;
import com.ezest.employee.exceptionHandler.ResourceNotFoundException;
import com.ezest.employee.model.Employee;
import com.ezest.employee.service.EmployeeService;
import com.ezest.employee.vo.Department;
import com.ezest.employee.vo.ResponseTemplateVO;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@Validated
@RequestMapping("/Employee")
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
			
	
	@PostMapping("/addEmployee")
	public  ResponseEntity<String> addEmployee(@Valid @RequestBody Employee employee) {
		
		log.info("Request to add new employee");
		try {
			String message = employeeService.saveEmployee(employee);
			return new ResponseEntity<String>(message, HttpStatus.OK);
		}catch(Exception e) {
			log.error("error occured in addEmployee() "+e.getMessage());
			throw new CustomException(e.getMessage());
			//return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/addEmployees")
	public ResponseEntity<String> addEmployees(@Valid @RequestBody List<Employee> employees) {
		
		log.info("Request to add new "+employees.size()+" employees");
		try {
			String message = employeeService.saveEmployees(employees);
			return new ResponseEntity<String>(message, HttpStatus.OK);
		}catch(Exception e) {
			log.error("error occured in addEmployees() "+e.getMessage());
			throw new CustomException(e.getMessage());
			//return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}


	}

	@GetMapping("/getAllEmployees")
	public ResponseEntity<List<Employee>> findAllEmployees() {
		
		log.info("Request to get employee details");
		try {
			List<Employee> emps = employeeService.getAllEmployees();
			return new ResponseEntity<List<Employee>>(emps, HttpStatus.OK);
		}catch(Exception e) {
			log.error("error occured in findAllEmployees() "+e.getMessage());
			throw new CustomException(e.getMessage());
			//return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}		
		
    }
	
	
	@GetMapping("/getEmployeeDetailsById/{empId}")
	public ResponseEntity<ResponseTemplateVO> getEmployeeById(@PathVariable @Min(1) long empId) {
		
		log.info("Request to get employee details by id - "+empId);
		try {
			ResponseTemplateVO emp = employeeService.getEmployeeDetailsById(empId);
			return new ResponseEntity<ResponseTemplateVO>(emp, HttpStatus.OK);
		}catch(Exception e) {
			log.error("error occured in getEmployeeById() "+e.getMessage());
			throw new CustomException(e.getMessage());
		}
	      
    }
	
	@GetMapping("/getEmployeesByDepartmentName/{deptName}")
	public ResponseEntity<List<Employee>> findAllEmployeesByDepartmentName(@PathVariable @NotBlank String deptName){
		log.info("Request to get employees by department name - "+deptName);
		try {
			List<Employee> emps = employeeService.getEmployeeByDepartmentName(deptName);
			if(!(emps.isEmpty())) {
				return new ResponseEntity<List<Employee>>(emps, HttpStatus.OK);
			}else {
				throw new ResourceNotFoundException("No employee exists in department ");
			}
		}catch(Exception e) {
			log.error("error occured in findAllEmployeesByDepartmentName() "+e.getMessage());
			throw new CustomException(e.getMessage());
		}
	}
	
	@GetMapping("/getAllEmployeesByJoiningDateBefore/{enddate}")
	public ResponseEntity<List<Employee>> findAllEmployeesByJoiningDateBefore(@PathVariable  @NotBlank String enddate) {
		
		log.info("Request to get employees having joining date before - "+enddate);
		try {
			List<Employee> emps = employeeService.getAllEmployeesWithJoiningDateBefore(enddate);
			return new ResponseEntity<List<Employee>>(emps, HttpStatus.OK);
		}catch(Exception e) {
			log.error("error occured in findAllEmployeesByJoiningDateBetween() "+e.getMessage());
			throw new CustomException(e.getMessage());
		}
		
    }
	

	@GetMapping("/getAllEmployeesByJoiningDateAfter/{startdate}")
	public ResponseEntity<List<Employee>> findAllEmployeesByJoiningDateAfter(@PathVariable @NotBlank String startdate) throws Exception {
		
		log.info("Request to get employees having joining date after - "+startdate);
		try {
			List<Employee> emps = employeeService.getAllEmployeesWithJoiningDateAfter(startdate);
			return new ResponseEntity<List<Employee>>(emps, HttpStatus.OK);
		}catch(Exception e) {
			log.error("error occured in findAllEmployeesByJoiningDateAfter() "+e.getMessage());
			throw new CustomException(e.getMessage());
		}
		//return new ResponseEntity<List<Employee>>(employeeService.getAllEmployeesWithJoiningDateAfter(startdate), new HttpHeaders(), HttpStatus.OK);

    }
	
	
	@GetMapping("/getAllEmployeesByJoiningDateBetween/{startdate}/{enddate}")
	public ResponseEntity<List<Employee>> findAllEmployeesByJoiningDateBetween(@PathVariable  @NotBlank String startdate, @PathVariable @NotBlank String enddate) {
		
		log.info("Request to get employees having joining date between "+startdate+" and "+enddate);
		try {
			List<Employee> emps = employeeService.getAllEmployeesWithJoiningDateBetween(startdate, enddate);
			return new ResponseEntity<List<Employee>>(emps, HttpStatus.OK);
		}catch(Exception e) {
			log.error("error occured in findAllEmployeesByJoiningDateBetween() "+e.getMessage());
			throw new CustomException(e.getMessage());
		}
		//return new ResponseEntity<List<Employee>>(employeeService.getAllEmployeesWithJoiningDateBetween(startdate, enddate), new HttpHeaders(), HttpStatus.OK);

    }

		
	@PutMapping("/updateEmployee")
    public ResponseEntity<String> updateEmployee(@Valid @RequestBody Employee employee) {
		
		log.info("Request to update employee.");
		try {
			String message = employeeService.upadateEmployee(employee);
			return new ResponseEntity<String>(message, HttpStatus.OK);
		}catch(Exception e) {
			log.error("error occured in updateEmployee() "+e.getMessage());
			throw new CustomException(e.getMessage());
			//return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
    }
	
	@DeleteMapping("/deleteEmployeeById/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable @NotNull   long id) {
		
		log.info("Request to delete employee id - "+id);
		try {
			String message = employeeService.deleteEmployeeById(id);
			return new ResponseEntity<String>(message, HttpStatus.OK);
		}catch(Exception e) {
			log.error("error occured in deleteEmployee() "+e.getMessage());
			throw new CustomException(e.getMessage());
			//return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
    }
	
	@GetMapping("/getEmployeeByFirstName/{firstName}")
	public ResponseEntity<List<Employee>> getEmployeeByFirstName(@PathVariable @NotBlank String firstName) {
		
		log.info("Request to get employees by first name - "+firstName);
		try {
			List<Employee> emps = employeeService.searchEmployeeByFirstName(firstName);
			return new ResponseEntity<List<Employee>>(emps, HttpStatus.OK);
		}catch(Exception e) {
			log.error("error occured in getEmployeeByFirstName() "+e.getMessage());
			throw new CustomException(e.getMessage());
		}
		//return new ResponseEntity<List<Employee>>(employeeService.searchEmployeeByFirstName(firstName), new HttpHeaders(), HttpStatus.OK);

	}
	
	@GetMapping("/getEmployeeByLastName/{lastName}")
	public ResponseEntity<List<Employee>> getEmployeeByLastName(@PathVariable @NotBlank String lastName) {
		
		log.info("Request to get employees by last name - "+ lastName);
		try {
			List<Employee> emps = employeeService.searchEmployeeByLastName(lastName);
			return new ResponseEntity<List<Employee>>(emps, HttpStatus.OK);
		}catch(Exception e) {
			log.error("error occured in getEmployeeByLastName() "+e.getMessage());
			throw new CustomException(e.getMessage());		
		}
		//return new ResponseEntity<List<Employee>>(employeeService.searchEmployeeByLastName(lastName), new HttpHeaders(), HttpStatus.OK);

	}
	
	@GetMapping("/getEmployeeByFullName/{fullName}")
	public ResponseEntity<List<Employee>> getEmployeeByFullName(@PathVariable @NotBlank String fullName) {
		
		log.info("Request to get employees by full name - "+fullName);
		try {
			List<Employee> emps = employeeService.searchEmployeeByFullName(fullName);
			return new ResponseEntity<List<Employee>>(emps, HttpStatus.OK);
		}catch(Exception e) {
			log.error("error occured in getEmployeeByFullName() "+e.getMessage());
			throw new CustomException(e.getMessage());
		}
		//return new ResponseEntity<List<Employee>>(employeeService.searchEmployeeByFullName(fullName), new HttpHeaders(), HttpStatus.OK);

	}
	
	@GetMapping("/getAllEmployeesWithinSalary/{minSalary}/{maxSalary}")
	public ResponseEntity<List<Employee>> getEmployeesInSalaryRange(@PathVariable @Positive @Min(1) int minSalary, @PathVariable @Positive @Min(1) int maxSalary ){
		
		log.info("Request to get employees having salary between "+minSalary+" and "+maxSalary);
		try {
			return new ResponseEntity<List<Employee>>(employeeService.getEmployeesBySalaryRange(minSalary,maxSalary), HttpStatus.OK);
		}catch(Exception e) {
			log.error("error occured in getEmployeesInSalaryRange() "+e.getMessage());
			throw new CustomException(e.getMessage());		
		}
		

	}

	@GetMapping("/getDepartmentDetailsByEmployeeId/{emp_id}")
	public ResponseEntity<Department> getDepartmentDetailsofEmployee(@PathVariable @Min(1) long emp_id){
		
		log.info("Request to get Department details of employee "+ emp_id);
		try {
			Department dept =employeeService.getDepartmentDetailsOfEmployee(emp_id);
			return new ResponseEntity<Department>(dept,HttpStatus.OK);
		}catch(Exception e) {
			log.error("error occured in getDepartmentDetailsofEmployee() "+e.getMessage());
			throw new CustomException(e.getMessage());
			//return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/getEmployeesByDepartment/{dept_id}")
	public ResponseEntity<List<Employee>> getEmployeesByDepartment(@PathVariable @Min(1) long dept_id){
		log.info("Request to get employees of department - "+ dept_id);
		try {
			List<Employee> emps = employeeService.getEmployeesByDepartment(dept_id);
			return new ResponseEntity<List<Employee>>(emps,HttpStatus.OK);
		}catch(Exception e) {
			log.error("error occured in getEmployeesByDepartment() "+e.getMessage());
			throw new CustomException(e.getMessage());
			//return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@GetMapping("/getEmployeesByDesignation/{designation}")
	public ResponseEntity<List<Employee>> getEmployeesByDesignation(@PathVariable @NotBlank String designation ){
		
		log.info("Request to get employees by designation - "+designation);
		try{
			List<Employee> emp = employeeService.getAllEmployees();
			List<Employee> emps = emp.stream().filter(e -> e.getDesignation().equalsIgnoreCase(designation)).collect(Collectors.toList());	
			return new ResponseEntity<List<Employee>>(emps, HttpStatus.OK);
		}catch(Exception e) {
			log.error("error occured in getEmployeesByDesignation() "+e.getMessage());
			throw new CustomException(e.getMessage());
			//return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}
		
	

}





