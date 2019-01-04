package com.example.twinkle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.twinkle.R;
import com.example.twinkle.enity.SongList;

public class EditSonglistActivity extends AppCompatActivity {

    private SongList songlist;
    private String position_str;
    private TextView change_name_name;
    private String option="back";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_songlist_layout);
        songlist=(SongList)getIntent().getSerializableExtra("songlist");
        position_str=getIntent().getStringExtra("Position");
        change_name_name =findViewById(R.id.change_name_name);
        change_name_name.setText(songlist.getSongListName());
        ImageView edit_songlist_back=findViewById(R.id.back);
        edit_songlist_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish_activity();
            }
        });
        TextView edit_songlist_name=findViewById(R.id.change_name_btn_area);
        edit_songlist_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!position_str.equals("0")){
                    Intent intent = new Intent(EditSonglistActivity.this,EditSonglistNameActivity.class);
                    intent.putExtra("songlist",songlist);
                    startActivityForResult(intent, 1);
                    overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish_activity();
    }

    private void finish_activity(){
        Intent intent = new Intent();
        intent.putExtra("Option", option);
        intent.putExtra("songlist", songlist);
        intent.putExtra("Position", position_str);
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String backoption=data.getStringExtra("Option");
        switch (backoption) {
            case "edit":
                SongList backsonglist=(SongList)data.getSerializableExtra("songlist");
                if(!songlist.getSongListName().equals(backsonglist.getSongListName())){
                    songlist.setSongListName(backsonglist.getSongListName());
                    change_name_name.setText(songlist.getSongListName());
                    option="edit";
                }
                break;
            default:

        }
    }
}
