package com.example.twinkle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class EditSonglistActivity extends AppCompatActivity {

    private SongList songlist;
    private String position_str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_songlist_layout);
        songlist=(SongList)getIntent().getSerializableExtra("songlist");
        position_str=getIntent().getStringExtra("Position");
        TextView change_name_name =findViewById(R.id.change_name_name);
        change_name_name.setText(songlist.getSongListName());
        ImageView edit_songlist_back=findViewById(R.id.back);
        edit_songlist_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish_activity("back");
            }
        });
        TextView edit_songlist_name=findViewById(R.id.change_name_btn_area);
        edit_songlist_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditSonglistActivity.this,EditSonglistNameActivity.class);
                intent.putExtra("songlist",songlist);
                startActivity(intent);
                overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish_activity("back");
    }

    private void finish_activity(String option){
        Intent intent = new Intent();
        intent.putExtra("Option", option);
        intent.putExtra("Position", position_str);
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
    }
}
