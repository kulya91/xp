package com.example.xp;


import android.app.Application;
import android.app.Instrumentation;
import android.content.SharedPreferences;

import java.io.File;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class xpmain implements IXposedHookLoadPackage {
    public static boolean ISSIGNED;
    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.ushaqi.zhuishushenqi")) {
            return;
        }
        XposedBridge.log("1");
        XposedHelpers.findAndHookMethod(Instrumentation.class, "callApplicationOnCreate",
                Application.class, new XC_MethodHook() {
            @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        new xp2().starthook(lpparam);
                    }
                }
        );
    }

}
