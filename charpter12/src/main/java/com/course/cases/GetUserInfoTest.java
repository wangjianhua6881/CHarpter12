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
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class GetUserInfoTest {

    @Test(dependsOnGroups = "loginTrue",description = "获取用户信息接口测试")
    public void getUserInfo() throws IOException {
        SqlSession session = DatabaseUtil.getSqlSession();
        GetUserInfoById getUserInfo = session.selectOne("getUserInfoCase",1);
        System.out.println("获取用户信息用例数据" + getUserInfo.toString());
        System.out.println("获取用户信息路由：" + TestConfig.getUserInfoUrl);

        //将查询到的预期结果转换为JSONObject类型
        User user = session.selectOne("getUserInfo",getUserInfo);
        JSONObject jsonExpected = new JSONObject(user);
        //将接口返回的结果转换为JSONObject类型
        String result = getResultJson(getUserInfo,TestConfig.getUserInfoUrl);
        JSONObject jsonResult = new JSONObject(result);

        //将jsonObject转换为字符串类型
        String assertExpected = String.valueOf(jsonExpected);
        String assertResult = String.valueOf(jsonResult);

        //对比获取到的结果
        try{
            if (assertExpected.equals(assertResult)){
                System.out.println("用户信息对比成功！");
            }
        }catch (AssertionError error){
            error.printStackTrace();
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
