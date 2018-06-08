package com.seaboxdata.portal.common.input;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.linewell.core.view.FontIconText;
import com.linewell.utils.SystemUtils;
import com.linewell.utils.ValidUtils;
import com.seaboxdata.portal.R;

import java.util.regex.Pattern;

/**
 * 输入框的封装
 * @author zjianning@linewell.com
 * @since 2016/10/8
 */
public class BaseInputLinearLayout extends LinearLayout {


    //密码输入框
    protected EditText mInputET;

    //清除密码按钮
    protected FontIconText mIconClearInput;

    /**
     * 构造函数
     * @param context
     */
    public BaseInputLinearLayout(Context context) {
        super(context);

    }

    /**
     * 构造函数
     * @param context
     * @param attrs
     */
    public BaseInputLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        //将布局加载到自定义的控件中
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_view_input_linearlayout, this);
        initView(context, attrs);
    }

    protected void initView(Context context, AttributeSet attrs) {

        mIconClearInput =(FontIconText) findViewById(R.id.view_input_clear_icon);
        mIconClearInput.setOnClickListener(onClickListener);

        mInputET =(EditText) findViewById(R.id.view_input_icon_input_et);
        mInputET.addTextChangedListener(textWatcher);
        mInputET.setOnFocusChangeListener(focusListener);

        // 自定义的属性值
        TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.UInput);

        // 默认提示语
        String hint = values.getString(R.styleable.UInput_hint);
        mInputET.setHint(hint);

        float textSize = values.getDimension(R.styleable.UInput_textSize, 0);
        if (textSize == 0) {
            textSize = 15;
            mInputET.setTextSize(textSize);
        }else{
            mInputET.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }

        mInputET.setMaxWidth(SystemUtils.dip2px(context, values.getInt(R.styleable.UInput_maxWidth, 200)));

        //输入字数
        int maxLength=values.getInt(R.styleable.UInput_maxLength,11);
        mInputET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

        int color  = values.getColor(R.styleable.UInput_textColor, context.getResources().getColor(R.color.textDark));
        mInputET.setTextColor(color);

        int textColorHint = values.getColor(R.styleable.UInput_textColorHint, context.getResources().getColor(R.color.textHintColor));
        mInputET.setHintTextColor(textColorHint);
    }

    /**
     * 获取密码输入框对象
     * @return
     */
    public EditText getEditText(){

        return mInputET;
    }

    /**
     * 获取输入框的值
     * @return
     */
    public String getEditTextContent(){
        EditText editText = this.getEditText();
        if (editText!=null) {
            return editText.getText().toString().trim();
        } else {
            return "";
        }
    }

    /**
     * 设置提示文字
     * @param hint
     */
    public void setHint(int hint){
        mInputET.setHint(hint);
    }


    /**
     * 清除密码的事件
     */
    protected OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mInputET.setText("");
            mInputET.requestFocus();
        }
    };

    /**
     * 监听输入框的事件
     */
    protected TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {

                if (mInputET.hasFocus()) {
                    mIconClearInput.setVisibility(View.VISIBLE);
                } else {
                    mIconClearInput.setVisibility(View.GONE);
                }

            } else {
                mIconClearInput.setVisibility(View.GONE);
            }
        }
    };

    /**
     * 监听焦点事件
     */
    protected OnFocusChangeListener focusListener =new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            focusChange(hasFocus);
        }
    };

    public void focusChange(boolean hasFocus) {
        if(hasFocus){
            //有焦点有内容则删除可用
            if(mInputET.getText()!=null&&!mInputET.getText().toString().equals("")){
                mIconClearInput.setVisibility(View.VISIBLE);
            }else{
                mIconClearInput.setVisibility(View.GONE);
            }
        }else{
            mIconClearInput.setVisibility(View.GONE);
        }
    }

    /**
     * 获取手机号，规则正确才会返回
     * @return
     */
    public String getPhoneValidStr() {
        String phone = mInputET.getText().toString();
        if (!TextUtils.isEmpty(phone) && ValidUtils.isPhoneValid(phone)) {
            return phone.trim();
        } else {
            return "";
        }
    }

    public FontIconText getIconClearInput() {
        return mIconClearInput;
    }

    /**
     * 获取身份证号，规则正确才会返回
     * @return
     */
    public String getCardIDValidStr() {

        String cardID = mInputET.getText().toString();

        if (!TextUtils.isEmpty(cardID)) {

            Pattern p = Pattern.compile("^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X)$");
            if(p.matcher(cardID).matches()) {
                return cardID;
            }
        }
        return "";



    }

}
