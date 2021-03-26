package com.ezest.employee.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ezest.employee.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{
	
	
	List<Employee> findByFirstName(String firstName);
	
	List<Employee> findByLastName(String lastName);
	
	List<Employee> findByDesignation(String designation);
	
	@Query("from Employee where deptId = ?1")
	List<Employee> findByDepartment(long deptId);
	
	@Query("from Employee where firstName =?1 AND lastName=?2")
	List<Employee> findByFullName(String firstName, String lastName);
	
	@Query("from Employee where lower(firstName) like :firstName%")
	List<Employee> findByFirstNameLikeCaseIgnore(String firstName);
	
	@Query("from Employee where lower(lastName) like :lastName%")
	List<Employee> findByLastNameLikeCaseIgnore(String lastName);
	
	@Query("from Employee where salary>=?1 AND salary<?2")
	List<Employee> findBySalaryRange(int minSalary, int maxSalary);
	
	@Query("from Employee where joiningDate<?1 ")
	List<Employee> findAllByWithJoiningDateBefore(Date enddate);
	
	@Query("from Employee where joiningDate>=?1 ")
	List<Employee> findAllByWithJoiningDateAfter(Date startdate);
	
	@Query("from Employee where joiningDate>=?1 AND joiningDate<=?2")
	List<Employee> findAllByJoiningDateBetween(Date StartDate, Date EndDate);


}