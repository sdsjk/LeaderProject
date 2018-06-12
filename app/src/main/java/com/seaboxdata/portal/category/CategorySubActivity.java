package com.seaboxdata.portal.category;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.linewell.core.view.AutoFixViewGroup;
import com.linewell.imageloader.UniversalImageLoader;
import com.linewell.innochina.core.entity.params.base.BaseParams;
import com.seaboxdata.portal.R;
import com.seaboxdata.portal.bean.ServiceCategoryDTO;
import com.seaboxdata.portal.common.CommonActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 首页应用的分类列表
 */
public class CategorySubActivity extends CommonActivity {

    private boolean mShowEdit = false;

    @BindView(R.id.app_manager_save)
    TextView app_manager_save;

    @BindView(R.id.app_manager_canale)
    ImageView mRightMenuBT;
    private boolean mIsEdit = false;

    private List<ServiceCategoryDTO> list = new ArrayList<>();

    public static void startAction(Activity activity, boolean isEdit) {
        Intent intent = new Intent(activity, CategorySubActivity.class);
        intent.putExtra(KEY_DATA, isEdit);
        activity.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_sub);
        setCenterTitle("应用管理");

        mShowEdit = getIntent().getBooleanExtra(KEY_DATA, false);

        initView();
        bindView();
        initData();
    }

    private void initView() {
//        mRightMenuBT.setVisibility(View.VISIBLE);
//
//        if (mShowEdit) {
//            mRightMenuBT.setText("完成");
//            mIsEdit = true;
//        } else {
//            mRightMenuBT.setText("编辑");
//        }

    }

    private void bindView() {

        mRightMenuBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        app_manager_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存配置
                renderCategory(list, false);
            }
        });



    }

    private void initData() {
        getAllCategory();
    }

    /**
     * 获取全部分类
     */
    private void getAllCategory() {
        BaseParams params = new BaseParams();
//        AppHttpUtils.postJson(mCommonContext, InnochinaServiceConfig.CATEGORY_SERVICES.ALL_CATEGORY_LIST, params,
//                new AppHttpResultHandler<Object>() {
//                    @Override
//                    public void onSuccess(Object result, JsonObject allResult) {
//
//                        List<ServiceCategoryDTO> list = GsonUtil.jsonToBean(allResult.get("row").toString(), new TypeToken<List<ServiceCategoryDTO>>() {
//                        }.getType());
//                        renderCategory(list);
//                    }
//
//                    @Override
//                    public boolean onFail(JsonObject allResult) {
//                        return false;
//                    }
//                });

        List<ServiceCategoryDTO> sublist1 = new ArrayList<>();
        ServiceCategoryDTO subServiceCategoryDTO1 = new ServiceCategoryDTO("城市交通", false);
        sublist1.add(subServiceCategoryDTO1);
        ServiceCategoryDTO subServiceCategoryDTO2 = new ServiceCategoryDTO("经济运行", true);
        sublist1.add(subServiceCategoryDTO2);
        ServiceCategoryDTO subServiceCategoryDTO3 = new ServiceCategoryDTO("城市生命线", true);
        sublist1.add(subServiceCategoryDTO3);
        ServiceCategoryDTO subServiceCategoryDTO4 = new ServiceCategoryDTO("人群聚集", false);
        sublist1.add(subServiceCategoryDTO4);
        ServiceCategoryDTO subServiceCategoryDTO5 = new ServiceCategoryDTO("一带一路",true);
        sublist1.add(subServiceCategoryDTO5);

        ServiceCategoryDTO serviceCategoryDTO1 = new ServiceCategoryDTO("主题应用", sublist1);
        list.add(serviceCategoryDTO1);
        /******************第二条数据****************************/
        List<ServiceCategoryDTO> sublist2 = new ArrayList<>();
        ServiceCategoryDTO subServiceCategoryDTO21 = new ServiceCategoryDTO("社会保障", true);
        sublist2.add(subServiceCategoryDTO21);
        ServiceCategoryDTO subServiceCategoryDTO22 = new ServiceCategoryDTO("数字化城管", true);
        sublist2.add(subServiceCategoryDTO22);

        ServiceCategoryDTO serviceCategoryDTO2 = new ServiceCategoryDTO("部门应用", sublist2);
        list.add(serviceCategoryDTO2);

        /******************第三条数据****************************/
        List<ServiceCategoryDTO> sublist3 = new ArrayList<>();
        ServiceCategoryDTO subServiceCategoryDTO31 = new ServiceCategoryDTO("灾害预报", false);
        sublist3.add(subServiceCategoryDTO31);

        ServiceCategoryDTO serviceCategoryDTO3 = new ServiceCategoryDTO("应用大数据", sublist3);
        list.add(serviceCategoryDTO3);

        renderCategory(list, mShowEdit);

    }

    /**
     * 渲染分类
     */
    private void renderCategory(List<ServiceCategoryDTO> list, boolean showEdit) {
        //父LL
        LinearLayout tagsListView = (LinearLayout) findViewById(R.id.layout_tag_list);
        if (tagsListView == null) {
            return;
        }
        tagsListView.removeAllViews();
        //
        LayoutInflater layoutInflater = LayoutInflater.from(mCommonContext);

        for (final ServiceCategoryDTO dto : list) {
            // 生成整个分类  一级分类
            View view = layoutInflater.inflate(R.layout.item_category_sub, tagsListView, false);
            final TextView categoryname = (TextView) view.findViewById(R.id.category_name);
            //分类图标赋值
            ImageView categoryCIV = (ImageView) view.findViewById(R.id.category_civ);
            categoryname.setText(dto.getCategoryName());
            if (TextUtils.isEmpty(dto.getCategoryIconUrl())) {
                categoryCIV.setImageResource(R.drawable.transparent);
            } else {
                UniversalImageLoader.displayImage(dto.getCategoryIconUrl(), categoryCIV);
            }
            // 自动换行控件
            AutoFixViewGroup tagsView = (AutoFixViewGroup) view.findViewById(R.id.tags);
            tagsView.removeAllViews();
            List<ServiceCategoryDTO> tagList = dto.getSubCategoryList();

            //二级分类
            //添加全部分类
//            View allItemView = layoutInflater.inflate(R.layout.item_category_tag, tagsView, false);
//            TextView allTag = (TextView) allItemView.findViewById(R.id.tv_tag);
//            allTag.setText("全部");
//
//            //标签的点击
//            allTag.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    startCategoryList(dto.getResourceCategoryId(), dto.getCategoryName(), dto.getResourceCategoryId(), "全部");
//                }
//            });
//            tagsView.addView(allItemView);
            //遍历二级分类
            if (tagList != null && tagList.size() > 0) {
                for (final ServiceCategoryDTO tagDTO : tagList) {
                    if (tagDTO == null) {
                        continue;
                    }
                    // 生成标签
                    View itemView = layoutInflater.inflate(R.layout.item_category_tag, tagsView, false);
                    final CheckBox tag = (CheckBox) itemView.findViewById(R.id.tv_tag);
                    tag.setText(tagDTO.getCategoryName());
//                    tag.setTag(tagDTO);

//                    final View deleteView = itemView.findViewById(R.id.front_fit);
                    if (tagDTO.isChoose()) {
                        tag.setTextColor(Color.parseColor("#496cef"));
                        tag.setChecked(true);
//                        deleteView.setVisibility(View.VISIBLE);
                    } else {
//                        deleteView.setVisibility(View.GONE);
                        tag.setTextColor(Color.parseColor("#272727"));
                        tag.setChecked(false);
                    }
                    tag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                                tag.setTextColor(Color.parseColor("#496cef"));
                            }else {
                                tag.setTextColor(Color.parseColor("#272727"));
                            }
                        }

                    });
//                    deleteView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            deleteView.setVisibility(View.GONE);
//                            // TODO 删除
//                        }
//                    });

                    //标签的点击
//                    tag.setOnClickListener(new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
////                            deleteView.setVisibility(View.VISIBLE);
//                            // TODO 添加
//                            startCategoryList(tagDTO.getPid(), dto.getParentCategoryName(), tagDTO.getResourceCategoryId(), tagDTO.getCategoryName());
//                        }
//                    });


                    tagsView.addView(itemView);
                }
            }
            tagsListView.addView(view);
        }
    }

    /**
     * 打开分类列表
     *
     * @param categoryId
     * @param pName
     */
    private void startCategoryList(String categoryId, String pName, String categorySubId, String categorySubName) {



    }
}
