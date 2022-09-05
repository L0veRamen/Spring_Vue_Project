package com.jc666.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 
 * </p>
 *
 * @author L0veRamen
 * @since 2022-08-30
 */
@Getter
@Setter
@TableName("sys_user")
@ApiModel(value = "User对象", description = "")
@ToString
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id	")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("username")
    private String username;

    @ApiModelProperty("password")
    private String password;

    @ApiModelProperty("nickname")
    private String nickname;

    @ApiModelProperty("email")
    private String email;

    @ApiModelProperty("phone")
    private String phone;

    @ApiModelProperty("address")
    private String address;

    @ApiModelProperty("created time by user")
    private Date createTime;

    @ApiModelProperty("person image")
    private String avatarUrl;

    @ApiModelProperty("role")
    private String role;

}
