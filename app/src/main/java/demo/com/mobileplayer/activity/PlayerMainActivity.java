package demo.com.mobileplayer.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;

import java.util.ArrayList;

import demo.com.mobileplayer.R;
import demo.com.mobileplayer.base.BasePager;
import demo.com.mobileplayer.fragment.ReplaceFragment;
import demo.com.mobileplayer.pager.AudioPager;
import demo.com.mobileplayer.pager.NetVideoPager;
import demo.com.mobileplayer.utils.PermissionUtil;


/**
 *
 * 播放主页面
 */
public class PlayerMainActivity extends AppCompatActivity {


    private RadioGroup rg_bottom_tag;

    /**
     * 页面的集合
     */
    private ArrayList<BasePager> basePagers;

    /**
     * 选中的位置
     */
    private int position;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_main);

        PermissionUtil.verifyStoragePermissions(this);

        iv_back =  findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayerMainActivity.this.finish();
            }
        });
        rg_bottom_tag =  findViewById(R.id.rg_bottom_tag);

        basePagers = new ArrayList<>();

        basePagers.add(new AudioPager(this));//添加本地音乐页面0
        basePagers.add(new NetVideoPager(this));//添加网络视频页面-1


        //设置RadioGroup的监听
        rg_bottom_tag.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

        //默认选中首页
        //  rg_bottom_tag.check(R.id.rb_video);


    }


    public static void startPlayerMainActivity(Context context, String type) {
        Intent intent = new Intent(context, PlayerMainActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String type = getIntent().getStringExtra("type");
        if (!TextUtils.isEmpty(type)) {
            switch (type) {
                case "local_audio":
                    rg_bottom_tag.check(R.id.rb_audio);
                    break;

                case "net_video":

                    rg_bottom_tag.check(R.id.rb_net_video);
                    break;
                 default:
                     rg_bottom_tag.check(R.id.rb_audio);
                     break;
            }
        }

    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {
                //音频
                case R.id.rb_audio:
                    position = 0;
                    break;
                //网络视频
                case R.id.rb_net_video:
                    position = 1;
                    break;
            }

            setFragment();


        }
    }

    /**
     * 把页面添加到Fragment中
     */
    private void setFragment() {
        //1.得到FragmentManger
        FragmentManager manager = getSupportFragmentManager();
        //2.开启事务
        FragmentTransaction ft = manager.beginTransaction();
        //3.替换
        ft.replace(R.id.fl_main_content,new ReplaceFragment(getBasePager()));
        //4.提交事务
        ft.commit();

    }

    /**
     * 根据位置得到对应的页面
     *
     * @return
     */
    private BasePager getBasePager() {
        BasePager basePager = basePagers.get(position);
        if (basePager != null && !basePager.isInitData) {
            basePager.initData();//联网请求或者绑定数据
            basePager.isInitData = true;
        }
        return basePager;
    }

}