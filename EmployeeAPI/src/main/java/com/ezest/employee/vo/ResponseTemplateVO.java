package com.ezest.employee.vo;

import com.ezest.employee.model.Employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseTemplateVO {
	
	private Employee employee;
	
	private Department department;

}
