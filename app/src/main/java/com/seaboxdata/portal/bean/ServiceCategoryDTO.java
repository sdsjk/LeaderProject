package com.seaboxdata.portal.bean;

import java.util.List;

/**
 * Created by zjianning on 2018/5/14.
 */

public class ServiceCategoryDTO {

    private String categoryName;

    private String categoryIconUrl;

    private String resourceCategoryId;

    private List<ServiceCategoryDTO> subCategoryList;

    private String pid;

    private String parentCategoryName;

    private boolean isChoose;

    public ServiceCategoryDTO(String categoryName,
                              List<ServiceCategoryDTO> subCategoryList) {

        this.categoryName = categoryName;
        this.subCategoryList = subCategoryList;

    }

    public ServiceCategoryDTO(String categoryName, boolean isChoose) {

        this.categoryName = categoryName;
        this.isChoose = isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryIconUrl() {
        return categoryIconUrl;
    }

    public List<ServiceCategoryDTO> getSubCategoryList() {
        return subCategoryList;
    }

    public String getResourceCategoryId() {
        return resourceCategoryId;
    }

    public String getPid() {
        return pid;
    }

    public String getParentCategoryName() {
        return parentCategoryName;
    }
}
