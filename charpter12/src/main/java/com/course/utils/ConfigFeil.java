package com.course.utils;

import com.course.module.InterfaceName;

import java.util.Locale;
import java.util.ResourceBundle;

public class ConfigFeil {

    private static ResourceBundle bundle = ResourceBundle.getBundle("application",Locale.CHINA);

    public static String getUrl(InterfaceName interfaceName){
        String url = bundle.getString("test.url");
        String uri = "";
        String testUrl = "";

        if (interfaceName.equals(InterfaceName.LOGIN)){
            uri = bundle.getString("login.uri");
        }

        if (interfaceName.equals(InterfaceName.ADDUSER)){
            uri = bundle.getString("addUser.uri");
        }

        if (interfaceName.equals(InterfaceName.GETUSERINFO)){
            uri = bundle.getString("getUserInfo.uri");
        }

        if (interfaceName.equals(InterfaceName.UPDATEUSERINFO)){
            uri = bundle.getString("updateUserInfo.uri");
        }

        if (interfaceName.equals(InterfaceName.GETUSERLIST)){
            uri = bundle.getString("getUserList.uri");
        }

        testUrl = url + uri;
        return testUrl;
    }
}
