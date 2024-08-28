package com.messenger.views.checkbox;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CompoundButton;

import androidx.appcompat.widget.AppCompatRadioButton;

public class OneCheck extends AppCompatRadioButton {

    public OneCheck(Context context) {
        super(context);
        init();
    }


    public OneCheck(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OneCheck(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    OneCheckPro.change(OneCheck.this);
                }
            }
        });
    }
}
