package com.linewell.core.view;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * @author lyixin
 * @since 2016/7/25.
 */
public class EditLimitTextWatcher implements TextWatcher {

    private int maxLen = 0;
    private EditText editText = null;

    public EditLimitTextWatcher(int maxLen, EditText editText) {
        this.maxLen = maxLen;
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before,
                              int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        setMaxLength(maxLen, editText);
    }

    /**
     * EditText设置最大长度
     *
     * @param maxLen
     *            最大长度
     * @param editText
     *            控件
     */
    private void setMaxLength(int maxLen, EditText editText) {
        Editable editable = editText.getText();
        int len = editable.length();

        if (len >maxLen) {
            int selEndIndex = Selection.getSelectionEnd(editable);
            String str = editable.toString();
            // 截取新字符串
            String newStr = str.substring(0, maxLen);
            editText.setText(newStr);
            editable = editText.getText();

            // 新字符串的长度
            int newLen = editable.length();
            // 旧光标位置超过字符串长度
            if (selEndIndex > newLen) {
                selEndIndex = editable.length();
            }
            // 设置新光标所在的位置
            Selection.setSelection(editable, selEndIndex);
        }else{
//            Selection.setSelection(editable, len);
        }
    }

}
