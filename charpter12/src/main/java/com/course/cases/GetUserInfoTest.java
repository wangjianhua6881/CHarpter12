package com.course.cases;

import com.course.config.TestConfig;
import com.course.module.GetUserInfoById;
import com.course.module.User;
import com.course.utils.DatabaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetUserInfoTest {

    @Test(dependsOnGroups = "loginTrue",description = "获取用户信息接口测试")
    public void getUserInfo() throws IOException {
        SqlSession session = DatabaseUtil.getSqlSession();
        GetUserInfoById getUserInfo = session.selectOne("getUserInfoCase",1);
        System.out.println("获取用户信息用例数据" + getUserInfo.toString());
        System.out.println("获取用户信息路由：" + TestConfig.getUserInfoUrl);

        List<User> userList = new ArrayList<User>();

        //将查询到的预期结果转换为JSONOArray类型
        User user = session.selectOne("getUserInfo",getUserInfo);
        userList.add(user);
        JSONArray jsonArrayExpected = new JSONArray(userList);
        System.out.println("预期结果：" + jsonArrayExpected.toString());

        //将接口返回的结果转换为JSONObject类型
        String result = getResultJson(getUserInfo,TestConfig.getUserInfoUrl);
        JSONArray jsonArrayResult = new JSONArray(result);
        System.out.println("实际结果：" + jsonArrayResult.toString());


        //对比获取到的结果
        if (jsonArrayExpected.toString().equals(jsonArrayResult.toString())){
            System.out.println("用户信息接口对比成功！");
        }else {
            throw new AssertionError();
        }

    }

    private String getResultJson(GetUserInfoById getUserInfoById,String url) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",getUserInfoById.getId());

        HttpPost post = new HttpPost(url);
        post.setHeader("content-type","application/json");
        StringEntity entity = new StringEntity(jsonObject.toString(),"utf-8");
        post.setEntity(entity);

        HttpResponse response = TestConfig.client.execute(post);
        String result = EntityUtils.toString(response.getEntity(),"utf-8");
        System.out.println("请求GetUserInfo接口返回的内容：" + result);
        return result;
    }

//    @Test(dependsOnMethods = {"getUserInfo"})
//    private User getUserObject(JSONObject jsonObject){
//        User user = new User();
//        user.setId(jsonObject.getInt("id"));
//        user.setUserName(jsonObject.getString("userName"));
//        user.setPassword(jsonObject.getString("password"));
//        user.setAge(jsonObject.getString("age"));
//        user.setSex(jsonObject.getString("sex"));
//        user.setIsDelete(jsonObject.getString("isDelete"));
//        user.setPermission(jsonObject.getString("permission"));
//        return user;
//    }


}
