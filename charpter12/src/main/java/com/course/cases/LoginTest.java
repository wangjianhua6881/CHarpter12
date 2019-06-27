package com.course.cases;

import com.course.config.TestConfig;
import com.course.module.InterfaceName;
import com.course.module.Login;
import com.course.utils.ConfigFeil;
import com.course.utils.DatabaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.io.IOException;

public class LoginTest {

    @BeforeTest(groups = "loginTrue",description = "测试准备工作")
    public void beforeTest(){

        TestConfig.client = new DefaultHttpClient();
        TestConfig.loginUrl = ConfigFeil.getUrl(InterfaceName.LOGIN);
        TestConfig.getUserListUrl = ConfigFeil.getUrl(InterfaceName.GETUSERLIST);
        TestConfig.getUserInfoUrl = ConfigFeil.getUrl(InterfaceName.GETUSERINFO);
        TestConfig.addUserUrl = ConfigFeil.getUrl(InterfaceName.ADDUSER);
        TestConfig.updateUserInfoUrl = ConfigFeil.getUrl(InterfaceName.UPDATEUSERINFO);
    }

    @Test(groups = "loginTrue")
    public void loginTestTrue() throws IOException {
        SqlSession session = DatabaseUtil.getSqlSession();
        Login loginTest = session.selectOne("loginCase",1);
        System.out.println(loginTest.toString());
        System.out.println(TestConfig.loginUrl);

        String result = getResult(loginTest,TestConfig.loginUrl);

        Assert.assertNotNull(result);

    }

    @Test(groups = "loginFail")
    public void loginTestFail() throws IOException {
        SqlSession session = DatabaseUtil.getSqlSession();
        Login loginTest = session.selectOne("loginCase",2);
        System.out.println(loginTest.toString());

        String result = getResult(loginTest,TestConfig.loginUrl);
        Assert.assertEquals(loginTest.getExpected(),result);
    }

    private String getResult(Login login,String url) throws IOException {

        JSONObject json = new JSONObject();
        json.put("userName",login.getUserName());
        json.put("password",login.getPassword());

        HttpPost post = new HttpPost(url);
        post.setHeader("content-type","application/json");
        StringEntity entity = new StringEntity(json.toString(),"utf-8");
        post.setEntity(entity);

        HttpResponse response = TestConfig.client.execute(post);
        TestConfig.cookieStore = TestConfig.client.getCookieStore();

        String result = EntityUtils.toString(response.getEntity(),"utf-8");
        System.out.println("登录失败返回的值" + result);
        return result;
    }


}
