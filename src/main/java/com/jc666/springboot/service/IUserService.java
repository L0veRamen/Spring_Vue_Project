package com.jc666.springboot.service;

import com.jc666.springboot.controller.dto.UserDTO;
import com.jc666.springboot.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author L0veRamen
 * @since 2022-08-30
 */
public interface IUserService extends IService<User> {

    UserDTO login(UserDTO userDTO);

    User register(UserDTO userDTO);
}
