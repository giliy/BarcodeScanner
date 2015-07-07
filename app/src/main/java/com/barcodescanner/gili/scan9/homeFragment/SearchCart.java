package com.barcodescanner.gili.scan9.homeFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.barcodescanner.gili.scan9.R;
import com.barcodescanner.gili.scan9.adapters.AdapterSearchCart;
import network.VolleySingleton;
import utils.Product;
import utils.ProductSorter;
import utils.SortListener;

import static utils.Keys.SearchProductKeys.*;

public class SearchCart extends Fragment implements SortListener{

    private static final String STATE_PRODUCT = "state_product";
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;
    private RequestQueue requestQueue;
    private RecyclerView recyclerViewProducts;
    private ArrayList<Product> listProduct = new ArrayList<>();
    public AdapterSearchCart adapterSearchCart;
    public ProductSorter productSorter = new ProductSorter();
    public static final String serverUrl = "http://lawgo.in/lawgo/products/user/1/search/";



    public static SearchCart getInstance(int position) {
        SearchCart myFragment = new SearchCart();
        Bundle args = new Bundle();
        args.putInt("position", position);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_PRODUCT,listProduct);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getRequestQueue();
        sendJsonRequest();
    }

    private void sendJsonRequest() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,serverUrl,"",new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                listProduct = parseJSONResponse(response);
                Toast.makeText(getActivity(), listProduct.toString(), Toast.LENGTH_LONG).show();
                adapterSearchCart.setProductsList(listProduct);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }

    private ArrayList<Product> parseJSONResponse(JSONObject response){

        ArrayList<Product> listP = new ArrayList<>();
        if(response==null || response.length()==0)
            return listP;
        StringBuilder data = new StringBuilder();

        try {
            JSONArray arrayProducts = response.getJSONArray(KEY_PROD_LIST);

            for (int i=0; i<arrayProducts.length(); i++){
                JSONObject currentProduct = arrayProducts.getJSONObject(i);
                String name = currentProduct.getString(KEY_PROD_NAME);
                String barcd = currentProduct.getString(KEY_PROD_BARCODE);
                data.append(name +" " + barcd+ "\n");
                Product product = new Product();
                product.setProductName(name);
                product.setProductBarcode(barcd);
                listP.add(product);
            }


        }
        catch (JSONException e) {

        }

        return listP;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_search_cart, container, false);
        recyclerViewProducts = (RecyclerView) layout.findViewById(R.id.listProd);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterSearchCart = new AdapterSearchCart(getActivity());
        recyclerViewProducts.setAdapter(adapterSearchCart);
        if(savedInstanceState != null){
            listProduct = savedInstanceState.getParcelableArrayList(STATE_PRODUCT);
            adapterSearchCart.setProductsList(listProduct);
        }
        else{
            sendJsonRequest();
        }


        return layout;
    }


    @Override
    public void onSortBuyName() {
        Toast.makeText(getActivity(),"im in onSortBuyName",Toast.LENGTH_SHORT).show();
        productSorter.SortProductByName(listProduct);
        adapterSearchCart.notifyDataSetChanged();
    }

    @Override
    public void onSortBuyDate() {

    }

    @Override
    public void onSortBuyRate() {

    }
}
