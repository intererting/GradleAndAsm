package com.yly.gradleandasm;

public class TestJavac {

    void test() {
        if (LoginChecker.getInstance().isLogin()) {
            System.out.println("xxx");
        } else {
            System.out.println("yyy");
        }
    }
}
