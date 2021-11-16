package com.yuan.test;

import com.yuan.utils.HttpUtils;
import org.junit.jupiter.api.Test;

/**
 * 测试工具类
 * @Classname TestMain
 * @Description :
 * @Date 2021/11/16 16:30
 * @Author cfy
 */
public class TestMain {
    @Test
    public void getBiliUrl() {
        String biliUrl = HttpUtils.getBiliUrl("https://www.bilibili.com/video/BV1z5411u7f3/");
        System.out.println(biliUrl);
    }
}
