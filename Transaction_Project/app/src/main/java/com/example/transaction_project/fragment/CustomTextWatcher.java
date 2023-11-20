package com.example.transaction_project.fragment;

import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;

public class CustomTextWatcher implements TextWatcher {

    // 가격 입력 시, 천단위 콤마 만들기.

    private EditText editText;
    String strAmount = "";

    CustomTextWatcher(EditText et) {
        editText = et;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(!TextUtils.isEmpty(s.toString()) && !s.toString().equals(strAmount)) {
            strAmount = makeStringComma(s.toString().replace(",", ""));
            editText.setText(strAmount);
            Editable editable = editText.getText();
            Selection.setSelection(editable, strAmount.length());
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    protected String makeStringComma(String str) {    // 천단위 콤마설정.
        if (str.length() == 0) {
            return "";
        }
        long value = Long.parseLong(str);
        DecimalFormat format = new DecimalFormat("###,###");
        return format.format(value);
    }
}