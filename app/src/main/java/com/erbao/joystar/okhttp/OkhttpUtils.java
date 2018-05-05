package com.erbao.joystar.okhttp;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;


import com.erbao.joystar.utils.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by asus on 2018/1/16.
 */

public class OkhttpUtils {
    public static OkHttpClient mOkHttpClient;

    private static OkhttpUtils okhttpUtils;

    public static OkhttpUtils getInstance(Activity activity) {
        if (okhttpUtils == null) {
            synchronized (OkhttpUtils.class) {
                if (okhttpUtils == null) {
                    okhttpUtils = new OkhttpUtils();

                    File sdcache = new File(activity.getCacheDir(), "mgb");//设置缓存
                    LogUtils.e("=====sdcache==========" + sdcache);
                    int cacheSize = 10 * 1024 * 1024;//设置缓存大小
                    OkHttpClient.Builder builder = new OkHttpClient.Builder()
                            .connectTimeout(15, TimeUnit.SECONDS)//设置连接超时时间
                            .writeTimeout(20, TimeUnit.SECONDS)//设置写超时时间
                            .readTimeout(20, TimeUnit.SECONDS)//设置读取超时时间
                            .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));//设置缓存
                    mOkHttpClient = builder.build();
                }
            }
        }
        return okhttpUtils;
    }


    /**
     * 异步GET请求
     *
     * @param url
     * @return
     */
    public <T> void Get(String url, final HttpCallBack<T> httpCallBack) {
        mOkHttpClient = new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder().url(url);
        //可以省略，默认是GET请求
        requestBuilder.method("GET", null);
        Request request = requestBuilder.build();
        Call mcall = mOkHttpClient.newCall(request);
        mcall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtils.e("---onResponse---" + response);
                httpCallBack.onSuccess(1, response.body().string().toString());
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e("---onFail---" + e.getMessage());
                httpCallBack.onFail(0, e.getMessage());
            }
        });
    }


    /**
     * 异步POST请求
     *
     * @param url
     * @param map
     * @param httpCallBack
     * @param <T>
     */
    public <T> void Post(String url, Map<String, Object> map, final HttpCallBack<T> httpCallBack) {
        RequestBody requestBody;

        FormBody.Builder formBody = new FormBody.Builder();

        for (String key : map.keySet()) {
            LogUtils.e("============" + key + "  " + map.get(key).toString());
            formBody.add(key, map.get(key).toString());
        }
        requestBody = formBody.build();
        mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e("---onFail---" + e.getMessage());
                httpCallBack.onFail(0, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtils.e("---onResponse---" + response);
                httpCallBack.onSuccess(1, response.body().string().toString());
            }
        });
    }

    /**
     * 异步上传文件
     * @param url
     * @param filed
     * @param httpCallBack
     * @param <T>
     *     同步的上传文件只要调用 mOkHttpClient.newCall(request).execute()就可以了
     */
    private <T> void postAsynFile(String url, File filed, final HttpCallBack<T> httpCallBack) {
        mOkHttpClient = new OkHttpClient();
        File file = filed;
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"), file))
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e("---onFail---" + e.getMessage());
                httpCallBack.onFail(0, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtils.e("---onResponse---" + response);
                httpCallBack.onSuccess(1, response.body().string().toString());
            }
        });
    }


    /**
     *异步下载文件
     * @param url
     * @param FilePath   保存的文件路径：如/sdcard/wangshu.jpg
     * @param httpCallBack
     * @param <T>
     */
    private <T> void downAsynFile(String url,final  String FilePath, final HttpCallBack<T> httpCallBack) {
        mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) {
                InputStream inputStream = response.body().byteStream();
                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(new File(FilePath));
                    byte[] buffer = new byte[2048];
                    int len = 0;
                    while ((len = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, len);
                    }
                    fileOutputStream.flush();
                    httpCallBack.onSuccess(1,"文件下载成功");
                } catch (IOException e) {
                    httpCallBack.onFail(0,e.getMessage());
                    Log.i("wangshu", "IOException");
                    e.printStackTrace();
                }

                Log.d("wangshu", "文件下载成功");
            }
        });
    }




    /**
     *异步上传Multipart文件
     * @param url
     * @param map    其他上传参数
     * @param FilePath  上传的文件路径：如/sdcard/wangshu.jpg
     * @param Filename  上传获取的名称：如wangshu.jpg
     * @param httpCallBack
     * @param <T>
     */
    public static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");//上传类型
//    public static final MediaType MEDIA_TYPE_PNG = MediaType.parse("multipart/form-data");//上传类型
    public <T>  void sendMultipart(String url, Map<String, Object> map, List<File> files, final  String Filename, final HttpCallBack<T> httpCallBack){
        mOkHttpClient = new OkHttpClient();

        LogUtils.e("=====FilePath=======" +files);

        MultipartBody.Builder multipartBody=new MultipartBody.Builder();
        multipartBody.setType(MultipartBody.FORM);
        //遍历map中所有参数到builder
        for (String key : map.keySet()) {
            LogUtils.e("============" + key + "  " + map.get(key).toString());
            multipartBody.addFormDataPart(key, map.get(key).toString());
        }
//        multipartBody.addFormDataPart("image", Filename,RequestBody.create(MEDIA_TYPE_PNG, new File(FilePath)));

//遍历paths中所有图片绝对路径到builder，并约定key如“upload”作为后台接受多张图片的key
        if (files != null){
            for (File file : files) {
                multipartBody.addFormDataPart(Filename, file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
            }
        }


        RequestBody requestBody=multipartBody.build();
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("title", "wangshu")
//                .addFormDataPart("image", Filename,RequestBody.create(MEDIA_TYPE_PNG, new File(FilePath)))
//                .build();

//        Request request = new Request.Builder()
//                .header("Authorization", "Client-ID " + "...")
//                .url(url)
//                .post(requestBody)
//                .build();


        Request.Builder RequestBuilder = new Request.Builder();
        RequestBuilder.url(url);// 添加URL地址
        RequestBuilder.post(requestBody);
        Request request = RequestBuilder.build();



        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                httpCallBack.onFail(0,e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.i("wangshu", response.body().string());
                httpCallBack.onSuccess(1,response.body().string().toString());

            }
        });
    }
}
