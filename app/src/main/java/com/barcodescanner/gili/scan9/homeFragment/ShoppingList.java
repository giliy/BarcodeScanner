package com.barcodescanner.gili.scan9.homeFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.barcodescanner.gili.scan9.R;
import com.barcodescanner.gili.scan9.adapters.AdapterRecyclerAnimators;

import jp.wasabeef.recyclerview.animators.ScaleInAnimator;


public class ShoppingList extends Fragment {

    //int containing the duration of the animation run when items are added or removed from the RecyclerView
    public static final int ANIMATION_DURATION = 2000;

    private EditText mInput;

    private RecyclerView mRecyclerView;

    private AdapterRecyclerAnimators mAdapter;
    private Button mButton;

    public static ShoppingList getInstance(int position) {
        ShoppingList myFragment = new ShoppingList();
        Bundle args = new Bundle();
        args.putInt("position", position);
        myFragment.setArguments(args);
        return myFragment;
    }


    public ShoppingList() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layout =inflater.inflate(R.layout.fragment_shopping_list, container, false);
        mInput = (EditText) layout.findViewById(R.id.text_input);
        mButton = (Button) layout.findViewById(R.id.plusButtonId);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.recyclerAnimatedItems);
        mAdapter = new AdapterRecyclerAnimators(getActivity());
        ScaleInAnimator animator = new ScaleInAnimator();
        animator.setAddDuration(ANIMATION_DURATION);
        animator.setRemoveDuration(ANIMATION_DURATION);
        mRecyclerView.setItemAnimator(animator);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

        return layout;
    }


    public void addItem() {
        //check if the EditText has valid contents

        if (mInput.getText() != null) {
            String text = mInput.getText().toString();
            if(text != null && text.trim().length()>0){
                mAdapter.addItem(mInput.getText().toString());
            }
        }
    }
}
