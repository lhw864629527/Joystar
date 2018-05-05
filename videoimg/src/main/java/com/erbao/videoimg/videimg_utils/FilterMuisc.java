package com.erbao.videoimg.videimg_utils;

import android.content.Context;
import android.util.Log;

import com.erbao.videoimg.gpufilter.helper.MagicFilterType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asus on 2018/3/12.
 */

public class FilterMuisc {
   public static boolean isLanguage=true;
    static List<String> mp3;
    /**
     * 获取Filter 滤镜
     * @return
     */
    public static  List<Map<String,Object>>  getFilter(){
        MagicFilterType[]  filterTypes = {MagicFilterType.NONE,MagicFilterType.WARM,MagicFilterType.ANTIQUE,MagicFilterType.COOL,MagicFilterType.BRANNAN,
                MagicFilterType.FREUD,MagicFilterType.HEFE,MagicFilterType.HUDSON,MagicFilterType.INKWELL,MagicFilterType.N1977,MagicFilterType.NASHVILLE};
        String[] filterstr_en={"No filter","WARM","ANTIQUE","COOL","BRANNAN","FREUD","HEFE","HUDSON","INKWELL","N1977","NASHVILLE"};
        String[] filterstr_cn={"原图","优雅","午后","月光","BRANNAN","薄荷","HEFE","HUDSON","褪色","N1977","乡村"};
        List<Map<String,Object>> mapList=new ArrayList<>();
        for (int i=0;i<filterTypes.length;i++){
            Map<String,Object> map=new HashMap<>();
            map.put("filter",filterTypes[i]);
            map.put("str_en",filterstr_en[i]);
            map.put("str_cn",filterstr_cn[i]);
            map.put("id",i);
            mapList.add(map);
        }
        return mapList;
    }

    /**
     * 设置语言
     * true :cn      false:en
     */
    public static void Language(boolean f){
        isLanguage=f;
    }



    /**
     * 获取MP3音乐
     * @param context
     * @return
     */
    public static List<String>  getMuisc(final Context context,final String mp3path){
        mp3=new ArrayList<>();
         mp3 = FileUtils.getInstance(context).getImagePathFromSD(mp3path, "mp3");
        if (mp3.size() < 1) {
            FileUtils.getInstance(context).copyAssetsToSD("mp3", mp3path).setFileOperateCallback(new FileUtils.FileOperateCallback() {
                @Override
                public void onSuccess() {
                    Log.e("TAG", "======复制成功========");
                    mp3 = FileUtils.getInstance(context).getImagePathFromSD(mp3path, "mp3");
                }
                @Override
                public void onFailed(String error) {
                    Log.e("TAG", "======复制失败========");
                }
            });
        }
        return mp3;
    }

}
