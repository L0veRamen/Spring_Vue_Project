package com.jc666.springboot.controller.dto;


import com.jc666.springboot.entity.Menu;
import lombok.Data;

import java.util.List;


// get front end login request parameter
@Data
public class UserDTO {
    private String username;
    private String password;
    private String nickname;
    private String avatarUrl;
    private String token;
    private String role;
    private List<Menu> menus;

}
