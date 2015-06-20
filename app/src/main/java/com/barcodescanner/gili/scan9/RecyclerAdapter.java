package com.barcodescanner.gili.scan9;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import utils.Product;

/**
 * Created by gili on 6/16/2015.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.myViewHolder> {

    private LayoutInflater inflator;
    private List<ProductInformation> data = Collections.emptyList();

    private Context context;
    private ClickListener clickListener;

    public RecyclerAdapter(Context context, List<ProductInformation> data){
        this.context = context;
        inflator = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.product_row, parent, false);
        myViewHolder holder = new myViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        ProductInformation current = data.get(position);
        holder.prodIcon.setImageResource(current.iconId);
        holder.prodName.setText(current.title);

    }

    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;
    }

    public void Delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView prodName;
        ImageView prodIcon;
        public myViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            prodName = (TextView) itemView.findViewById(R.id.prodname);
            prodIcon = (ImageView) itemView.findViewById(R.id.prodicon);

            //prodIcon.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context,"Item click at: "+getPosition(),Toast.LENGTH_SHORT).show();
            if(clickListener != null){
               clickListener.itemClicked(v, getPosition());
            }
           // Delete(getPosition());
        }

    }

    public interface ClickListener {
        public void itemClicked(View view, int position);
    }
}
