package com.seaboxdata.portal.common;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seaboxdata.portal.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 自定义弹出框
 */
public class CustomDialog extends Dialog {

    public CustomDialog(Context context) {
        super(context);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String message;
        private int gravity;
        private float contentSize;
        private String positiveButtonText;
        private String negativeButtonText;
        private View contentView;
        private int layoutId = R.layout.pop_dialog;
        private View layout;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public View getLayout() {
            return  this.layout;
        }

        public Builder setLayoutId(int layoutId) {
            this.layoutId = layoutId;
            return this;
        }

        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder setContentSize(float contentSize) {
            this.contentSize = contentSize;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * Set the Dialog message from resource
         *
         * @param message
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public CustomDialog create() {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CustomDialog dialog = new CustomDialog(context, R.style.curDialog);
            layout = inflater.inflate(layoutId, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            // set the dialog title
            if (TextUtils.isEmpty(title)) {//无标题时隐藏布局
                layout.findViewById(R.id.pop_dialog_title).setVisibility(View.GONE);
            } else {
                ((TextView) layout.findViewById(R.id.pop_dialog_title)).setText(title);
            }


            List<View> buttons = new ArrayList<>();

            // set the confirm button
            if (positiveButtonText != null) {
                Button button = ((Button) layout.findViewById(R.id.pop_dialog_ok));
                button.setText(positiveButtonText);
                buttons.add(button);
                if (positiveButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.pop_dialog_ok))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                    dialog.dismiss();
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.pop_dialog_ok).setVisibility(
                        View.GONE);
//                ((TextView) layout.findViewById(R.id.pop_dialog_title)).setTextAppearance(context, R.style.PopDialogContentText_single_withoutButton);

            }
            // set the cancel button
            if (negativeButtonText != null) {
                Button button = ((Button) layout.findViewById(R.id.pop_dialog_cancle));
                button.setText(negativeButtonText);
                buttons.add(button);
                if (negativeButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.pop_dialog_cancle))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                    dialog.dismiss();
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.pop_dialog_cancle).setVisibility(
                        View.GONE);
//                ((TextView) layout.findViewById(R.id.pop_dialog_title)).setTextAppearance(context, R.style.PopDialogContentText_single_withoutButton);
            }

//            if (buttons.size() == 1) {
//                ((Button) buttons.get(0)).setBackgroundResource(R.drawable.selector_pop_box_single_button);
//            }

            // set the content message
            if (!TextUtils.isEmpty(message)) {
                TextView contentTV = (TextView) layout.findViewById(R.id.pop_dialog_content);
                contentTV.setText(message);

                // TODO 行数小于1 居中 大于的时候默认

                if (contentSize > 0) {
                    contentTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, contentSize);
                }


                if (gravity > 0) {
                    contentTV.setGravity(gravity);
                } else {
                    if (contentTV.getLineCount() == 1) {
                        contentTV.setGravity(View.TEXT_ALIGNMENT_CENTER);
                    }
                }

            } else if (TextUtils.isEmpty(message)) {//无信息是隐藏布局
                layout.findViewById(R.id.pop_dialog_content).setVisibility(View.GONE);
            } else if (contentView != null) {
                // if no message set
                // add the contentView to the dialog body
                ((LinearLayout) layout.findViewById(R.id.pop_dialog_content_ll))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.pop_dialog_content_ll))
                        .addView(contentView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
            }
            dialog.setContentView(layout);
            return dialog;
        }

    }

    /**
     * 确认弹窗
     *
     * @param context
     * @param msg
     * @param dialogOperation
     * @return
     */
    public static CustomDialog.Builder initConfirmDialog(Context context, final DialogOperation dialogOperation, String... msg) {
        CustomDialog.Builder builder = new CustomDialog.Builder(context);
        String okBtnName = "确定";
        String cancelBtnName = "取消";
        if (msg != null && msg.length > 0) {
            if (msg.length >= 1) {
                builder.setTitle(msg[0]);
            }
            if (msg.length >= 2) {
                builder.setMessage(msg[1]);
            }
            if (msg.length >= 3) {
                okBtnName = msg[2];
            }
            if (msg.length >= 4) {
                cancelBtnName = msg[3];
            }
        }

        builder.setNegativeButton(cancelBtnName, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogOperation.negativeClick(dialog, which);
            }
        });
        builder.setPositiveButton(okBtnName, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogOperation.positiveClick(dialog, which);
            }
        });
        return builder;
    }

    /**
     * 提示弹窗
     *
     * @param context
     * @param btnName
     * @param msg
     * @return
     */
    public static CustomDialog.Builder initPromptDialog(Context context, String btnName, String... msg) {
        if (TextUtils.isEmpty(btnName)) {
            btnName = "确定";
        }
        CustomDialog.Builder builder = new CustomDialog.Builder(context);
        if (msg != null && msg.length > 0) {
            if (msg.length >= 1) {
                builder.setTitle(msg[0]);
            }
            if (msg.length == 2) {
                builder.setMessage(msg[1]);
            }
        }
        builder.setPositiveButton(btnName, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder;
    }

    /**
     * 弹窗操作类
     */
    public static abstract class DialogOperation {

        protected void negativeClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }

        protected abstract void positiveClick(DialogInterface dialog, int which);
    }


}
