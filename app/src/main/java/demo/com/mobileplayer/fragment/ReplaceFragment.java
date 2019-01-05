package demo.com.mobileplayer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * java.lang.IllegalStateException: Fragment null must be a public static class to be properly recreated from instance state.
 *
 *
 */
import demo.com.mobileplayer.base.BasePager;

public class ReplaceFragment extends Fragment {

    private BasePager currPager;

    public ReplaceFragment( ){

    }
    public ReplaceFragment(BasePager pager) {
        this.currPager = pager;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         if (currPager!=null){
             return currPager.rootView;
         }
        return null;
    }
}
