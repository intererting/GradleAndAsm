package com.yly.gradleandasm;

import com.yly.asmannotation.NeedLoginCheck;

public class Template {

    @NeedLoginCheck()
    public void temp() {
        System.out.println("temp");
    }
}