package com.course.cases;

import com.course.config.TestConfig;
import com.course.module.GetUserList;
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
import java.util.List;

public class GetUserListInfoTest {

    @Test(dependsOnGroups = "loginTrue",description = "获取用户信息列表接口测试")
    public void getUserListInfo() throws IOException {
        SqlSession session = DatabaseUtil.getSqlSession();
        GetUserList getUserList = session.selectOne("getUserInfoListCase",1);
        System.out.println(getUserList.toString());
        System.out.println(TestConfig.getUserListUrl);

        JSONArray getArray = getJSONArray(getUserList,TestConfig.getUserListUrl);
        List<User> userList = session.selectList("getUserInfoList",getUserList);

        for (User u:userList) {
            System.out.println("获取到的用户信息：" + u);
        }

        JSONArray sessionList = new JSONArray(userList);

        Assert.assertEquals(sessionList.length(),getArray.length());

        String array1 = String.valueOf(sessionList);
        String array2 = String.valueOf(getArray);

        try{
            if (array1.equals(array2)){
                System.out.println("对比成功！");
            }
        }catch (AssertionError error){
            error.printStackTrace();
        }

//        System.out.println("sessionList数据" + sessionList);
//        System.out.println("getArray数据" + getArray);
//
//        for (int i = 0; i < userList.size(); i++) {
//            JSONObject expected = (JSONObject) sessionList.get(i);
//            JSONObject result = (JSONObject) getArray.get(i);
//            Assert.assertEquals(expected,result);
//        }

    }

    private JSONArray getJSONArray(GetUserList userList,String url) throws IOException {
        JSONObject json = new JSONObject();
        json.put("id",userList.getId());
        json.put("userName",userList.getUserName());
        json.put("age",userList.getAge());
        json.put("sex",userList.getSex());
        json.put("permission",userList.getPermission());

        HttpPost post = new HttpPost(url);
        StringEntity entit = new StringEntity(json.toString(),"utf-8");
        post.setEntity(entit);
        post.setHeader("content-type","application/json");

        HttpResponse response = TestConfig.client.execute(post);
        String result = EntityUtils.toString(response.getEntity(),"utf-8");
        JSONArray jsonArray = new JSONArray(result);
        return jsonArray;
    }

}
