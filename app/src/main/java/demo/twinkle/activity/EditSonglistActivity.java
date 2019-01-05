package demo.twinkle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import demo.com.mobileplayer.R;


public class EditSonglistActivity extends AppCompatActivity {

    private String songlist_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_songlist_layout);
        songlist_name=getIntent().getStringExtra("songlist_name");
        TextView change_name_name = (TextView) findViewById(R.id.change_name_name);
        change_name_name.setText(songlist_name);
        ImageView edit_songlist_back=(ImageView)findViewById(R.id.back);
        edit_songlist_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_fade_out, R.anim.push_fade_in);
            }
        });
        TextView edit_songlist_name= (TextView) findViewById(R.id.change_name_btn_area);
        edit_songlist_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditSonglistActivity.this,EditSonglistNameActivity.class);
                intent.putExtra("songlist_name",songlist_name);//key就是自己定义一个String的字符串就行了
                startActivity(intent);
                overridePendingTransition(R.anim.push_fade_out, R.anim.push_fade_in);
            }
        });
    }

}
