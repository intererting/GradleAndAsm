package com.yly.gradleandasm;

import kotlin.jvm.JvmField;

import java.util.HashMap;
import java.util.Map;

public class TestJavaClass {


    @JvmField
    static HashMap map = new HashMap<String, String>();
    @JvmField
    static int a = 0;

    static void initData(String name) {
        System.out.println("call initData xxxx");
        a = 200;
        map.put("name", name);
    }

    void testInitData() {
    }

}
