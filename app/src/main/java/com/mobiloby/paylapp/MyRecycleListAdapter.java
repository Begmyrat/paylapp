package com.mobiloby.paylapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyRecycleListAdapter extends RecyclerView.Adapter<MyRecycleListAdapter.ViewHolder> {

    private Activity context;
    private ArrayList<MenuObject> list;
    int selected_pos;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    MyRecycleListAdapter(Activity context, ArrayList<MenuObject> list) {
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_list_menu, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(position%5==0) {
            holder.textview_title.setText(list.get(position).getName());
            holder.textview_number.setText("+15");
            holder.layout_box.getBackground().setTint(context.getResources().getColor(R.color.colorPalet2));
            holder.imageview.setImageResource(R.drawable.ic_3249752);
        }
        else if(position%5==1) {
            holder.textview_title.setText(list.get(position).getName());
            holder.textview_number.setText("+50");
            holder.layout_box.getBackground().setTint(context.getResources().getColor(R.color.colorPalet1));
            holder.imageview.setImageResource(R.drawable.ic_4004989);
        }
        else if(position%5==2) {
            holder.layout_box.getBackground().setTint(context.getResources().getColor(R.color.colorPalet4));
            holder.imageview.setImageResource(R.drawable.ic_4535225);
        }
        else if(position%5==3) {
            holder.layout_box.getBackground().setTint(context.getResources().getColor(R.color.colorPalet5));
            holder.imageview.setImageResource(R.drawable.ic_3568951);
        }
        else if(position%5==4)
            holder.layout_box.getBackground().setTint(context.getResources().getColor(R.color.colorPalet5));

//        holder.textview_title.setTypeface(Typeface.DEFAULT);

//        if(position==evrak.p){
//            holder.textview_title.setTypeface(Typeface.DEFAULT_BOLD);
//        }
//        else{
//            holder.textview_title.setTypeface(Typeface.DEFAULT);
//        }
//
//        if(position%3==0){
//            holder.layout_box.setBackgroundResource(R.drawable.ic_20944966);;
//        }
//        else if(position%3==1){
//            holder.layout_box.getBackground().setTint(context.getResources().getColor(R.color.colorYellow));
//        }
//        if(position==2){
//            holder.imageview_topLeft.setImageResource(R.drawable.ic_20944966);
//        }



    }

    // total number of rows
    @Override
    public int getItemCount() {
        return list.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textview_title, textview_subtitle, textview_number;
        RelativeLayout layout_box;
        ImageView imageview;

        ViewHolder(View itemView) {
            super(itemView);
            textview_title = itemView.findViewById(R.id.t_title);
            textview_subtitle = itemView.findViewById(R.id.t_subtitle);
            layout_box = itemView.findViewById(R.id.r_box);
            textview_number = itemView.findViewById(R.id.t_number);
            imageview = itemView.findViewById(R.id.i_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public MenuObject getItem(int id) {
        return list.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;

    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}

