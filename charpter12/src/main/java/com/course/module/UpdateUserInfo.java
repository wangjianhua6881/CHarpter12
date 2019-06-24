package com.course.module;

import lombok.Data;

@Data
public class UpdateUserInfo {

    private int id;
    private int userId;
    private String userName;
    private String age;
    private String sex;
    private String isDelete;
    private String permission;
}
