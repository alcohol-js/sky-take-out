package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     * @param emp
     */
    void addEmp(EmployeeDTO emp);

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    PageResult pageLimit(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用/停用员工账号
     * @param status
     * @param id
     */
    void enableAndDisable(Integer status, Long id);

    /**
     * 根据id查询员工数据
     * @param id
     * @return
     */
    Employee getEmp(Long id);

    /**
     * 修改员工信息
     * @param employeeDTO
     */
    void editEmp(EmployeeDTO employeeDTO);
}
