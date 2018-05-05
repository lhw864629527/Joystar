package com.erbao.joystar.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by asus on 2018/1/29.
 */

public class CheckData {

    //判断手机号是否正确 true 正确   false不正确
    public static boolean isPhoneNumberValid(String phoneNumber) {
        boolean isValid = false;
        CharSequence inputStr = phoneNumber;
        //正则表达式
        String phone = "^1[34578]\\d{9}$";
        Pattern pattern = Pattern.compile(phone);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * 判断邮箱格式
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }


    /**
     * 密码格式判断
     * 至少需要一个英文大写字母与一个数字.
     *
     * @param
     * @return
     */
    public static boolean PwdFormat(String pwd) {
//        Pattern p = Pattern.compile(
//                "^(?![A-Za-z]+$)(?![A-Z\\d]+$)(?![A-Z\\W]+$)(?![a-z\\d]+$)(?![a-z\\W]+$)(?![\\d\\W]+$)\\S{8,20}$");
        Pattern p = Pattern.compile(
                "^(?=.*\\d)(?=.*[A-Z])(?=.*[A-Z])[a-zA-Z0-9]{8,20}$");
        Matcher m = p.matcher(pwd);
        return m.find();
    }


    //null 转 “”
    public static String getnull(String ss) {
        String dd = "";
        LogUtils.e("======getnull====" + ss);

        if (ss.equals(null) || ss.equals("null") || ss.equals("")) {
            return dd;
        } else {
            if (ss.toLowerCase().contains("failed to connect to")) {
                return "server exception";
            } else {
                return ss;
            }
        }
    }


    /**
     * 隐藏软键盘
     **/
    public static void inputmanger(Activity context) {
        View view = context.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    //將list数据以json形式保存在手机Share...中s//保存支付缓存数据
    public static void saveInfo(List<Map<String, String>> datas) {
        try {
            JSONArray array = new JSONArray();
            for (int i = 0; i < datas.size(); i++) {
                Map<String, String> map = datas.get(i);
                JSONObject object = new JSONObject();
                for (Map.Entry entry : map.entrySet()) {
                    object.put(entry.getKey() + "", map.get(entry.getKey()) + "");
                }
                array.put(object);
            }
            LogUtils.e("=======saveInfo==+9+9==" + array);
            SpUtil.put("listpaymento", array + "");
            LogUtils.e("=======saveInfo==+9+9==" + SpUtil.get("listpaymento", "----"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //將list数据以json形式保存在手机Share...中
    public static void saveInfojson(List<Map<String, Object>> datas, String Sputilname) {
        try {
            JSONArray array = new JSONArray();
            for (int i = 0; i < datas.size(); i++) {
                Map<String, Object> map = datas.get(i);
                JSONObject object = new JSONObject();
                for (Map.Entry entry : map.entrySet()) {
                    object.put(entry.getKey() + "", map.get(entry.getKey()) + "");
                }
                array.put(object);
            }
            SpUtil.put(Sputilname, array + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //解保存在手机Share...中的json 数据
    public static List<Map<String, Object>> pullInfojson(String Sputilname) {
        List<Map<String, Object>> datas = new ArrayList<>();
        if (SpUtil.get(Sputilname, "").toString().length() > 2) {
            String jsonStr = SpUtil.get(Sputilname, "").toString();
            try {
                JSONArray array = new JSONArray(jsonStr);
                for (int i = 0; i < array.length(); i++) {
                    Map<String, Object> map = new HashMap<>();
                    JSONObject object = array.getJSONObject(i);
                    Iterator<?> it = object.keys();
                    String key = "";
                    String vaule = "";
                    while (it.hasNext()) {//遍历JSONObject
                        key = (String) it.next().toString();
                        vaule = object.getString(key);
                        map.put(key, vaule);
                    }
                    datas.add(map);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        return datas;

    }

    /**
     *
     * @param calendar 日期字符串"2017-01-01 00:00:00"
     * @param dateType 0:年	1：月	2：天	3：时区
     * @param dataCount 时间值 可为正 也可为负
     * @return 国际时间转中国时间
     */
    /**
     * @return
     */
    public static String dateset(String stringDate) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ;//设置日期格式
        String returnDate = "";
        try {
            java.util.Date date = sdf.parse(stringDate);
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(date);
            cal.add(java.util.Calendar.MINUTE, 8 * 60);
            cal.setTimeZone(java.util.TimeZone.getTimeZone("Asia/Shanghai"));
            returnDate = sdf.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnDate;
    }


}
