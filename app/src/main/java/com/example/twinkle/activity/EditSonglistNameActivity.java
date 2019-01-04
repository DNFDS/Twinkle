package com.example.twinkle.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.twinkle.R;
import com.example.twinkle.enity.SongList;

import java.util.Timer;
import java.util.TimerTask;

public class EditSonglistNameActivity extends AppCompatActivity {
    private EditText change_name_editbox;

    private SongList songlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_songlist_name_layout);
        songlist=(SongList)getIntent().getSerializableExtra("songlist");
        change_name_editbox =findViewById(R.id.change_songlist_name_editbox);
        change_name_editbox.setText(songlist.getSongListName());
        change_name_editbox.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        change_name_editbox .setImeOptions(EditorInfo.IME_ACTION_DONE);
        change_name_editbox.setFocusable(true);
        change_name_editbox.setFocusableInTouchMode(true);
        change_name_editbox.requestFocus();;
        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            public void run()
            {
                InputMethodManager inputManager = (InputMethodManager)change_name_editbox.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
                inputManager.showSoftInput(change_name_editbox, 0);
            }
        }, 500);
        ImageView edit_songlist_name_back=findViewById(R.id.back);
        edit_songlist_name_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish_activity("back");
            }
        });
        ImageView edit_songlist_name_delete=findViewById(R.id.delete);
        edit_songlist_name_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_name_editbox.setText("");
            }
        });
        TextView edit_songlist_name_save=findViewById(R.id.save);
        edit_songlist_name_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!change_name_editbox.getText().toString().equals("")){
                    songlist.setSongListName(change_name_editbox.getText().toString());
                    finish_activity("edit");
                }
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
        intent.putExtra("songlist", songlist);
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
    }
}
