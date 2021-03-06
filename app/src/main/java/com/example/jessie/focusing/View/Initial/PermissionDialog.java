package com.example.jessie.focusing.View.Initial;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.example.jessie.focusing.R;

/**
 * @author : Yujie Lyu
 * @date : 13-12-2018
 * @time : 00:58
 */
public class PermissionDialog extends Dialog {
    private onClickListener onClickListener;

    PermissionDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_permission);
        TextView btnPermission = findViewById(R.id.btn_permission);
        btnPermission.setOnClickListener(view -> {
            if (onClickListener != null) {
                dismiss();
                onClickListener.onClick();
            }
        });
    }

    public void setOnClickListener(onClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface onClickListener {
        void onClick();
    }
}
