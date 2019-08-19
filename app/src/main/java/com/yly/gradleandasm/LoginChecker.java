package com.yly.gradleandasm;

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
        return true;
    }
}
