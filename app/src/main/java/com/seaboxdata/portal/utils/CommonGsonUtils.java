package com.seaboxdata.portal.utils;

import com.google.gson.JsonObject;
import com.linewell.innochina.core.entity.dto.FileListDTO;
import com.linewell.utils.GsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 带有处理过滤属性的Gson转换
 * Created by caicai on 2016-08-24.
 */
public class CommonGsonUtils {

    /**
     * json字符串转对象
     *
     * @param jsonStr jsonStr字符串
     * @param cls     转换对象
     * @param <T>     返回转换对象
     * @return
     */
    public static <T> T jsonToBean(String jsonStr, Class<T> cls) {
        try {
            return GsonUtil.jsonToBean(jsonStr, cls);
        } catch (Exception ex) {
            jsonStr = ClearNotFieldAttrUtils.clear(jsonStr, cls);
            return GsonUtil.jsonToBean(jsonStr, cls);
        }
    }

    /**
     * json字符串转对象
     *
     * @param jsonStr jsonStr字符串
     * @param cls     转换对象
     * @param <T>     返回转换对象
     * @return
     */
    public static <T> T jsonToBean(String jsonStr, Class<T> cls, Map<String, Class> subMap) {
        try {
            return GsonUtil.jsonToBean(jsonStr, cls);
        } catch (Exception ex) {
            jsonStr = ClearNotFieldAttrUtils.clear(jsonStr, cls, subMap);
            return GsonUtil.jsonToBean(jsonStr, cls);
        }
    }

    /**
     * json字符串转对象
     *
     * @param jsonObject jsonStr字符串
     * @param cls        转换对象
     * @param <T>        返回转换对象
     * @return
     */
    public static <T> T jsonToBean(JsonObject jsonObject, Class<T> cls) {
        try {
            return GsonUtil.jsonToBean(jsonObject, cls);
        } catch (Exception ex) {
            ClearNotFieldAttrUtils.clear(jsonObject, cls);
            return GsonUtil.jsonToBean(jsonObject, cls);
        }
    }

    /**
     * json字符串转对象
     *
     * @param jsonObject jsonStr字符串
     * @param cls        转换对象
     * @param <T>        返回转换对象
     * @return
     */
    public static <T> T jsonToBean(JsonObject jsonObject, Class<T> cls, Map<String, Class> subMap) {
        try {
            return GsonUtil.jsonToBean(jsonObject, cls);
        } catch (Exception ex) {
            ClearNotFieldAttrUtils.clear(jsonObject, cls, subMap);
            return GsonUtil.jsonToBean(jsonObject, cls);
        }
    }

    /**
     * 获取图片数组字符串
     *
     * @param picList
     * @return
     */
    public static String getFileJson(List<String> picList) {
        List<FileListDTO> picFileList = new ArrayList<>();
        if (picList != null && picList.size() > 0) {
            for (String pic : picList) {
                FileListDTO dto = new FileListDTO();
                dto.setFileId(pic);
                picFileList.add(dto);
            }
            return GsonUtil.getJsonStr(picFileList);
        }
        return "";
    }




}
