package com.mobiloby.paylapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyScoreListAdapter extends ArrayAdapter<UserObject> {

    private Activity context;
    private ArrayList<UserObject> list;

    public MyScoreListAdapter(Activity context, ArrayList<UserObject> list) {
        super(context, R.layout.item_list_zehinli, list);

        this.context = context;
        this.list = list;

    }

    static  class ViewHolder
    {
        TextView textview_name, textview_score, textview_position, textview_dolar;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null)
        {
            LayoutInflater inflator = context.getLayoutInflater();
            convertView = inflator.inflate(R.layout.item_list_zehinli, null);
            viewHolder = new ViewHolder();

            viewHolder.textview_name = convertView.findViewById(R.id.t_username);
            viewHolder.textview_score = convertView.findViewById(R.id.t_utuk);
            viewHolder.textview_position = convertView.findViewById(R.id.t_position);
            viewHolder.textview_dolar = convertView.findViewById(R.id.t_dolar);

            convertView.setTag(viewHolder);

        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (list != null && list.size() > 0)
        {
            final UserObject user = list.get(position);

            if(user!=null){
//                Toast.makeText(context, "adapter: " + user.getUsername(), Toast.LENGTH_SHORT).show();
                viewHolder.textview_name.setText(user.getUsername());
                viewHolder.textview_score.setText("Utuk: "+user.getOffScore());
                viewHolder.textview_position.setText(""+(position+1));
                viewHolder.textview_dolar.setText(user.getMoney());
            }

        }
        return convertView;
    }
}

