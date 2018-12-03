package com.example.group_project.foodpantry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class RegistrationListAdapter extends BaseAdapter {

    private List<Registration> items;
    private final Context mContext;


    RegistrationListAdapter(List<Registration> items, Context context) {
        this.items = items;
        this.mContext = context;
    }

    public void add(Registration item) {

        items.add(item);
        notifyDataSetChanged();

    }

    public void remove(int pos) {
        items.remove(pos);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View dataView = convertView;
        LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);

        if (dataView == null) {
            dataView = mLayoutInflater.inflate(R.layout.reg_list_item_layout, parent, false);
            ViewHolder vh = new ViewHolder(dataView);
            vh.position = position;

            dataView.setTag(vh);
        }

        ViewHolder holder = (ViewHolder) dataView.getTag();

        Registration item = items.get(position);

        holder.mNameTextView.setText(item.getName());
        holder.mWebsiteTextView.setText(item.getWebsite());
        holder.mPhoneTextView.setText(item.getPhoneNumber());
        holder.mEmailTextView.setText(item.getEmailAddress());
        holder.mAddressTextView.setText(item.getAddress());

        String isEventString = item instanceof Pantry ? "PANTRY" : "EVENT";

        holder.mIsEventTextView.setText(isEventString);

        return dataView;
    }

        @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int pos) {
        return items.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }



    public static class ViewHolder {
        // each data item is just a string in this case
        TextView mNameTextView;
        TextView mAddressTextView;
        TextView mEmailTextView;
        TextView mPhoneTextView;
        TextView mWebsiteTextView;
        TextView mIsEventTextView;
        int position;
        View v;

        ViewHolder(View itemView) {
            this.v = itemView;

            mNameTextView = itemView.findViewById(R.id.name_text_view);
            mAddressTextView = itemView.findViewById(R.id.address_text_view);
            mEmailTextView = itemView.findViewById(R.id.email_text_view);
            mPhoneTextView = itemView.findViewById(R.id.phone_text_view);
            mWebsiteTextView = itemView.findViewById(R.id.website_text_view);
            mIsEventTextView = itemView.findViewById(R.id.is_event_text_view);
        }

    }
}
