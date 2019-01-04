package com.example.twinkle.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.twinkle.R;
import com.example.twinkle.serverenity.ServerUser;
import com.example.twinkle.enity.User;
import com.example.twinkle.fgactivity.MainActivity;

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

public class LoginActivity extends AppCompatActivity {

    private EditText idEditText;
    private EditText passwordEditText;
    private ImageView loginBtn;
    private TextView loginMsg;
    private String connecturl="http://129.211.118.50:8080/Android";
    private User loginUser=new User();
    private PopupWindow loginPopupWindow;
    private View loginPopupView;
    private Handler handler=null;
    private Runnable light;
    private Runnable dark;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.login_layout);
        initPopwindow();
        handler=new Handler();
        dark=new  Runnable(){
            @Override
            public void run() {
                //更新界面
                setBackgroundAlpha(0.7f);
            }
        };
        light=new  Runnable(){
            @Override
            public void run() {
                //更新界面
                setBackgroundAlpha(1.0f);
            }
        };
        idEditText=findViewById(R.id.id_eidtbox);
        idEditText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        idEditText.setSingleLine(true);
        idEditText.setHorizontallyScrolling(false);
        idEditText.setFocusable(true);
        idEditText.setFocusableInTouchMode(true);
        idEditText.setImeOptions(android.view.inputmethod.EditorInfo.IME_ACTION_NONE);
        idEditText.setSelection(idEditText.getText().length());
        passwordEditText=findViewById(R.id.password_eidtbox);
        passwordEditText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        passwordEditText.setSingleLine(true);
        passwordEditText.setHorizontallyScrolling(false);
        passwordEditText.setFocusable(true);
        passwordEditText.setFocusableInTouchMode(true);
        passwordEditText.setImeOptions(android.view.inputmethod.EditorInfo.IME_ACTION_NONE);
        passwordEditText.setSelection(passwordEditText.getText().length());
        loginBtn=findViewById(R.id.login_btn);
        loginBtn.getBackground().setAlpha(150);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idEditText.getText().toString().equals("")||passwordEditText.getText().toString().equals("")){
                    showloginpopupWindow("账号和密码不得为空");
                    setBackgroundAlpha(0.7f);
                }else{
                    hideKeyboard();
                    login(idEditText.getText().toString(),passwordEditText.getText().toString());
                }
            }
        });
    }
    private void login(String id,String password){
        String url =connecturl+"/login";
        HashMap<String,String> paramsMap=new HashMap<>();
        paramsMap.put("userid",id);
        paramsMap.put("pwd",password);
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
                boolean succ=(boolean)params.get("succ");
                ServerUser serverUser;
                if(succ==false){
                    Looper.prepare();
                    showloginpopupWindow("账号或密码错误，请重新输入");
                    handler.post(dark);
                    Looper.loop();
                }else {
                    serverUser= JSON.toJavaObject((JSONObject)params.get("user"),ServerUser.class);
                    loginUser.setUserID(serverUser.getUserid());
                    loginUser.setUserName(serverUser.getUsername());
                    loginUser.setUserImage(R.id.user_image);
                    loginUser.setIsVIP(serverUser.getIsvip());
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    intent.putExtra("loginUser",loginUser);
                    intent.putExtra("connecturl",connecturl);
                    startActivity(intent);
                    overridePendingTransition(R.anim.login_in, R.anim.fade_out);
                }
            }
        });
    }
    //初始化弹窗
    private void initPopwindow() {
        loginPopupView= LayoutInflater.from(this).inflate(R.layout.login_popup_layout, null, false);
        loginPopupWindow = new PopupWindow(loginPopupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        loginPopupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loginPopupWindow.setFocusable(true);
        loginPopupWindow.setTouchable(true);
        loginPopupWindow.setOutsideTouchable(true);
        loginMsg=loginPopupView.findViewById(R.id.login_message_title);
        TextView confirm_box=loginPopupView.findViewById(R.id.confirm_box);
        confirm_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPopupWindow.dismiss();
                handler.post(light);
            }
        });
    }
    //显示新建歌单弹窗
    private void showloginpopupWindow(String msg) {
        View rootview = LayoutInflater.from(this).inflate(R.layout.login_layout, null);
        loginMsg.setText(msg);
        loginPopupWindow.showAtLocation(rootview, Gravity.TOP, 0, 100);
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
    //调整屏幕亮度
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        lp.alpha = bgAlpha;
        this.getWindow().setAttributes(lp);
    }
}
