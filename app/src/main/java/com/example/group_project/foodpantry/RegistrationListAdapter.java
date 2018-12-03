package com.example.group_project.foodpantry;

import android.arch.lifecycle.ViewModel;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class RegistrationListAdapter extends RecyclerView.Adapter<RegistrationListAdapter.ViewHolder> {

    private List<Registration> items;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mNameTextView;
        public TextView mAddressTextView;
        public TextView mEmailTextView;
        public TextView mPhoneTextView;
        public TextView mWebsiteTextView;
        public TextView mIsEventTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            mNameTextView = itemView.findViewById(R.id.name_text_view);
            mAddressTextView = itemView.findViewById(R.id.address_text_view);
            mEmailTextView = itemView.findViewById(R.id.email_text_view);
            mPhoneTextView = itemView.findViewById(R.id.phone_text_view);
            mWebsiteTextView = itemView.findViewById(R.id.website_text_view);
            mIsEventTextView = itemView.findViewById(R.id.is_event_text_view);
        }
    }

    public RegistrationListAdapter(List<Registration> items) {
        this.items = items;
    }

    @Override
    public RegistrationListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reg_list_item_layout, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(mDataset[position]);
        Registration item = items.get(position);
        holder.mNameTextView.setText(item.getName());
        holder.mWebsiteTextView.setText(item.getWebsite());
        holder.mPhoneTextView.setText(item.getPhoneNumber());
        holder.mEmailTextView.setText(item.getEmailAddress());
        holder.mAddressTextView.setText(item.getAddress());

        String isEventString = item instanceof Pantry ? "PANTRY" : "EVENT";

        holder.mIsEventTextView.setText(isEventString);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
