package com.course.module;

import lombok.Data;

@Data
public class Login {
    private String userName;
    private String password;
    private String expected;
}
