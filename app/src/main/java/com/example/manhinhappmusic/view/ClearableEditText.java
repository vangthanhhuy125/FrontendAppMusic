package com.example.manhinhappmusic.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.manhinhappmusic.R;

/**
 * TODO: document your custom view class.
 */
public class ClearableEditText extends FrameLayout {

    private EditText editText;
    private ImageButton btnClear;
    private TextWatcher textChangedListener;

    public ClearableEditText(Context context) {
        super(context);
        init(context);
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_clearable_edit_text, this, true);
        editText = findViewById(R.id.edit_text);
        btnClear = findViewById(R.id.btn_clear);

        btnClear.setOnClickListener(v -> {
            editText.setText("");
            btnClear.setVisibility(GONE);
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(textChangedListener != null)
                    textChangedListener.beforeTextChanged(s,start, count, after);
            }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(textChangedListener != null)
                    textChangedListener.onTextChanged(s,start, before, count);
                btnClear.setVisibility(s.length() > 0 ? VISIBLE : GONE);
            }
            @Override public void afterTextChanged(Editable s) {
                if(textChangedListener != null)
                    textChangedListener.afterTextChanged(s);
            }
        });
    }

    public Editable getText() {
        return editText.getText();
    }

    public void setHint(String hint) {
        editText.setHint(hint);
    }

    public EditText getEditText() {
        return editText;
    }

    public void addTextChangedListener(TextWatcher textChangedListener)
    {
        this.textChangedListener = textChangedListener;
    }

    public void setText(String text)
    {
        editText.setText(text);
        btnClear.setVisibility(text != null && !text.isEmpty() ? VISIBLE : GONE);
    }
}