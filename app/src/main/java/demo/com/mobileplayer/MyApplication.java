package demo.com.mobileplayer;

import android.app.Application;

import org.xutils.x;


public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initXUtil3();


    }

    private void initXUtil3() {
        //xutils 3 初始化
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能
    }


}
