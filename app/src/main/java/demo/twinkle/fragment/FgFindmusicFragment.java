package demo.twinkle.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import demo.com.mobileplayer.R;
import demo.com.mobileplayer.activity.PlayerMainActivity;

public class FgFindmusicFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState){
          View  rootView =  inflater.inflate(R.layout.fg_find_music,container,false);


        TextView  recommend_mv_title = rootView.findViewById(R.id.recommend_mv_title);
        recommend_mv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              PlayerMainActivity.startPlayerMainActivity(FgFindmusicFragment.this.getContext(),"net_video");
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
    }
}
