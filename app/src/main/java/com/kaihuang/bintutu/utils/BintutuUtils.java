package com.kaihuang.bintutu.utils;

import com.kaihuang.bintutu.home.bean.ShowTagPictureBean;
import com.kaihuang.bintutu.home.bean.TagPictureDesc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 该项目辅助工具类
 * Created by zhaopf on 2018/3/22.
 */

public class BintutuUtils {

    /**
     * 拼成jsonarray
     * @return jsonArray
     * @throws JSONException
     */
    public static JSONArray objToJson(List<ShowTagPictureBean> leftFootPictureList,List<ShowTagPictureBean> rightFootPictureList ) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        if(leftFootPictureList != null &&leftFootPictureList.size() > 0){
            for(ShowTagPictureBean showTagPictureBean : leftFootPictureList){
                JSONObject jsonObject1 = new JSONObject();
                if("1".equals(showTagPictureBean.getIsFrom())){
                    jsonObject1.put("foot_label_image",showTagPictureBean.getFileName());
                    jsonObject1.put("foot_label_number",showTagPictureBean.getPosition() + "");
                    jsonObject1.put("footshape_data_id",0);
                    JSONArray jsonArray1 = new JSONArray();
                    if(showTagPictureBean.getDescList() != null && showTagPictureBean.getDescList().size() >0) {
                        for (String des : showTagPictureBean.getDescList()) {
                            JSONObject jsonObject2 = new JSONObject();
                            jsonObject2.put("footshape_label_desc_id",0);
                            jsonObject2.put("footshape_label_desc",des);
                            jsonArray1.put(jsonObject2);
                        }
                        jsonObject1.put("footshape_label_desc_List",jsonArray1);
                    }
                    jsonArray.put(jsonObject1);
                }
            }
        }

