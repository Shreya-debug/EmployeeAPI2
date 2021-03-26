package com.ezest.employee.service;

import java.util.List;

import com.ezest.employee.model.Employee;
import com.ezest.employee.vo.Department;
import com.ezest.employee.vo.ResponseTemplateVO;

public interface EmployeeService {
	
	public String saveEmployee(Employee employee);
	
	public String saveEmployees(List<Employee> employees);
	
	public Employee getEmployeeById(Long id);
	
	public ResponseTemplateVO getEmployeeDetailsById(long id);
	
	public List<Employee> searchEmployeeByFirstName(String firstName);
	
	public List<Employee> searchEmployeeByLastName(String lastName);
	
	public List<Employee> searchEmployeeByFullName(String fullName);
	
	public List<Employee> getAllEmployees();
	
	public List<Employee> getEmployeesBySalaryRange(int minSalary,int maxSalary);
	
	public List<Employee> getEmployeesByDesignation(String designation);
	
	public List<Employee> getAllEmployeesWithJoiningDateBetween(String startdate, String enddate);	
	
	public List<Employee> getAllEmployeesWithJoiningDateAfter(String startdate);
	
	public List<Employee> getAllEmployeesWithJoiningDateBefore(String enddate);
	
	public String deleteEmployeeById(long id);
	
	public String upadateEmployee(Employee employee);
	
	public List<Employee> getEmployeeByDepartmentName(String deptName);
	
	public Department getDepartmentDetailsOfEmployee(long emp_id);
	
	public List<Employee> getEmployeesByDepartment(long dept_id);
}
