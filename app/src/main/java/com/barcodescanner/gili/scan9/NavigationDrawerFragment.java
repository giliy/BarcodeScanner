package com.barcodescanner.gili.scan9;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import com.barcodescanner.gili.scan9.adapters.RecyclerAdapter;


public class NavigationDrawerFragment extends Fragment implements RecyclerAdapter.ClickListener {

    private static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";
    private static final String PREF_FILE_NAME = "testpref";
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;

    private android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private View containerView;

    public NavigationDrawerFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(),KEY_USER_LEARNED_DRAWER,"false"));
        if(savedInstanceState != null){
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_nagivation_drawer,container,false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        adapter = new RecyclerAdapter(getActivity(),getData());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return layout;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
      mDrawerLayout = drawerLayout;
      containerView = getActivity().findViewById(fragmentId);
      mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,R.string.drawer_open, R.string.drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if(!mUserLearnedDrawer){
                    mUserLearnedDrawer = true;
                    saveToPreferences(getActivity(),KEY_USER_LEARNED_DRAWER,mUserLearnedDrawer+"");
                }

                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

          @Override
          public void onDrawerSlide(View drawerView, float slideOffset) {
              if(slideOffset < 0.6){
                  toolbar.setAlpha(1-slideOffset);
              }
          }
      };
        if(!mUserLearnedDrawer && !mFromSavedInstanceState){
            mDrawerLayout.openDrawer(containerView);
        }

      mDrawerLayout.setDrawerListener(mDrawerToggle);

      mDrawerLayout.post(new Runnable() {
          @Override
          public void run() {
              mDrawerToggle.syncState();
          }
      });
    }

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue){

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String preferenceName, String defaultValue) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName,defaultValue);


    }

    public static ArrayList<ProductInformation> getData() {
        ArrayList<ProductInformation> data = new ArrayList<>();
        int[] icons = {R.drawable.ic_launcher,R.drawable.ic_compose,R.drawable.ic_pwd,R.drawable.ic_username};
        String[] Pnames={"gvina","corekas", "halav", "avokado"};

        for(int i=0; i<100; i++){
            ProductInformation current = new ProductInformation();
            current.iconId = icons[i%icons.length];
            current.title = Pnames[i%icons.length];
            data.add(current);
        }
        return data;
    }

    @Override
    public void itemClicked(View view, int position) {
        startActivity(new Intent(getActivity(), RegisterScreen.class));
    }
}
