package com.jc666.springboot.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jc666.springboot.common.Constants;
import com.jc666.springboot.controller.dto.UserDTO;
import com.jc666.springboot.entity.Menu;
import com.jc666.springboot.entity.User;
import com.jc666.springboot.exception.ServiceException;
import com.jc666.springboot.mapper.RoleMapper;
import com.jc666.springboot.mapper.RoleMenuMapper;
import com.jc666.springboot.mapper.UserMapper;
import com.jc666.springboot.service.IMenuService;
import com.jc666.springboot.service.IUserService;
import com.jc666.springboot.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author L0veRamen
 * @since 2022-08-30
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private static final Log LOG = Log.get();

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    @Qualifier("menuServiceImpl")
    private IMenuService menuService;

    @Override
    public UserDTO login(UserDTO userDTO) {
//        List<User> list  = list(queryWrapper);
//        return list.size() != 0;
        User one = getUserInfo(userDTO);
        if (one != null) {
            BeanUtil.copyProperties(one, userDTO, true);
            //set token
            String token = TokenUtils.genToken(one.getId().toString(), one.getPassword());
            userDTO.setToken(token);
            System.out.println(token);

            String role = one.getRole(); // ROLE_ADMIN
            Integer roleId = roleMapper.selectByFlag(role);
            List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);


            // 设置用户的菜单列表
            List<Menu> roleMenus = getRoleMenus(role);
            userDTO.setMenus(roleMenus);

            return userDTO;
        } else {
            throw new ServiceException(Constants.CODE_600, "Username or Password is not correct !");
        }
    }

    @Override
    public User register(UserDTO userDTO) {
        User one = getUserInfo(userDTO);
       if (one == null){
           one = new User();
           BeanUtil.copyProperties(userDTO, one, true);
           save(one); // copy user and save into database
       }else{
           throw new ServiceException(Constants.CODE_600, "User does not exist !");
       }
       return one;
    }

    private User getUserInfo(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        queryWrapper.eq("password", userDTO.getPassword());
        User one;
        try {
            one = getOne(queryWrapper); // find user data from database
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "System Error !");
        }
        return one;
    }

    /**
     * 获取当前角色的菜单列表
     * @param roleFlag
     * @return
     */
    private List<Menu> getRoleMenus(String roleFlag) {
        Integer roleId = roleMapper.selectByFlag(roleFlag);
        // 当前角色的所有菜单id集合
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);

        // 查出系统所有的菜单(树形)
        List<Menu> menus = menuService.findMenus("");
        // new一个最后筛选完成之后的list
        List<Menu> roleMenus = new ArrayList<>();
        // 筛选当前用户角色的菜单
        for (Menu menu : menus) {
            if (menuIds.contains(menu.getId())) {
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            // removeIf()  移除 children 里面不在 menuIds集合中的 元素
            children.removeIf(child -> !menuIds.contains(child.getId()));
        }
        return roleMenus;
    }

}
