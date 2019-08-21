package com.yly.gradleandasm;

import com.yly.asmannotation.LoginCallback;

@LoginCallback
public class LoginChecker {

    private static volatile LoginChecker loginChecker;

    public static LoginChecker getInstance() {
        if (loginChecker == null) {
            synchronized (LoginChecker.class) {
                if (loginChecker == null) {
                    loginChecker = new LoginChecker();
                }
            }
        }
        return loginChecker;
    }

    boolean isLogin() {
        return false;
    }
}
