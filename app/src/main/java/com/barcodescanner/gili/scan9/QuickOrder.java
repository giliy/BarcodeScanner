package com.barcodescanner.gili.scan9;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class QuickOrder extends Fragment {

    View myFragmentView;
    private static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;

    public static QuickOrder newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        QuickOrder fragment = new QuickOrder();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        myFragmentView = inflater.inflate(R.layout.fragment_quickorder, container, false);
        TextView tvTitle = (TextView) myFragmentView.findViewById(R.id.tvTitle);
        tvTitle.setText("Fragment #" + mPage);
        return myFragmentView;
    }
}