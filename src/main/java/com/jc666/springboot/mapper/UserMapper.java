package com.jc666.springboot.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jc666.springboot.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

//@Mapper
public interface UserMapper extends BaseMapper<User> {

//    @Select("select * from sys_user")
//    List<User> findAll();
//
//    @Insert("Insert into sys_user(username, password, nickname, email, phone, address) values(#{username}, #{password}, #{nickname}, #{email}, #{phone}, #{address})")
//    int insert(User user);
//
//    int update(User user);
//
//    @Delete("delete from sys_user where id = #{id}")
//    int deleteById(@Param("id") Integer id);
//
//
//    @Select("select * from sys_user where username like #{username} limit #{pageNum}, #{pageSize}")
//    List<User> selectPage(Integer pageNum, Integer pageSize, String username);
//
//    @Select("select count(*) from sys_user where username like #{username}")
//    Integer selectTotal(String username);
}
