package demo.twinkle.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import demo.com.mobileplayer.R;
import demo.twinkle.adapter.ManageSonglistAdapter;
import demo.twinkle.bean.SongList;

public class ManageSonglistActivity extends AppCompatActivity implements OnItemClickListener,ManageSonglistAdapter.InnerItemOnclickListener {
    private List<SongList> songListList;
    private View popupView;
    private ListView manage_songlist;
    private PopupWindow popupWindow;
    private ManageSonglistAdapter manage_songlistAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_songlist_layout);
        manage_songlist = (ListView) findViewById(R.id.manage_songlist_list_view);
        songListList = (List<SongList>)getIntent().getSerializableExtra("songListList");
        manage_songlistAdapter = new ManageSonglistAdapter(this, R.layout.manage_songlist_item, songListList);
        manage_songlistAdapter.setOnInnerItemOnClickListener(this);
        manage_songlist.setAdapter(manage_songlistAdapter);
        manage_songlist.setOnItemClickListener(this);
        ImageView manage_songlist_back= (ImageView) findViewById(R.id.back);
        manage_songlist_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_fade_out, R.anim.push_fade_in);
            }
        });
        initPopwindow();
        TextView delete_songlist_back= (TextView) findViewById(R.id.delete_btn_area);
        delete_songlist_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindow();
                setBackgroundAlpha(0.7f);
            }
        });
    }

    private void initPopwindow() {
        popupView = LayoutInflater.from(this).inflate(R.layout.delete_songlist_layout, null, false);
        popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.newsonglistTranslate);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
        TextView popup_cancel=popupView.findViewById(R.id.cancel_box);
        popup_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        TextView popup_confirm=popupView.findViewById(R.id.confirm_box);
        popup_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                finish();
                overridePendingTransition(R.anim.push_fade_out, R.anim.push_fade_in);
            }
        });
    }

    //调整屏幕亮度
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        lp.alpha = bgAlpha;
        this.getWindow().setAttributes(lp);
    }

    //显示底部弹窗
    private void showPopWindow() {
        View rootview = LayoutInflater.from(this).inflate(R.layout.manage_songlist_layout, null);
        popupWindow.showAtLocation(rootview, Gravity.TOP, 0, 100);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckBox songlist_checkbox=view.findViewById(R.id.songlist_checkBox);
        if(songlist_checkbox.isChecked()) {
            songlist_checkbox.setChecked(false);
        }else{
            songlist_checkbox.setChecked(true);
        }
    }

    private void setcheckbox(int position){
        if(songListList.get(position).getSonglistIsChecked()) {
            songListList.get(position).setSonglistIsChecked(false);
            CheckBox temp_checkbox= manage_songlist.getChildAt(position).findViewById(R.id.songlist_checkBox);
            temp_checkbox.setChecked(false);
        }else{
            songListList.get(position).setSonglistIsChecked(true);
            CheckBox temp_checkbox= manage_songlist.getChildAt(position).findViewById(R.id.songlist_checkBox);
            temp_checkbox.setChecked(true);
        }
    }

    @Override
    public void itemClick(View v) {
        int position;
        position = (Integer) v.getTag();
        switch (v.getId()) {
            case R.id.songlist_check_area:
                setcheckbox(position);
                break;
            case R.id.songlist_checkBox:
                setcheckbox(position);
                break;
            default:
                break;
        }
    }
}
