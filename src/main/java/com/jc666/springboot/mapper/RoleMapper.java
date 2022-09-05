package com.jc666.springboot.mapper;

import com.jc666.springboot.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author L0veRamen
 * @since 2022-09-01
 */
public interface RoleMapper extends BaseMapper<Role> {

    @Select("select id from sys_role where flag = #{flag}")
    Integer selectByFlag(@Param("flag") String flag);
}
