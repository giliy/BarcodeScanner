package com.barcodescanner.gili.scan9.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.barcodescanner.gili.scan9.R;

import java.util.ArrayList;

import utils.Product;

/**
 * Created by gili on 6/26/2015.
 */
public class AdapterSearchCart extends RecyclerView.Adapter<AdapterSearchCart.ViewHolderSearchCart> {

    private ArrayList<Product> listProducts = new ArrayList<>();
    private LayoutInflater layoutInflater;

    public AdapterSearchCart(Context context){
        layoutInflater = LayoutInflater.from(context);
    }

    public void setProductsList(ArrayList<Product> listProducts){
        this.listProducts = listProducts;
        notifyItemRangeChanged(0,listProducts.size());
    }

    @Override
    public ViewHolderSearchCart onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.custom_item_search_cart,parent,false);
        ViewHolderSearchCart viewHolder = new ViewHolderSearchCart(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderSearchCart holder, int position) {
        Product currentProduct = listProducts.get(position);
        holder.productName.setText(currentProduct.getProductName());
    }

    @Override
    public int getItemCount() {
        return listProducts.size();
    }

    static class ViewHolderSearchCart extends RecyclerView.ViewHolder{

        private ImageView productImage;
        private TextView productName;
        private RatingBar productRate;

        public ViewHolderSearchCart(View itemView) {
            super(itemView);
            productImage = (ImageView) itemView.findViewById(R.id.prod_icon);
            productName = (TextView) itemView.findViewById(R.id.prod_name);
            productRate = (RatingBar) itemView.findViewById(R.id.prodRate);
        }
    }


}
