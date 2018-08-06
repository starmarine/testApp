package com.example.tmac.testapp;


import com.alibaba.fastjson.JSON;
import com.example.tmac.testapp.test.TestJsonDto;
import com.example.tmac.testapp.utils.http.HttpUtils;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
//@RunWith(RobolectricTestRunner.class)
public class HttpUtilsTest {

    @Test
    public void test() throws Exception {
        String body = HttpUtils.get("http://baike.baidu.com/api/openapi/BaikeLemmaCardApi?scope=103&format=json&appid=379020&bk_key=test&bk_length=10",null);
        System.out.println(body);
        TestJsonDto dto = JSON.parseObject(body, TestJsonDto.class);
        System.out.print(dto);
    }

    @Test
    public void testException() throws Exception {
        String body = HttpUtils.get("http://localhost:8080/test/exception",null);
        System.out.println(body);
        TestJsonDto dto = JSON.parseObject(body, TestJsonDto.class);
        System.out.print(dto);
    }

}