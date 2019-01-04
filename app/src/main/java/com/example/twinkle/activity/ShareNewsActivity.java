package com.example.twinkle.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.TypeReference;
import com.example.twinkle.R;
import com.example.twinkle.enity.News;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ShareNewsActivity extends AppCompatActivity {

    private News sharedNews;
    private EditText shareNewsEditText;
    private String currentUserID;
    private String connecturl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_news_layout);
        sharedNews =(News)getIntent().getSerializableExtra("SharedNews");
        currentUserID=getIntent().getStringExtra("currentUserID");
        connecturl=getIntent().getStringExtra("connecturl");
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
                sharenews(shareNewsEditText.getText().toString());
                Toast.makeText(ShareNewsActivity.this,"已转发到动态",Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
            }
        });
    }
    private void sharenews(String text){
        String url =connecturl+"/forwardNews";
        HashMap<String,String> paramsMap=new HashMap<>();
        paramsMap.put("uid",currentUserID);
        paramsMap.put("nid",sharedNews.getNewsId());
        paramsMap.put("text",text);
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : paramsMap.keySet()) {
            //追加表单信息
            builder.add(key, paramsMap.get(key));
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody formBody=builder.build();
        Request request=new   Request.Builder().url(url).post(formBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call var1, IOException var) {
                Log.d("liangyue",var.toString());
            }

            @Override
            public void onResponse(Call var1, Response response) throws IOException {
                String responseStr = response.body().string();
                Map<String,Object> params= com.alibaba.fastjson.JSONObject.parseObject(responseStr,new TypeReference<Map<String, Object>>(){});
                Log.d("liangyue",(String)params.get("msg"));
            }
        });
    }
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
    }
}
