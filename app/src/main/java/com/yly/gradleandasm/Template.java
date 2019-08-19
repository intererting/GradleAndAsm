package com.yly.gradleandasm;

import com.yly.manno.NeedLoginCheck;

public class Template {

    @NeedLoginCheck()
    public void temp() {
        System.out.println("temp");
    }
}