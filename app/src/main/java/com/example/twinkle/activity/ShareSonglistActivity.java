package com.example.twinkle.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.TypeReference;
import com.example.twinkle.R;
import com.example.twinkle.enity.SongList;

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

public class ShareSonglistActivity extends AppCompatActivity {

    private SongList songlist;
    private EditText share_songlist_edittext;
    private String connecturl;
    private String currentUserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_songlist_layout);
        songlist=(SongList)getIntent().getSerializableExtra("songlist");
        connecturl=getIntent().getStringExtra("connecturl");
        currentUserID=getIntent().getStringExtra("currentUserID");
        ImageView songlist_cover =findViewById(R.id.songlist_cover);
        TextView songlist_name_title =findViewById(R.id.songlist_name_title);
        songlist_name_title.setText(songlist.getSongListName());
        TextView songlist_creator_title =findViewById(R.id.songlist_creator_title);
        String songlist_creator_string="创建者："+songlist.getUserName();
        songlist_creator_title.setText(songlist_creator_string);
        songlist_cover.setImageResource(songlist.getSongListImageId());
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
                sharesonglist(share_songlist_edittext.getText().toString());
                Toast.makeText(ShareSonglistActivity.this,"已分享到动态",Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
            }
        });
        share_songlist_edittext=findViewById(R.id.share_songlist_comment_eidtbox);
        share_songlist_edittext.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        share_songlist_edittext.setSingleLine(false);
        share_songlist_edittext.setHorizontallyScrolling(false);
        share_songlist_edittext.setFocusable(true);
        share_songlist_edittext.setFocusableInTouchMode(true);
        share_songlist_edittext.setImeOptions(android.view.inputmethod.EditorInfo.IME_ACTION_NONE);
        share_songlist_edittext.setSelection(share_songlist_edittext.getText().length());
        share_songlist_edittext.addTextChangedListener(new TextWatcher() {
            String tmp;
            int cursor;
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                int line = share_songlist_edittext.getLineCount();
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
                int line = share_songlist_edittext.getLineCount();
                if (line > 3){
                    String str = s.toString();
                    tmp = str.substring(0, cursor) + str.substring(cursor + 1);
                    share_songlist_edittext.setText(tmp);
                    share_songlist_edittext.setSelection(cursor);
                }
            }
        });
    }
    private void sharesonglist(String text){
        String url =connecturl+"/createNews";
        HashMap<String,String> paramsMap=new HashMap<>();
        paramsMap.put("userid",currentUserID);
        paramsMap.put("type","歌单");
        paramsMap.put("contentid",songlist.getSongListID());
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
