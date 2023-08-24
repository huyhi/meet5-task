package com.hongye.model;

import lombok.Data;

import java.util.Date;


@Data
public class User {

    private Integer id;

    private String username;

    private String occupation;

    private String avatar;

    private String email;

    private Integer gender;

    private Integer status;

    private String description;

    private Date birthday;

    private Date createdAt;

    private Date updatedAt;

}
