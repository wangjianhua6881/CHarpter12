package com.course.module;

import lombok.Data;

@Data
public class AddUser {

    private int id;
    private String userName;
    private String password;
    private String age;
    private String sex;
    private String permission;
    private String isDelete;
    private String expected;
}
