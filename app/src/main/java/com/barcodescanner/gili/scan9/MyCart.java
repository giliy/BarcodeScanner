package com.barcodescanner.gili.scan9;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import utils.Product;


public class MyCart extends Fragment {


    private static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    private View myFragmentView;
    private ListView searchResults;
    private Button scanBtn;

    String found = "N";
    final int REQUEST_CODE = 1;

    //This arraylist will have data as pulled from server. This will keep cumulating.
    ArrayList<Product> productResults = new ArrayList<>();
    //Based on the search string, only filtered products will be moved here from productResults
    ArrayList<Product> filteredProductResults = new ArrayList<>();

    public static MyCart newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        MyCart fragment = new MyCart();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final HomeScreen activity = (HomeScreen) getActivity();
        myFragmentView = inflater.inflate(R.layout.fragment_mycart, container, false);
        searchResults = (ListView) myFragmentView.findViewById(R.id.listview_products1);
        scanBtn = (Button) myFragmentView.findViewById(R.id.scanBtn1);

        scanBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                startActivityForResult(new Intent(activity, Barcode.class),REQUEST_CODE);
            }
        });


        return myFragmentView;
    }

    public void filterProductArray(String newText)
    {

        String pName;

        filteredProductResults.clear();
        for (int i = 0; i < productResults.size(); i++)
        {
            pName = productResults.get(i).getProductName().toLowerCase();
            if ( pName.contains(newText.toLowerCase()) ||
                    productResults.get(i).getProductBarcode().contains(newText))
            {
                filteredProductResults.add(productResults.get(i));

            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        //super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE)
        {
            String barcode=data.getStringExtra("BARCODE");
            if (barcode.equals("NULL"))
            {
                //that means barcode could not be identified or user pressed the back button
                //do nothing
            }
            else
            {
                searchResults.setVisibility(myFragmentView.VISIBLE);
                new myAsyncTask().execute(barcode);
            }
        }

    }

    //in this myAsyncTask, we are fetching data from server for the search string entered by user.
    class myAsyncTask extends AsyncTask<String, Void, String>
    {
        JSONParser jParser;
        JSONArray productList;
        String url=new String();
        String textSearch;
        ProgressDialog pd;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            productList=new JSONArray();
            jParser = new JSONParser();
            pd= new ProgressDialog(getActivity());
            pd.setCancelable(false);
            pd.setMessage("Searching...");
            pd.getWindow().setGravity(Gravity.CENTER);
            pd.show();
        }

        @Override
        protected String doInBackground(String... sText) {

            url="http://lawgo.in/lawgo/products/user/1/search/"+sText[0];
            String returnResult = getProductList(url);
            this.textSearch = sText[0];
            return returnResult;

        }

        public String getProductList(String url)
        {

            Product tempProduct;
            String matchFound;
            //productResults is an arraylist with all product details for the search criteria
            //productResults.clear();


            try {


                JSONObject json = jParser.getJSONFromUrl(url);

                productList = json.getJSONArray("ProductList");

                //parse date for dateList
                for(int i=0;i<productList.length();i++)
                {
                    tempProduct = new Product();

                    JSONObject obj=productList.getJSONObject(i);

                    tempProduct.setProductCode(obj.getString("ProductCode"));
                    tempProduct.setProductName(obj.getString("ProductName"));
                    tempProduct.setProductGrammage(obj.getString("ProductGrammage"));
                    tempProduct.setProductBarcode(obj.getString("ProductBarcode"));
                    tempProduct.setProductDivision(obj.getString("ProductCatCode"));
                    tempProduct.setProductDepartment(obj.getString("ProductSubCode"));
                    tempProduct.setProductMRP(obj.getString("ProductMRP"));
                    tempProduct.setProductBBPrice(obj.getString("ProductBBPrice"));

                    //check if this product is already there in productResults, if yes, then don't add it again.
                    matchFound = "N";

                    for (int j=0; j < productResults.size();j++)
                    {

                        if (productResults.get(j).getProductCode().equals(tempProduct.getProductCode()))
                        {
                            matchFound = "Y";
                        }
                    }

                    if (matchFound == "N")
                    {
                        productResults.add(tempProduct);
                    }

                }

                return ("OK");

            } catch (Exception e) {
                e.printStackTrace();
                return ("Exception Caught");
            }
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            if(result.equalsIgnoreCase("Exception Caught"))
            {
                Toast.makeText(getActivity(), "Unable to connect to server,please try later", Toast.LENGTH_LONG).show();

                pd.dismiss();
            }
            else
            {
                //calling this method to filter the search results from productResults and move them to
                //filteredProductResults
                filterProductArray(textSearch);
                searchResults.setAdapter(new SearchResultsAdapter(getActivity(), filteredProductResults));
                pd.dismiss();
            }
        }

    }

}
