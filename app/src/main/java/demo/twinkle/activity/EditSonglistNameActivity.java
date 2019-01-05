package demo.twinkle.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.Timer;
import java.util.TimerTask;

import demo.com.mobileplayer.R;

public class EditSonglistNameActivity extends AppCompatActivity {
    private EditText change_name_editbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_songlist_name_layout);
        String songlist_name=getIntent().getStringExtra("songlist_name");
        change_name_editbox = (EditText) findViewById(R.id.change_songlist_name_editbox);
        change_name_editbox.setText(songlist_name);
        change_name_editbox.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        change_name_editbox .setImeOptions(EditorInfo.IME_ACTION_DONE);
        change_name_editbox.setFocusable(true);
        change_name_editbox.setFocusableInTouchMode(true);;
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
        ImageView edit_songlist_name_back= (ImageView) findViewById(R.id.back);
        edit_songlist_name_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_fade_out, R.anim.push_fade_in);
            }
        });
        ImageView edit_songlist_name_delete= (ImageView) findViewById(R.id.delete);
        edit_songlist_name_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_name_editbox.setText("");
            }
        });
        TextView edit_songlist_name_save= (TextView) findViewById(R.id.save);
        edit_songlist_name_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(change_name_editbox.getText().toString().equals("")==false){
                    finish();
                    overridePendingTransition(R.anim.push_fade_out, R.anim.push_fade_in);
                }
            }
        });
    }

}
