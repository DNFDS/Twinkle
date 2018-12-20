package com.example.twinkle;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ShareNewsActivity extends AppCompatActivity {

    private News sharedNews;
    private EditText shareNewsEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_news_layout);
        sharedNews =(News)getIntent().getSerializableExtra("SharedNews");
        shareNewsEditText=findViewById(R.id.share_news_eidtbox);
        shareNewsEditText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        shareNewsEditText.setSingleLine(false);
        shareNewsEditText.setHorizontallyScrolling(false);
        shareNewsEditText.setFocusable(true);
        shareNewsEditText.setFocusableInTouchMode(true);
        shareNewsEditText.setImeOptions(android.view.inputmethod.EditorInfo.IME_ACTION_NONE);
        final InputMethodManager inputManager = (InputMethodManager)shareNewsEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean handler =new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                shareNewsEditText.requestFocus();
                inputManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
                inputManager.showSoftInput(shareNewsEditText, 0);
            }
        },300); // 延时0.3秒
        shareNewsEditText.setSelection(shareNewsEditText.getText().length());
        shareNewsEditText.addTextChangedListener(new TextWatcher() {
            String tmp;
            int cursor;
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                int line = shareNewsEditText.getLineCount();
                if (line > 4) {
                    if (before > 0 && start == 0) {
                        if (s.toString().equals(tmp)) {
                            // setText触发递归TextWatcher
                            cursor--;
                        } else {
                            // 手动移动光标为0
                            cursor = count - 1;
                        }
                    } else {
                        cursor = start + count - 1;
                    }
                }
            }
            @Override public void afterTextChanged(Editable s) {
                // 限制可输入行数
                int line = shareNewsEditText.getLineCount();
                if (line > 3){
                    String str = s.toString();
                    tmp = str.substring(0, cursor) + str.substring(cursor + 1);
                    shareNewsEditText.setText(tmp);
                    shareNewsEditText.setSelection(cursor);
                }
            }
        });
        ImageView share_news_back=findViewById(R.id.back);
        TextView share_news_btn=findViewById(R.id.share_btn);
        //退回按钮点击事件
        share_news_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
            }
        });
        //转发按钮点击事件
        share_news_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ShareNewsActivity.this,"已转发到动态",Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
    }
}
