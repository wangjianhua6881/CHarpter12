package com.course.cases;

import com.course.config.TestConfig;
import com.course.module.UpdateUserInfo;
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

public class UpdateUserInfoTest {

    private static User user;

    @Test(dependsOnGroups = "loginTrue",description = "更行用户信息接口测试")
    public void updateUserInfo() throws IOException, InterruptedException {
        SqlSession session = DatabaseUtil.getSqlSession();
        UpdateUserInfo updateUserInfo = session.selectOne("updateUserInfoCase",1);
        System.out.println(updateUserInfo);
        System.out.println(TestConfig.updateUserInfoUrl);
        user = session.selectOne("updateUserInfo",updateUserInfo);
        int result = getResult(updateUserInfo,TestConfig.updateUserInfoUrl);
        Thread.sleep(3000);
        System.out.println("总共修改了：" + result + "处。");

    }

    @Test(dependsOnMethods = {"updateUserInfo"})
    public void assertUpdateLaterUserInfo() throws IOException {
        SqlSession session = DatabaseUtil.getSqlSession();
        UpdateUserInfo updateUserInfo = session.selectOne("updateUserInfoCase",1);
        User user1 = session.selectOne("updateLaterUserInfo",updateUserInfo);
        Assert.assertNotEquals(user,user1);
    }

    private int getResult(UpdateUserInfo updateUserInfo,String url) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",updateUserInfo.getUserId());
        jsonObject.put("userName",updateUserInfo.getUserName());
        jsonObject.put("age",updateUserInfo.getAge());
        jsonObject.put("sex",updateUserInfo.getSex());
        jsonObject.put("permission",updateUserInfo.getPermission());

        HttpPost post = new HttpPost(url);
        post.setHeader("content-type","application/json");
        StringEntity entity = new StringEntity(jsonObject.toString(),"utf-8");
        post.setEntity(entity);

        HttpResponse response = TestConfig.client.execute(post);
        Integer result = Integer.parseInt(EntityUtils.toString(response.getEntity(),"utf-8"));
        return result;
    }
}
