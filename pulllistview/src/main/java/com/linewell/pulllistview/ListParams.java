package com.linewell.pulllistview;

import java.io.Serializable;

/**
 * 列表传递的参数
 * Created by zjianning on 2016-07-12.
 */
public class ListParams implements Serializable{

    public static final int  FIELD_INTEGER_VALUE = 0;
    public static final int FIELD_STRING_VALUE=1;
    public static final int FIELD_BOOLEAN_VALUE = 2;
    public static final int FIELD_LONG_VALUE=3;
    /**
     * 服务端的地址
     */
    public String serviceUrl;

    /**
     * 加载个数
     */
    private int pageSize;

    /**
     * 排序字段名称
     */
    private String sortFieldName;

    public int getSortFieldType() {
        return sortFieldType;
    }


    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setSortFieldType(int sortFieldType) {
        this.sortFieldType = sortFieldType;
    }

    /**
     * 字段值类型
     */
    private int sortFieldType;

    /**
     * item的布局标识
     */
    public int itemLayoutId;

    /**
     * item布局文件中组件的id前缀
     */
    public String viewIdPrefix = "";

    /**
     * 字段的名称，item的布局xml定义要显示的字段
     */
    public String[] fieldNames;

    /**
     * R的类型(对应的id类  R.id.class)
     */
    private Class<?> rClass;

    /**
     * 刷新的时候 是否重新加载数据
     */
    private boolean isRefreshReload;

    /**
     * 与服务端交互的数据对象
     */
    private String defaultVisitorParams;

    /**
     * 是否加载本地数据
     */
    private boolean isLoadLocal;

    /**
     * 是否禁止上拉
     */
    private boolean forbidUp;

    /**
     * 是否禁止下拉
     */
    private boolean forbidDown;

    /**
     * 默认图片的标识
     */
    private int defaultImgId;

    /**
     * 自定义排序字段
     */
    private String customSortFields;
    /**
     * 上拉是否刷新页面
     */
    private boolean isPageRefresh;

    /**
     * 刷新的时候 是否重新加载数据
     * @return
     */
    public boolean isRefreshReload() {
        return isRefreshReload;
    }

    /**
     * 刷新的时候 是否重新加载数据
     * @param refreshReload
     */
    public void setRefreshReload(boolean refreshReload) {
        isRefreshReload = refreshReload;
    }

    /**
     * 是否加载本地数据
     * @return
     */
    public boolean isLoadLocal() {
        return isLoadLocal;
    }

    /**
     * 是否加载本地数据
     * @param loadLocal
     */
    public void setLoadLocal(boolean loadLocal) {
        isLoadLocal = loadLocal;
    }

    /**
     * 设置默认图片的标识
     * @param defaultImgId
     */
    public void setDefaultImgId(int defaultImgId) {
        this.defaultImgId = defaultImgId;
    }

    /**
     * 默认图片的标识
     * @return
     */
    public int getDefaultImgId() {
        return defaultImgId;
    }

    /**
     * 是否禁止下拉
     * @return
     */
    public boolean isForbidDown() {
        return forbidDown;
    }

    /**
     * 是否禁止下拉
     * @param forbidDown
     */
    public void setForbidDown(boolean forbidDown) {
        this.forbidDown = forbidDown;
    }

    /**
     * 是否禁止上拉
     */
    public boolean isForbidUp() {
        return forbidUp;
    }

    /**
     * 是否禁止上拉
     */
    public void setForbidUp(boolean forbidUp) {
        this.forbidUp = forbidUp;
    }

    /**
     * 与服务端交互的数据对象
     */
    public String getDefaultVisitorParams() {
        return defaultVisitorParams;
    }

    /**
     * 设置与服务端交互的数据对象
     * @param defaultVisitorParams
     */
    public void setDefaultVisitorParams(String defaultVisitorParams) {
        this.defaultVisitorParams = defaultVisitorParams;
    }

    /**
     * R的类型
     */
    public Class<?> getrClass() {
        return rClass;
    }

    /**
     * 设置R的类型
     * @param rClass
     */
    public void setrClass(Class<?> rClass) {
        this.rClass = rClass;
    }

    /**
     * 字段的名称
     * @return
     */
    public String[] getFieldNames() {
        return fieldNames;
    }

    /**
     * 设置字段的名称
     * @param fieldNames
     */
    public void setFieldNames(String[] fieldNames) {
        this.fieldNames = fieldNames;
    }

    /**
     * 设置item布局文件中组件的id前缀
     * @param viewIdPrefix
     */
    public void setViewIdPrefix(String viewIdPrefix) {
        this.viewIdPrefix = viewIdPrefix;
    }

    /**
     * item布局文件中组件的id前缀
     * @return
     */
    public String getViewIdPrefix() {
        return viewIdPrefix;
    }

    /**
     * 获取item的布局标识
     * @return
     */
    public int getItemLayoutId() {
        return itemLayoutId;
    }

    /**
     * 访问的服务端路径
     * @return
     */
    public String getServiceUrl() {
        return serviceUrl;
    }

    /**
     * 设置item的布局标识
     * @param itemLayoutId
     */
    public void setItemLayoutId(int itemLayoutId) {
        this.itemLayoutId = itemLayoutId;
    }

    /**
     * 设置服务端地址
     * @param serviceUrl
     */
    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    /**
     *  排序字段名称
     * @return
     */
    public String getSortFieldName() {
        return sortFieldName;
    }

    /**
     * 设置排序字段名称
     * @param sortFieldName
     */
    public void setSortFieldName(String sortFieldName) {
        this.sortFieldName = sortFieldName;
    }


    public String getCustomSortFields() {
        return customSortFields;
    }

    public void setCustomSortFields(String customSortFields) {
        this.customSortFields = customSortFields;
    }

    public boolean isPageRefresh() {
        return isPageRefresh;
    }

    public void setPageRefresh(boolean pageRefresh) {
        isPageRefresh = pageRefresh;
    }
}
