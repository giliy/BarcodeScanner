package com.barcodescanner.gili.scan9.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.barcodescanner.gili.scan9.TitleInformation;
import com.barcodescanner.gili.scan9.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by gili on 6/16/2015.
 */
public class AdapterDrawer extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflator;
    private List<TitleInformation> data = Collections.emptyList();
    private static final int  TYPE_HEADER = 0;
    private static final int  TYPE_ITEM = 1;

    private Context context;
    private ClickListener clickListener;

    public AdapterDrawer(Context context, List<TitleInformation> data){
        this.context = context;
        inflator = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER){
            View view = inflator.inflate(R.layout.drawer_header, parent, false);
            HeaderHolder holder = new HeaderHolder(view);
            return holder;
        }
        else {
            View view = inflator.inflate(R.layout.item_drawer, parent, false);
            ItemHolder holder = new ItemHolder(view);
            return holder;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof HeaderHolder){

        }
        else
        {
            ItemHolder itemHolder = (ItemHolder)holder;
            TitleInformation current = data.get(position-1);
            itemHolder.prodIcon.setImageResource(current.iconId);
            itemHolder.prodName.setText(current.title);
        }


    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return TYPE_HEADER;
        }
        else {
            return TYPE_ITEM;
        }
    }

    public void Delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return data.size()+1;
    }

    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView prodName;
        ImageView prodIcon;
        public ItemHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            prodName = (TextView) itemView.findViewById(R.id.prodName);
            prodIcon = (ImageView) itemView.findViewById(R.id.prodPic);

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

    class HeaderHolder extends RecyclerView.ViewHolder {

        public HeaderHolder(View itemView) {
            super(itemView);
        }

    }


}
