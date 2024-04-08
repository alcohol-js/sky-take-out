package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        //进行md5加密，然后再进行比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    @Override
    public void addEmp(EmployeeDTO emp) {
        Employee employee = new Employee();

        //属性拷贝
        BeanUtils.copyProperties(emp, employee);

        //填充其他信息
        employee.setStatus(StatusConstant.ENABLE);//用户状态
        employee.setCreateTime(LocalDateTime.now());//创建时间
        employee.setUpdateTime(LocalDateTime.now());//更新时间
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));//默认密码
        //创建人和更新人
        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());

        //调用数据库操作
        employeeMapper.insertEmp(employee);
    }

    @Override
    public PageResult pageLimit(EmployeePageQueryDTO employeePageQueryDTO) {
        //开始分页
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());

        //调用数据库操作
        Page<Employee> page = employeeMapper.pageLimit(employeePageQueryDTO);

        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void enableAndDisable(Integer status, Long id) {
        //创建员工对象，填入信息
        Employee employee = new Employee();
        employee.setId(id);
        employee.setStatus(status);

        //调用数据库操作
        employeeMapper.editEmp(employee);

    }

    @Override
    public Employee getEmp(Long id) {

        //调用数据库操作
        Employee employee = employeeMapper.getEmp(id);

        return employee;
    }

    @Override
    public void editEmp(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();

        //属性拷贝
        BeanUtils.copyProperties(employeeDTO, employee);

        //填充其他信息
        employee.setUpdateTime(LocalDateTime.now());//更新时间
        employee.setUpdateUser(BaseContext.getCurrentId());//更新人

        //调用数据库操作
        employeeMapper.editEmp(employee);
    }


}
