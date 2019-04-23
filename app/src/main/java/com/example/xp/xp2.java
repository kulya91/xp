package com.example.xp;


import android.app.Activity;
import android.app.AndroidAppHelper;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class xp2 {
    private ClassLoader load;

    public void starthook(XC_LoadPackage.LoadPackageParam lpparam) {
        load = lpparam.classLoader;

        XposedBridge.log("开始hook");
        hook1();
        XposedBridge.log("hook1完成");
        hook2();
        XposedBridge.log("hook2完成");
        hook3();
        XposedBridge.log("hook3完成");
        XposedBridge.log("hook结束");
    }

    private void hook1() {
        XposedHelpers.findAndHookMethod("com.ushaqi.zhuishushenqi.ui.home.HomeActivity",
                load, "onCreate", Bundle.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        final Class sharesdk_class =
                                XposedHelpers.findClass("com.ushaqi.zhuishushenqi.util.e", load);
                        XposedHelpers.callStaticMethod(sharesdk_class, "c", "share_book");
                        XposedHelpers.callStaticMethod(sharesdk_class, "c", "share");
                    }
                });
    }

    private void hook2() {
        XposedBridge.log("hook2开始");
        XposedHelpers.findAndHookMethod("com.ushaqi.zhuishushenqi.ui.user.UserInfoActivity",
                load, "onCreate", Bundle.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Object obj = param.thisObject;
                            ImageView imageView = (ImageView) XposedHelpers.getObjectField(obj, "g");
                            imageView.performClick();
                    }
                });
    }

    private void hook3() {
        XposedBridge.log("hook3开始");
        Class view_class = XposedHelpers.findClass("com.ushaqi.zhuishushenqi.reader.PageBinder", load);
        XposedHelpers.findAndHookMethod(view_class, "n",
                new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                        Object object = param.thisObject;
                        Object background = XposedHelpers.getObjectField(object, "f");
                        Object title = XposedHelpers.getObjectField(object, "g");
                        Object text = XposedHelpers.getObjectField(object, "h");
                        Object background_id = XposedHelpers.getObjectField(object, "c");
                        int int_background = (int) XposedHelpers.getObjectField(background_id, "h");
                        File file = new File("/sdcard/ZhuiShuShenQi/bj.png");
                        File color = new File("/sdcard/ZhuiShuShenQi/color.txt");
                        if (color.exists()) {
                            FileReader fr;
                            BufferedReader reader;
                            try {
                                fr = new FileReader(color);
                                reader = new BufferedReader(fr);
                                String[] text_color = new String[4];
                                int i = 0;
                                while ((text_color[i++] = reader.readLine()) != null) {
                                    if(i==2)break;
                                }
                                XposedBridge.log("text读取中......" +text_color[0]);
                                XposedBridge.log("title读取中......" +text_color[1]);
                                if (text_color[0] != null)
                                    XposedHelpers.callMethod(text, "setTextColor",Color.parseColor(text_color[0]));
                                if (text_color[1] != null)
                                    XposedHelpers.callMethod(title, "setTextColor",Color.parseColor(text_color[1]));
                            } catch (Exception e) {
                                XposedBridge.log("读取失败");
                            }
                        }
                        if (file.exists() && int_background == 0x7f020265) {
                            FileInputStream fis;
                            try {
                                fis = new FileInputStream(file);
                                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                                Drawable drawable = new BitmapDrawable(bitmap);
                                XposedHelpers.callMethod(background, "setBackground", drawable);
                            } catch (FileNotFoundException e) {
                                XposedBridge.log("hook3失败");
                            }
                            return null;
                        } else if (!file.exists() && int_background == 0x7f020265) {
                            XposedHelpers.callMethod(background, "setBackgroundResource", int_background);
                            return null;
                        } else
                            return XposedBridge.invokeOriginalMethod(param.method, param.thisObject, param.args);

                    }
                }
        );


    }

}