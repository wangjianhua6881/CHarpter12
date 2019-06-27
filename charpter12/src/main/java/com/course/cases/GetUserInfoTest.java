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
        System.out.println(getUserInfo.toString());
        System.out.println(TestConfig.getUserInfoUrl);

        User user = session.selectOne("getUserInfo",getUserInfo);

        String result = getResultJson(getUserInfo,TestConfig.getUserInfoUrl);
        JSONObject resultJson = new JSONObject(result);
        User user1 = getUserObject(resultJson);
        Assert.assertEquals(user,user1);

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
        System.out.println("请求接口返回的内容：" + result);
        return result;
    }

    private User getUserObject(JSONObject jsonObject){
        User user = new User();
        user.setId(jsonObject.getInt("id"));
        user.setUserName(jsonObject.getString("userName"));
        user.setPassword(jsonObject.getString("password"));
        user.setAge(jsonObject.getString("age"));
        user.setSex(jsonObject.getString("sex"));
        user.setIsDelete(jsonObject.getString("isDelete"));
        user.setPermission(jsonObject.getString("permission"));
        return user;
    }


}
