package com.example.twinkle.activity;

import android.content.Context;
import android.os.Bundle;
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
import com.example.twinkle.enity.Song;

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

public class ShareSongActivity extends AppCompatActivity {

    private Song song;
    private EditText share_song_edittext;
    private String connecturl;
    private String currentUserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_song_layout);
        song=(Song) getIntent().getSerializableExtra("song");
        connecturl=getIntent().getStringExtra("connecturl");
        currentUserID=getIntent().getStringExtra("currentUserID");
        ImageView song_cover =findViewById(R.id.song_cover);
        TextView song_name_title =findViewById(R.id.song_name_title);
        song_name_title.setText(song.getSongName());
        TextView singer_title =findViewById(R.id.singer_title);
        String singer_title_str="歌手："+song.getSingerName();
        singer_title.setText(singer_title_str);
        song_cover.setImageResource(song.getSongImageId());
        ImageView share_songlist_back=findViewById(R.id.back);
        share_songlist_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
            }
        });
        TextView share_songlist_btn=findViewById(R.id.share_btn);
        share_songlist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                sharesong(share_song_edittext.getText().toString());
                Toast.makeText(ShareSongActivity.this,"已分享到动态",Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
            }
        });
        share_song_edittext=findViewById(R.id.share_song_comment_eidtbox);
        share_song_edittext.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        share_song_edittext.setSingleLine(false);
        share_song_edittext.setHorizontallyScrolling(false);
        share_song_edittext.setFocusable(true);
        share_song_edittext.setFocusableInTouchMode(true);
        share_song_edittext.setImeOptions(android.view.inputmethod.EditorInfo.IME_ACTION_NONE);
        share_song_edittext.setSelection(share_song_edittext.getText().length());
        share_song_edittext.addTextChangedListener(new TextWatcher() {
            String tmp;
            int cursor;
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                int line = share_song_edittext.getLineCount();
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
                int line = share_song_edittext.getLineCount();
                if (line > 3){
                    String str = s.toString();
                    tmp = str.substring(0, cursor) + str.substring(cursor + 1);
                    share_song_edittext.setText(tmp);
                    share_song_edittext.setSelection(cursor);
                }
            }
        });
    }
    private void sharesong(String text){
        String url =connecturl+"/createNews";
        HashMap<String,String> paramsMap=new HashMap<>();
        paramsMap.put("userid",currentUserID);
        paramsMap.put("type","单曲");
        paramsMap.put("contentid",song.getSongID());
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
    //关闭软键盘
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && this.getCurrentFocus() != null) {
            if (this.getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
