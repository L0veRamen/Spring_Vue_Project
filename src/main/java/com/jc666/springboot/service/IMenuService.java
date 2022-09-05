package com.jc666.springboot.service;

import com.jc666.springboot.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author L0veRamen
 * @since 2022-09-02
 */
public interface IMenuService extends IService<Menu> {
    List<Menu> findMenus(String name);
}
