package com.ezest.employee.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ezest.employee.exceptionHandler.CustomException;
import com.ezest.employee.exceptionHandler.ResourceNotFoundException;
import com.ezest.employee.model.Employee;
import com.ezest.employee.repository.EmployeeRepository;
import com.ezest.employee.validator.DateValidatorUsingDateFormat;
import com.ezest.employee.vo.Department;
import com.ezest.employee.vo.ResponseTemplateVO;

@Service
public class EmployeeServiceImpl implements EmployeeService{

	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	private DateValidatorUsingDateFormat dateValidator = new DateValidatorUsingDateFormat("dd-MM-yyyy");

	//Get department id from employee id
	public long getDepartmentId(long id) {
		Optional<Employee> existingEmployee = employeeRepository.findById(id);
        
        if(existingEmployee.isPresent()) {
            Employee e = employeeRepository.findById(id).orElse(null);       
    		return e.getDeptId();
        } else {
            throw new ResourceNotFoundException("No Employee record exist for id",id);
        }
	}
	
	//Add single employee
	public String saveEmployee(Employee employee){
       // employeeRepository.save(employee);
        //return "Employee details saved successfully. ";	
        
        long dept_id = employee.getDeptId();
        Boolean value ;
		String url = "http://DEPARTMENT-SERVICE/Department/checkIfValidDepartment/"+dept_id;
		ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
		if(response.getStatusCode() == HttpStatus.OK) {
			value = response.getBody();
			if(value) {
				employeeRepository.save(employee);
		        return "Employee details saved successfully. ";
			}else {
				throw new ResourceNotFoundException("No Department exists for given DepartmentId - ",dept_id);
			}
		}else {
			throw new ResourceNotFoundException("No Department exists for given DepartmentId - ",dept_id);
		}
	}

	//Add multiple employees
	public String saveEmployees(List<Employee> employees){
		try {
			for(Employee emp : employees) {
				saveEmployee(emp);
			}
			return "Employee details saved successfully. ";
		}catch(Exception e) {
			throw new CustomException(e.getMessage());
		}     	
	}

	
	//Get employee by Id
	public Employee getEmployeeById(Long id) {
		
		Employee emp =employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee Id doesn't exist.",id));
		
		return emp;
    }
	
	
	//Get Employees by Department Name
	public List<Employee> getEmployeeByDepartmentName(String deptName){
		try 
		{
			String url = "http://DEPARTMENT-SERVICE/Department/getDepartmentByName/"+deptName;
			ResponseEntity<Department> response = restTemplate.getForEntity(url, Department.class);
			if(response.getStatusCode() == HttpStatus.OK) {
				Department dept = response.getBody();
				long deptId = dept.getId();
				List<Employee> emps = getEmployeesByDepartment(deptId);
				if(!(emps.isEmpty())) {
					return emps;
				}else {
					throw new ResourceNotFoundException("No employee exists in department ");
				}
				//return emps;
			}else {
				throw new ResourceNotFoundException(" No department exists with name - "+deptName);
			}
		}catch(Exception e) {
			throw new ResourceNotFoundException(e.getMessage());
		}
		
	}
	
	//Get employee and it's department details by id
	public ResponseTemplateVO getEmployeeDetailsById(long id) {
	
		ResponseTemplateVO vo = new ResponseTemplateVO();
		Employee emp = getEmployeeById(id);
		Department dept = getDepartmentDetailsOfEmployee(id);
		 
		vo.setEmployee(emp);
		vo.setDepartment(dept);
		return vo;
	}
	
	//Get employees by first name
	public List<Employee> searchEmployeeByFirstName(String firstName) {
		firstName= firstName.trim();
		if(firstName.isBlank() || firstName.isEmpty()) {
			throw new CustomException("Please Enter first name.");
		}
		if(!(employeeRepository.findByFirstNameLikeCaseIgnore(firstName)).isEmpty()) {			
			return (List<Employee>)employeeRepository.findByFirstNameLikeCaseIgnore(firstName);
		}else {
			throw new ResourceNotFoundException("No employee exists with first name as - "+firstName);
		}
			
				
	}
	
