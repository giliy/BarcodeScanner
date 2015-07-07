package com.barcodescanner.gili.scan9.homeFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.barcodescanner.gili.scan9.HomeActivity;
import com.barcodescanner.gili.scan9.R;
import com.barcodescanner.gili.scan9.barcodeHandler.Barcode;

import utils.SortListener;

/**
 * Created by gili on 6/21/2015.
 */
public class SearchProduct extends Fragment implements SortListener{

    static int REQUEST_CODE = 1;

    private Button scanBtn;
    private ListView listProd;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    public static SearchProduct getInstance(int position) {
        SearchProduct myFragment = new SearchProduct();
        Bundle args = new Bundle();
        args.putInt("position", position);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final HomeActivity activity = (HomeActivity) getActivity();
        View layout = inflater.inflate(R.layout.fragment_search_prod, container, false);
        scanBtn = (Button) layout.findViewById(R.id.scanBtn);
        listProd = (ListView) layout.findViewById(R.id.listview_products);

        Bundle bundle = getArguments();
        if(bundle != null){
            //    textView.setText("the page is: "+bundle.getInt("position"));
            scanBtn.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    startActivityForResult(new Intent(activity, Barcode.class),REQUEST_CODE);
                }
            });
        }

        return layout;
    }


    @Override
    public void onSortBuyName() {

    }

    @Override
    public void onSortBuyDate() {

    }

    @Override
    public void onSortBuyRate() {

    }
}