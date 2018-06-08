package com.linewell.core.params;

import java.io.File;
import java.util.Map;

/**
 * 上传文件的参数对象
 * Created by caicai on 2016-07-27.
 */
public class UploadFilesParams {

    /**
     * 表单
     */
    private Map<String,String> formMap;

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    /**
     * 头部
     */
    private Map<String,String> headerMap;
    /**
     * 文件列表
     */
    private Map<String,File> fileMap;

    public boolean isHasFiles() {
        return hasFiles;
    }

    public void setHasFiles(boolean hasFiles) {
        this.hasFiles = hasFiles;
    }

    /**
     * 是否存在图片上传
     */
    private boolean hasFiles;

    public Map<String, File> getFileMap() {
        return fileMap;
    }

    public void setFileMap(Map<String, File> fileMap) {
        this.fileMap = fileMap;
    }

    public Map<String, String> getFormMap() {
        return formMap;
    }

    public void setFormMap(Map<String, String> formMap) {
        this.formMap = formMap;
    }


}
