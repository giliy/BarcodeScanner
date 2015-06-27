package com.barcodescanner.gili.scan9;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.barcodescanner.gili.scan9.barcodeHandler.Barcode;

import utils.Product;


public class Search extends Fragment
{
    View myFragmentView;
    private static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    SearchView search;
    ImageButton buttonBarcode;
    ImageButton buttonAudio;
    Typeface type;
    ListView searchResults;
    String found = "N";
    final int REQUEST_CODE = 1;

    //This arraylist will have data as pulled from server. This will keep cumulating.
    ArrayList<Product> productResults = new ArrayList<>();
    //Based on the search string, only filtered products will be moved here from productResults
    ArrayList<Product> filteredProductResults = new ArrayList<>();


    public static Search newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        Search fragment = new Search();
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
        //get the context of the HomeScreen Activity
        final HomeScreen activity = (HomeScreen) getActivity();
        type= Typeface.createFromAsset(activity.getAssets(),"book.ttf");
        myFragmentView = inflater.inflate(R.layout.fragment_search, container, false);
        TextView tvTitle = (TextView) myFragmentView.findViewById(R.id.tvTitle);
        tvTitle.setText("Fragment #" + mPage);

        search=(SearchView) myFragmentView.findViewById(R.id.searchView1);
        search.setQueryHint("Start typing to search...");
        searchResults = (ListView) myFragmentView.findViewById(R.id.listview_search);
        buttonBarcode = (ImageButton) myFragmentView.findViewById(R.id.imageButton2);
        buttonAudio = (ImageButton) myFragmentView.findViewById(R.id.imageButton1);


        //this part of the code is to handle the situation when user enters any search criteria, how should the
        //application behave?

        buttonBarcode.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                startActivityForResult(new Intent(activity, Barcode.class),REQUEST_CODE);
            }
        });

        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener()
        {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

                //Toast.makeText(activity, String.valueOf(hasFocus),Toast.LENGTH_SHORT).show();
            }
        });

        search.setOnQueryTextListener(new OnQueryTextListener()
        {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.length() > 3)
                {

                    searchResults.setVisibility(myFragmentView.VISIBLE);
                    myAsyncTask m = (myAsyncTask) new myAsyncTask().execute(newText);
                }
                else
                {

                    searchResults.setVisibility(myFragmentView.INVISIBLE);
                }



                return false;
            }

        });


        return myFragmentView;
    }

    //this filters products from productResults and copies to filteredProductResults based on search text

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

    //this captures the result from barcode and populates in the searchView.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        //Log.d("arindam","insideOnActivityResult");
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
                search.setQuery(barcode, true);
                search.setIconifiedByDefault(false);
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
                searchResults.setAdapter(new SearchResultsAdapter(getActivity(),filteredProductResults));
                pd.dismiss();
            }
        }

    }
}

class SearchResultsAdapter extends BaseAdapter
{
    private LayoutInflater layoutInflater;

    private ArrayList<Product> productDetails=new ArrayList<>();
    int count;
    Typeface type;
    Context context;

    //constructor method
    public SearchResultsAdapter(Context context, ArrayList<Product> product_details) {

        layoutInflater = LayoutInflater.from(context);

        this.productDetails=product_details;
        this.count= product_details.size();
        this.context = context;
        type= Typeface.createFromAsset(context.getAssets(),"book.ttf");

    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int arg0) {
        return productDetails.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        ViewHolder holder;
        Product tempProduct = productDetails.get(position);

        if (convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.listtwo_searchresults, null);
            holder = new ViewHolder();
            holder.product_name = (TextView) convertView.findViewById(R.id.product_name);
            holder.product_mrp = (TextView) convertView.findViewById(R.id.product_mrp);
            holder.product_mrpvalue = (TextView) convertView.findViewById(R.id.product_mrpvalue);
            holder.product_bb = (TextView) convertView.findViewById(R.id.product_bb);
            holder.product_bbvalue = (TextView) convertView.findViewById(R.id.product_bbvalue);
            holder.addToCart = (Button) convertView.findViewById(R.id.add_cart);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.product_name.setText(tempProduct.getProductName());
        holder.product_name.setTypeface(type);

        holder.product_mrp.setTypeface(type);

        holder.product_mrpvalue.setText(tempProduct.getProductMRP());
        holder.product_mrpvalue.setTypeface(type);

        holder.product_bb.setTypeface(type);

        holder.product_bbvalue.setText(tempProduct.getProductBBPrice());
        holder.product_bbvalue.setTypeface(type);

        return convertView;
    }

    static class ViewHolder
    {
        TextView product_name;
        TextView product_mrp;
        TextView product_mrpvalue;
        TextView product_bb;
        TextView product_bbvalue;
        TextView product_savings;
        TextView product_savingsvalue;
        TextView qty;
        TextView product_value;
        Button addToCart;

    }


}