	//Get employees by last name
	public List<Employee> searchEmployeeByLastName(String lastName) {
		lastName = lastName.trim(); 
		if(lastName.isBlank() || lastName.isEmpty()) {
			throw new CustomException("Please Enter first name.");
		}else {	
			if(!(employeeRepository.findByLastName(lastName)).isEmpty()) {
				return (List<Employee>)employeeRepository.findByLastName(lastName);
			}else {
				throw new ResourceNotFoundException("No employee exists with last name as - "+lastName);
			}
		}
	}
	
	
		
	//Get employee by full name
	public List<Employee> searchEmployeeByFullName(String fullName) {
		
		fullName = fullName.trim();
		if(fullName.isBlank() || fullName.isEmpty()) {
			throw new ResourceNotFoundException("Please Enter in correct format as (Firstname Lastname).",null);
			//throw new ResourceNotFoundException("Please Enter in correct format as (Firstname Lastname).", null);
		}else {
			String[] names = fullName.split(" ");
			if(names.length>1) {
				String firstName = names[0];
				String lastName = names[1];
		
				if(!(employeeRepository.findByFullName(firstName, lastName)).isEmpty()) {
					return employeeRepository.findByFullName(firstName, lastName);
				}else {
					throw new ResourceNotFoundException("No employee exists with name -"+ fullName);
			}
			}else {
				throw new ResourceNotFoundException("Please enter last name as well.");
			}
		}	
	}
		
	
	//Get all employees
	public List<Employee> getAllEmployees() {
        return (List<Employee>)employeeRepository.findAll();
    }
	
	//Get all employees in given salary range
	public List<Employee> getEmployeesBySalaryRange(int minSalary,int maxSalary){
		if(minSalary< maxSalary) {
			if(!(employeeRepository.findBySalaryRange(minSalary, maxSalary)).isEmpty()) {
			
				return employeeRepository.findBySalaryRange(minSalary, maxSalary);
			}else {
				throw new ResourceNotFoundException("No employee exists within given salary range(minSalary < maxSalary).");
			}
		}else {
			throw new ResourceNotFoundException("Enter valid salary range.");
		}
	}
	
	//Get all employees of given designation
	public List<Employee> getEmployeesByDesignation(String designation){
		
		if(!(employeeRepository.findByDesignation(designation)).isEmpty()) {
			
			return employeeRepository.findByDesignation(designation);
		}else {
			throw new ResourceNotFoundException("No employee exists.(Make sure you have selected correct designation.)");
		}
			
	}

	//Get Employees By Department
	public List<Employee> getEmployeesByDepartment(long dept_id){
		if(!(employeeRepository.findByDepartment(dept_id)).isEmpty()) {
			
			return employeeRepository.findByDepartment(dept_id);
		}else {
			throw new ResourceNotFoundException("No employee exists in department ",dept_id);
		}
	}
		
	//Delete employee by id	
	public String deleteEmployeeById(long id) {
		Employee emp =employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee Id doesn't exist",id));
		
		employeeRepository.deleteById(id);
		return "Employee details deleted successfully.";
	}
	
	
	//Update Employee
	public String upadateEmployee(Employee employee) {
        Optional<Employee> existingEmployee = employeeRepository.findById(employee.getId());
         
        if(existingEmployee.isPresent()) {
            Employee e = employeeRepository.findById(employee.getId()).orElse(null);
            e.setFirstName(employee.getFirstName());
            e.setLastName(employee.getLastName());
            e.setUserName(employee.getUserName());
    		e.setPassword(employee.getPassword());
    		e.setEmail(employee.getEmail());
    		//e.setDepartmentId(employee.getDepartmentId());
            long dept_id = employee.getDeptId();
            Boolean value ;
    		String url = "http://DEPARTMENT-SERVICE/Department/checkIfValidDepartment/"+dept_id;
    		ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
    		if(response.getStatusCode() == HttpStatus.OK) {
    			value = response.getBody();
    			if(value) {
    				e.setDeptId(dept_id);
    				employeeRepository.save(employee);
    				return "Employee Details updated successfully.";
    			}else {
    				throw new ResourceNotFoundException("No Department exists for given DepartmentId - ",dept_id);
    			}
    		}else {
    			throw new ResourceNotFoundException("No Department exists for given DepartmentId - ",dept_id);
    		}  		
    		
        } else {
            throw new ResourceNotFoundException("No Employee record exist for given id",employee.getId());
        }
    }
	