        if(rightFootPictureList != null &&rightFootPictureList.size() > 0){
            for(ShowTagPictureBean showTagPictureBean : rightFootPictureList){
                JSONObject jsonObject1 = new JSONObject();
                if("1".equals(showTagPictureBean.getIsFrom())){
                    jsonObject1.put("foot_label_image",showTagPictureBean.getFileName());
                    jsonObject1.put("foot_label_number",showTagPictureBean.getPosition() + "");
                    jsonObject1.put("footshape_data_id",0);
                    JSONArray jsonArray1 = new JSONArray();
                    if(showTagPictureBean.getDescList() != null && showTagPictureBean.getDescList().size() >0) {
                        for (String des : showTagPictureBean.getDescList()) {
                            JSONObject jsonObject2 = new JSONObject();
                            jsonObject2.put("footshape_label_desc_id",0);
                            jsonObject2.put("footshape_label_desc",des);
                            jsonArray1.put(jsonObject2);
                        }
                        jsonObject1.put("footshape_label_desc_List",jsonArray1);
                    }
                    jsonArray.put(jsonObject1);
                }
            }
        }
        return jsonArray;
    }


    /**
     * 返回一个示例脚型图的描述信息
     * @param descList
     * @param position
     * @return
     */
    public static List<TagPictureDesc> singleList(List<String> descList, int position){
        List<TagPictureDesc> tagPictureDescList = new ArrayList<>();
        if(descList != null && descList.size() > 0){
            for(String des : descList){
                TagPictureDesc tagPictureDesc = new TagPictureDesc();
                tagPictureDesc.setIndex(position+ "");
                tagPictureDesc.setDesc(des);
                tagPictureDescList.add(tagPictureDesc);
            }
        }
        return tagPictureDescList;
    }

    /**
     * 计算推荐尺码
     */
    public static String recommdSize(double footLen){
        String size = "";
        if( 113<= footLen && footLen <=117){
            size = "17";
        }else if(118 <= footLen && footLen <= 122 ){
            size = "18";
        }else if(123 <= footLen && footLen <= 127 ){
            size = "19";
        }else if(128 <= footLen && footLen <= 132 ){
            size = "20";
        }else if(133 <= footLen && footLen <= 137 ){
            size = "21";
        }else if(138 <= footLen && footLen <= 142 ){
            size = "22";
        }else if(143 <= footLen && footLen <= 147 ){
            size = "23";
        }else if(148 <= footLen && footLen <= 152 ){
            size = "24";
        }else if(153 <= footLen && footLen <= 157 ){
            size = "25";
        }else if(158 <= footLen && footLen <= 162 ){
            size = "26";
        }else if(163 <= footLen && footLen <= 167 ){
            size = "26.5";
        }else if(168 <= footLen && footLen <= 172 ){
            size = "27";
        }else if(173 <= footLen && footLen <= 177 ){
            size = "28";
        }else if(178 <= footLen && footLen <= 182 ){
            size = "29";
        }else if(183 <= footLen && footLen <= 187 ){
            size = "29.5";
        }else if(188 <= footLen && footLen <= 192 ){
            size = "30";
        }else if(193 <= footLen && footLen <= 197 ){
            size = "30.5";
        }else if(198 <= footLen && footLen <= 202 ){
            size = "31";
        }else if(203 <= footLen && footLen <= 207 ){
            size = "31.5";
        }else if(208 <= footLen && footLen <= 212 ){
            size = "32";
        }else if(213 <= footLen && footLen <= 215 ){
            size = "33";
        }else if(215 < footLen && footLen <= 217 ){
            size = "33.5";
        }else if(218 <= footLen && footLen <= 220 ){
            size = "34";
        }else if(220 < footLen && footLen <= 222 ){
            size = "34.5";
        }else if(223 <= footLen && footLen <= 225 ){
            size = "35";
        }else if(225 < footLen && footLen <= 227 ){
            size = "35.5";
        }else if(228 <= footLen && footLen <= 230 ){
            size = "36";
        }else if(230 < footLen && footLen <= 227 ){
            size = "36.5";
        }else if(233 <= footLen && footLen <= 235 ){
            size = "37";
        }else if(235 < footLen && footLen <= 237 ){
            size = "37.5";
        }else if(238 <= footLen && footLen <= 240 ){
            size = "38";
        }else if(240 < footLen && footLen <= 242 ){
            size = "38.5";
        }else if(243 <= footLen && footLen <= 245 ){
            size = "39";
        }else if(245 < footLen && footLen <= 247 ){
            size = "39.5";
        }else if(248 <= footLen && footLen <= 250 ){
            size = "40";
        }else if(250 < footLen && footLen <= 252 ){
            size = "40.5";
        }else if(253 <= footLen && footLen <= 255 ){
            size = "41";
        }else if(255 < footLen && footLen <= 257 ){
            size = "41.5";
        }else if(258 <= footLen && footLen <= 260 ){
            size = "42";
        }else if(260 < footLen && footLen <= 262 ){
            size = "42.5";
        }else if(263 <= footLen && footLen <= 265 ){
            size = "43";
        }else if(265 < footLen && footLen <= 267 ){
            size = "43.5";
        }else if(268 <= footLen && footLen <= 270 ){
            size = "44";
        }else if(270 < footLen && footLen <= 272 ){
            size = "44.5";
        }else if(273 <= footLen && footLen <= 275 ){
            size = "45";
        }else if(275 < footLen && footLen <= 277 ){
            size = "45.5";

        }else if(278 <= footLen && footLen <= 280 ){
            size = "46";
        }else if(280 < footLen && footLen <= 282 ){
            size = "46.5";
        }else if(283 <= footLen && footLen <= 285 ){
            size = "47";
        }else if(285 < footLen && footLen <= 287 ){
            size = "47.5";
        }else if(288 <= footLen && footLen <= 290 ){
            size = "48";
        }else if(290 < footLen && footLen <= 292 ){
            size = "48.5";
        }else if(293 <= footLen && footLen <= 295 ){
            size = "49";
        }else if(295 < footLen && footLen <= 297 ){
            size = "49.5";
        }else if(298 <= footLen && footLen <= 300 ){
            size = "50";
        }
        return size;
    }
}
