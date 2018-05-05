package com.erbao.joystar.okhttp;

import android.os.Environment;

import java.util.List;
import java.util.Map;

/**
 * Created by asus on 2018/1/29.
 */

public class HttpUrls {
    public static boolean isLogUtils = true;//是否开启LoG日志
    public static boolean isLogin = false;//是否登录
    //服务器地址
    public static String url = "http://47.91.228.17:8080/gorgias";
    //用户用手机号注册@JINBO
    public static String registerPhone = url + "/api/user/registerPhone";
    //用户发送验证码@JINBO
    public static String sendCode = url + "/api/user/sendCode";
    //用户登录@JINBO
    public static String userLoginPhone = url + "/api/user/userLoginPhone";
    //用户修改密码@JINBO
    public static String alterPassword = url + "/api/user/alterPassword";
    //忘记密码@JINBO
    public static String forgetPassword = url + "/api/user/forgetPassword";
    //超级用户列表@JINBO   HomeFragment_context 首页接口
    public static String superList = url + "/api/user/superList";
    //首页列表@JINBO  QzoneActivity 首页接口
    public static String pageList = url + "/api/user/pageList";
    //用户发布动态@JINBO
    public static String saveDynamic = url + "/api/user/saveDynamic";
    //用户查看某条动态评论详情@JINBO
    public static String ReviewDetails = url + "/api/user/ReviewDetails";
    //评论动态@JINBO likeType=0          点赞动态@JINBO   likeType=1
    public static String addReview = url + "/api/user/addReview";
    //上传视频@JINBO
    public static String saveVideo = url + "/api/user/saveVideo";
    //关注超级用户@JINBO
    public static String addFllow = url + "/api/user/addFllow";
    //用户修改头像@JINBO
    public static String updatePhoto = url + "/api/user/updatePhoto";
    //用户修改封面图@JINBO
    public static String updateCover = url + "/api/user/updateCover";
    //超级用户获取其他用户最早发布的动态@JINBO
    public static String dynamicList = url + "/api/user/dynamicList";
    //点赞封面图@JINBO
    public static String saveLike = url + "/api/user/saveLike";
    //用户修改信息@JINBO
    public static String updateUser = url + "/api/user/updateUser";
    //用户之间评论详细资料@JINBO
    public static String commentDetails = url + "/api/user/commentDetails";
    //用户之间相互评论@JINBO
    public static String addComment = url + "/api/user/addComment";








    //动态发布数据存储
    public static List<Map<String, Object>> QzoneSendnewslist;
    //文件夹
    public static  String mImagePath= Environment.getExternalStorageDirectory()+"/Yoystar/";
}
