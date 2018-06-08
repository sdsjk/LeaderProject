package com.linewell.pulllistview;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 列表对应的adapter
 * Created by zjianning on 2016/7/19.
 */
public class RecyclerViewFragmentAdapter extends BaseQuickAdapter<JsonObject> {

    private String mViewIdPrefix;

    private String[] mFieldNames;

    private Class<?> mRClass;

    private RecyclerViewListener mRecyclerViewListener;

    private int mDefaultImgId;

    /**
     *
     * @param params
     * @param data
     */
    public RecyclerViewFragmentAdapter(ListParams params, List<JsonObject> data, RecyclerViewListener recyclerViewListener) {
        super(params.getItemLayoutId(), data);
        mViewIdPrefix = params.getViewIdPrefix();
        mFieldNames = params.getFieldNames();
        mRClass = params.getrClass();
        mRecyclerViewListener = recyclerViewListener;
        mDefaultImgId = params.getDefaultImgId();
    }

    @Override
    public void addData(List<JsonObject> items) {

        if (items != null && items.size() > 0) {
            int oldSize = this.mData.size();
            if (oldSize >= 0) {
                this.mData.addAll(0, items);

                notifyItemRangeInserted(this.mData.size() - items.size(), items.size());
            }
        }

//        this.notifyDataSetChanged();



    }


    @Override
    protected void convert(BaseViewHolder baseViewHolder, JsonObject obj) {
//        if (mFieldNames != null && mFieldNames.length > 0) {
//            for(String fieldName : mFieldNames) {
//                // 当前要渲染的view的标识
//                int viewID = SystemUtils.getStringUnid(mRClass, mViewIdPrefix + fieldName);
//                if(viewID>0) {
//                    // 要渲染的值
//                    JsonElement jsonElement = obj.get(fieldName);
//                    String value = "";
//                    if (null != jsonElement && !TextUtils.isEmpty(jsonElement.toString()) && !jsonElement.toString().equals("null")) {
//                        value = obj.get(fieldName).getAsString();
//                    }
//                    // 前提的命名规则：view的控件名称要和属性的名称一致
//                    render(baseViewHolder, viewID, value);
//                }
//            }
//        } else {
//            // TODO
//        }

        if(null != mRecyclerViewListener) {
            mRecyclerViewListener.customRenderItem(baseViewHolder, obj);
            mRecyclerViewListener.customRenderItemAfter(baseViewHolder, obj);
        }

    }


    /**
     * 渲染
     * @param baseViewHolder
     * @param viewID
     * @param value
     */
    private void render(BaseViewHolder baseViewHolder, int viewID, String value){
//        try {
//
//            // 获取bean属性对应的view控件
//            View view = baseViewHolder.getView(viewID);
//            // 如果是textView控件
//            if (view instanceof TextView) {
//                baseViewHolder.setText(viewID, value);
//            } else if (view instanceof ImageView) {
//
//                // 图片为空时
//                if (TextUtils.isEmpty(value)) {
//                    // 默认图片不为空的时候
//                    if (mDefaultImgId != 0) {
//                        ((ImageView) view).setImageResource(mDefaultImgId);
//                    }
//
//                } else {
//                    UniversalImageLoader.displayImage(value, ((ImageView) view));
//                }
//
//            }
//
//        } catch (IllegalArgumentException e) {
//        }
    }

    public List<JsonObject> getData(){
        return this.mData;
    }

    /**
     * 删除列表item数据
     * @param position
     */
    public void deleteItem(int position) {

        this.mData.remove(position);


        notifyItemRemoved(position);
//        notifyDataSetChanged();
    }

    public void deleteItem(List<Integer> positionList) {

        // TODO 需要调整

        if (positionList == null || positionList.size() == 0) {
            return;
        }

        if (positionList.size() == this.mData.size()) {
            this.mData = new ArrayList<>();
            mRecyclerViewListener.setNewData(this.mData);
            return;
        }

        for (int size = positionList.size(), i = size - 1; i >= 0; i--) {
            for (int mSize = mData.size(), j = mSize - 1; j >=0; j--) {
                if (positionList.get(i) == j) {
                    mData.remove(j);
                    break;
                }
            }
        }

//        int mDataIndex = 0;
//        Iterator<JsonObject> iterator = this.mData.iterator();
//        for (int i = 0, size = positionList.size(); i < size; i++) {
//            while (iterator.hasNext()) {
//                iterator.next();
//                if (positionList.get(i) == mDataIndex) {
//                    // 找到下一个下标数据 并-1
//                    iterator.remove();
//                    if (i != size -1) {
//                        positionList.set(i + 1, positionList.get(i + 1) - 1);
//                    }
//                    break;
//                }
//                mDataIndex ++;
//            }
//
//        }

        mRecyclerViewListener.setNewData(this.mData);


    }

    /**
     * 增加列表item数据
     * @param jsonObject
     */
    public void insertItem(JsonObject jsonObject){
        this.mData.add(0, jsonObject);
        notifyDataSetChanged();
    }

    /**
     * 增加列表item数据
     * @param jsonObject
     */
    public void insertItemLast(JsonObject jsonObject){
        this.mData.add(jsonObject);
        notifyDataSetChanged();
    }

    /**
     * 更新item数据
     * @param index
     * @param jsonObject
     */
    public void updateItemData(int index, JsonObject jsonObject) {
        this.mData.set(index, jsonObject);
        notifyDataSetChanged();
    }

    /**
     * 更新
     */
    public void update() {
        notifyDataSetChanged();
    }

    public void setData(List<JsonObject> listData) {
        this.mData = listData;
        notifyItemRangeInserted(0, listData.size());
    }

    public void setLayoutResId(int resId){
        this.mLayoutResId = resId;
    }


    public void setmDate(List<JsonObject> listData) {
        this.mData = listData;
    }

}
