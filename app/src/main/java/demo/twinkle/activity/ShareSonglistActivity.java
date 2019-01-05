package demo.twinkle.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import demo.com.mobileplayer.R;


public class ShareSonglistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_songlist_layout);
        String songlist_name=getIntent().getStringExtra("songlist_name");
        String songlist_creator=getIntent().getStringExtra("songlist_creator");
        TextView songlist_name_title = (TextView) findViewById(R.id.songlist_name_title);
        songlist_name_title.setText(songlist_name);
        TextView songlist_creator_title = (TextView) findViewById(R.id.songlist_creator_title);
        String songlist_creator_string="创建者："+songlist_creator;
        songlist_creator_title.setText(songlist_creator_string);

        ImageView share_songlist_back= (ImageView) findViewById(R.id.back);
        share_songlist_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_fade_out, R.anim.push_fade_in);
            }
        });
        TextView share_songlist_btn= (TextView) findViewById(R.id.share_btn);
        share_songlist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    finish();
                    overridePendingTransition(R.anim.push_fade_out, R.anim.push_fade_in);
            }
        });
    }

}
