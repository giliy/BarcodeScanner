package com.barcodescanner.gili.scan9;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.barcodescanner.gili.scan9.homeFragment.ShoppingList;
import com.barcodescanner.gili.scan9.homeFragment.SearchProduct;
import com.barcodescanner.gili.scan9.homeFragment.SearchCart;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import Tabs.SlidingTabLayout;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import utils.AnimationUtils;
import utils.Product;
import utils.SortListener;


public class HomeActivity extends ActionBarActivity implements MaterialTabListener, View.OnClickListener{

    private static final String ARG_PAGE = "ARG_PAGE";
    public View myFragmentView;
    public ListView searchResults;
    private Button scanBtn;
    public ViewPager mPager;
    private SlidingTabLayout mTabs;
    public MyPagerAdapter mAdapter;

    private NavigationDrawerFragment drawerFragment;
    private Toolbar mToolbar;
    private ViewGroup mContainerToolbar;
    public MaterialTabHost mTabHost;

    private String found = "N";
    public final int REQUEST_CODE = 1;

    public static final String TAG_SORT_NAME = "sortName";
    public static final String TAG_SORT_DATE = "sortDate";
    public static final String TAG_SORT_RATE = "sortRate";

    private FloatingActionButton mFAB;
    private FloatingActionMenu mFABMenu;

    //This arraylist will have data as pulled from server. This will keep cumulating.
    ArrayList<Product> productResults = new ArrayList<>();
    //Based on the search string, only filtered products will be moved here from productResults
    ArrayList<Product> filteredProductResults = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupDrawer();
        setupTabs();
        setupFAB();
    }

    private void setupDrawer() {
        mToolbar = (Toolbar)findViewById(R.id.app_bar);
        mContainerToolbar = (ViewGroup) findViewById(R.id.container_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer_fragment);
        drawerFragment.setUp(R.id.navigation_drawer_fragment,(DrawerLayout)findViewById(R.id.drawer_layout), mToolbar);
    }

    private void setupTabs() {
        mPager = (ViewPager) findViewById(R.id.vwPager);
        mTabHost = (MaterialTabHost) findViewById(R.id.materialTabHost);
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);

        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){

            @Override
            public void onPageSelected(int position) {
                mTabHost.setSelectedNavigationItem(position);
            }
        });

        for(int i=0; i< mAdapter.getCount(); i++){
            mTabHost.addTab(mTabHost.newTab().setIcon(mAdapter.getIcons(i)).setTabListener(this));
        }
    }

    private void setupFAB() {
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.ic_action_new);

        mFAB  = new FloatingActionButton.Builder(this)
                .setContentView(imageView)
                .setBackgroundDrawable(R.drawable.selector_navigation_button_red)
                .build();

        ImageView iconSortName = new ImageView(this);
        iconSortName.setImageResource(R.drawable.ic_action_alphabets);
        ImageView iconSortDate = new ImageView(this);
        iconSortDate.setImageResource(R.drawable.ic_barcode);
        ImageView iconSortRate = new ImageView(this);
        iconSortRate.setImageResource(R.drawable.ic_launcher);

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        itemBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_sub_navigation_button_gray));

        SubActionButton buttonSortName = itemBuilder.setContentView(iconSortName).build();
        SubActionButton buttonSortDate = itemBuilder.setContentView(iconSortDate).build();
        SubActionButton buttonSortRate = itemBuilder.setContentView(iconSortRate).build();

        buttonSortName.setTag(TAG_SORT_NAME);
        buttonSortDate.setTag(TAG_SORT_DATE);
        buttonSortRate.setTag(TAG_SORT_RATE);

        buttonSortName.setOnClickListener(this);
        buttonSortDate.setOnClickListener(this);
        buttonSortRate.setOnClickListener(this);

        mFABMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(buttonSortName)
                .addSubActionView(buttonSortDate)
                .addSubActionView(buttonSortRate)
                .attachTo(mFAB)
                .build();

        AnimationUtils.animateToolbar(mContainerToolbar);
    }



    public static ArrayList<Product> getData() {
        ArrayList<Product> data = new ArrayList<>();
        int[] icons = {R.drawable.ic_launcher,R.drawable.ic_compose,R.drawable.ic_pwd,R.drawable.ic_username};
        String[] Pnames={"gvina","corekas", "halav", "avokado"};

        for(int i=0; i<Pnames.length; i++){
            Product current = new Product();
            current.setProductIcon(icons[i]);
            current.setProductName(Pnames[i]);
            data.add(current);
        }
        return data;
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
                //that means _barcode could not be identified or user pressed the back button
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
            pd= new ProgressDialog(HomeActivity.this);
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
                Toast.makeText(HomeActivity.this, "Unable to connect to server,please try later", Toast.LENGTH_LONG).show();

                pd.dismiss();
            }
            else
            {
                //calling this method to filter the search results from productResults and move them to
                //filteredProductResults
                filterProductArray(textSearch);
                searchResults.setAdapter(new SearchResultsAdapter(HomeActivity.this, filteredProductResults));
                pd.dismiss();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_products, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onTabSelected(MaterialTab materialTab) {
        mPager.setCurrentItem(materialTab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {

    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }

    @Override
    public void onClick(View v) {
        Fragment fragment = (Fragment) mAdapter.instantiateItem(mPager, mPager.getCurrentItem());
        if(fragment instanceof SortListener){

            if(v.getTag().equals(TAG_SORT_NAME)){
                ((SortListener)fragment).onSortBuyName();
            }
            if(v.getTag().equals(TAG_SORT_DATE)){
                Toast.makeText(HomeActivity.this, "let me think", Toast.LENGTH_LONG).show();

            }
            if(v.getTag().equals(TAG_SORT_RATE)){
                Toast.makeText(HomeActivity.this, "good question", Toast.LENGTH_LONG).show();

            }

        }


    }

    public void onDrawerItemClicked(int index){
        mPager.setCurrentItem(index);

    }

    public void onDrawerSlide(float slideOffset){
        toggleTranslateFAB(slideOffset);
    }

    private void toggleTranslateFAB(float slideOffset) {
        if(mFABMenu != null){
            if(mFABMenu.isOpen()){
                mFABMenu.close(true);
            }
            mFAB.setTranslationX(slideOffset * 200);
        }
    }

    class MyPagerAdapter extends FragmentStatePagerAdapter {

        String[] tabs;
        int icons[] = {R.drawable.ic_barcode, R.drawable.ic_cart2, R.drawable.ic_cart2};
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.tabs);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return getResources().getStringArray(R.array.tabs)[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return  SearchProduct.getInstance(position);
                case 1:
                    return SearchCart.getInstance(position);
                case 2:
                    return ShoppingList.getInstance(position);
                default:
                    break;
            }
            return  null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        private Drawable getIcons(int position){
            return getResources().getDrawable(icons[position]);
        }

    }
}
