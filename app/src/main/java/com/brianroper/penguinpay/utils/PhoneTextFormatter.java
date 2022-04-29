package com.brianroper.penguinpay.utils;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

/**
 * https://stackoverflow.com/a/56090054/5124891
 *
 * slightly modified to take pattern through a setter instead of the constructor
 * allowing us to update the pattern dynamically when the country spinner is updated
 */
public class PhoneTextFormatter implements TextWatcher {

    private final String TAG = this.getClass().getSimpleName();

    private EditText mEditText;

    private String mPattern;

    public PhoneTextFormatter(EditText editText) {
        mEditText = editText;
    }

    public void setPattern(String pattern) {
        this.mPattern = pattern;
        mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(pattern.length())});
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        StringBuilder phone = new StringBuilder(s);

        Log.d(TAG, "join");

        if (count > 0 && !isValid(phone.toString())) {
            for (int i = 0; i < phone.length(); i++) {
                Log.d(TAG, String.format("%s", phone));
                char c = mPattern.charAt(i);

                if ((c != '#') && (c != phone.charAt(i))) {
                    phone.insert(i, c);
                }
            }

            mEditText.setText(phone);
            mEditText.setSelection(mEditText.getText().length());
        }
    }

    @Override
    public void afterTextChanged(Editable editable) { }

    private boolean isValid(String phone)
    {
        for (int i = 0; i < phone.length(); i++) {
            char c = mPattern.charAt(i);

            if (c == '#') continue;

            if (c != phone.charAt(i)) {
                return false;
            }
        }

        return true;
    }
}
