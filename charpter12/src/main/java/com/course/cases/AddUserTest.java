package com.course.cases;

import com.course.config.TestConfig;
import com.course.module.AddUser;
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

public class AddUserTest {

    @Test(dependsOnGroups = "loginTrue",description = "添加用户接口测试")
    public void addUser() throws IOException, InterruptedException {
        SqlSession session = DatabaseUtil.getSqlSession();
        AddUser addUser = session.selectOne("addUserCase",9);
        System.out.println(addUser.toString());
        System.out.println(TestConfig.addUserUrl);

        String result = getResult(addUser,TestConfig.addUserUrl);
        Thread.sleep(3000);
        User user = session.selectOne("getUserInfo",addUser);
        Assert.assertEquals(addUser.getExpected(),result);
    }

    private String getResult(AddUser addUser,String url) throws IOException {

        JSONObject json = new JSONObject();
        json.put("id",addUser.getId());
        json.put("userName",addUser.getUserName());
        json.put("password",addUser.getPassword());
        json.put("age",addUser.getAge());
        json.put("sex",addUser.getSex());
        json.put("permission",addUser.getPermission());
        json.put("isDelete",addUser.getIsDelete());


        HttpPost post = new HttpPost(url);
        post.setHeader("content-type","application/json");
        StringEntity entity = new StringEntity(json.toString(),"utf-8");
        post.setEntity(entity);

        HttpResponse response = TestConfig.client.execute(post);

        String result = EntityUtils.toString(response.getEntity(),"utf-8");
        return result;
    }
}