	// Check if input string is valid date or not.
	public Date parseDate(String startdate) {
		
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date;         
        try {
            date = dateFormat.parse(startdate);
            System.out.println(date);           
            return date;
        } catch (ParseException e) {
        	//System.out.println(e);
            e.printStackTrace();
            throw new ResourceNotFoundException("Enter valid date string");
        }
		
	}
	
	//Get all employees having joining date after
	public List<Employee> getAllEmployeesWithJoiningDateAfter(String startdate) {
		
		if(dateValidator.isValid(startdate)) {
			//System.out.println("yes go ahead");
			Date parsedDate = parseDate(startdate);
			if(!(employeeRepository.findAllByWithJoiningDateAfter(parsedDate)).isEmpty()) {
				return employeeRepository.findAllByWithJoiningDateAfter(parsedDate);
			}else {
				throw new ResourceNotFoundException("No employee exists having joining date after "+startdate);
			}
		}else {
			//System.out.println("No stop");
			throw new ResourceNotFoundException("Enter valid date.");
		}
		
	}
		
	//Get all employees having joining date Before	
	public List<Employee> getAllEmployeesWithJoiningDateBefore(String enddate) {
		
		if(dateValidator.isValid(enddate)) {
			//System.out.println("yes go ahead");
			Date parsedDate = parseDate(enddate);
			if(!(employeeRepository.findAllByWithJoiningDateBefore(parsedDate)).isEmpty()) {
				return employeeRepository.findAllByWithJoiningDateBefore(parsedDate);
			}else {
				throw new ResourceNotFoundException("No employee exists having joining date before "+enddate);
			}
		}else {
			//System.out.println("No stop");
			throw new ResourceNotFoundException("Enter valid date.");
		}
		
	}
	
	//Get all employees having joining date Between
	public List<Employee> getAllEmployeesWithJoiningDateBetween(String startdate, String enddate){
		
		if(dateValidator.isValid(startdate) & dateValidator.isValid(enddate)) {
			Date parsedStartDate = parseDate(startdate);
			Date parsedEndDate = parseDate(enddate);
			if(parsedEndDate.after(parsedStartDate))
			{
			
				if(!(employeeRepository.findAllByJoiningDateBetween(parsedStartDate, parsedEndDate)).isEmpty()) {
					return employeeRepository.findAllByJoiningDateBetween(parsedStartDate, parsedEndDate);
				}else {
					throw new ResourceNotFoundException("No employee exists having joining date between "+startdate+" and "+enddate);
				}
			}else {
				throw new ResourceNotFoundException("Enter valid range of dates.(startdate < enddate)");
			}
		}else {
			if(dateValidator.isValid(startdate))
				throw new ResourceNotFoundException("Enter valid end date.");
			else if(dateValidator.isValid(enddate))
				throw new ResourceNotFoundException("Enter valid end date.");
			else
				throw new ResourceNotFoundException("Enter valid dates.");
		}
		
		
	}

	public Department getDepartmentDetailsOfEmployee(long emp_id) {
		
		long dept_id = getDepartmentId(emp_id);
		String url = "http://DEPARTMENT-SERVICE/Department/getDepartmentById/" + dept_id;
		ResponseEntity<Department> response = restTemplate.getForEntity(url, Department.class);
		if(response.getStatusCode() == HttpStatus.OK) {
			//return response;
			Department dept = response.getBody();
			return dept;
		}else {
			throw new ResourceNotFoundException("Employee Id doesn't exist.",emp_id);
		}
	}
	
	
}

