package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    @Select("select username from employee where username = #{username}")
    String searchEmployee(String username);

    @Insert("insert into employee(name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user) " +
            "values " +
            "(#{name}, #{username}, #{password}, #{phone}, #{sex}, #{idNumber}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void addEmployee(Employee employee);

    //分页查询

    Page<Employee> pageSearch(EmployeePageQueryDTO employeePageQueryDTO);

    //直接创建动态类sql,以后只要是修改,都可以调用这个方法
    void update(Employee employee);

    @Select("select * from employee where id = #{id}")
    Employee getEmployeeById(String id);

}
