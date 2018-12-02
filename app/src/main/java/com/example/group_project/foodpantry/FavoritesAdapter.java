package com.example.group_project.foodpantry;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FavoritesAdapter extends BaseAdapter {

    private final List<Registration> mItems = new ArrayList<Registration>();
    private final Context mContext;

    private static final String TAG = "Lab-UserInterface";

    public FavoritesAdapter(Context context) {

        mContext = context;

    }

    // Add a ToDoItem to the adapter
    // Notify observers that the data set has changed

    public void add(Registration item) {

        mItems.add(item);
        notifyDataSetChanged();

    }

    public void remove(Registration item) {

        mItems.remove(item);
        notifyDataSetChanged();

    }


    // Clears the list adapter of all items.

    public void clear() {

        mItems.clear();
        notifyDataSetChanged();

    }

    // Returns the number of ToDoItems

    @Override
    public int getCount() {

        return mItems.size();

    }

    // Retrieve the number of ToDoItems

    @Override
    public Object getItem(int pos) {

        return mItems.get(pos);

    }

    // Get the ID for the ToDoItem
    // In this case it's just the position

    @Override
    public long getItemId(int pos) {

        return pos;

    }

    // TODO - Create a View for the ToDoItem at specified position
    // Remember to check whether convertView holds an already allocated View
    // before created a new View.
    // Consider using the ViewHolder pattern to make scrolling more efficient
    // See: http://developer.android.com/training/improving-layouts/smooth-scrolling.html

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View dataView = convertView;
        LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);


        ViewHolder stashedViewHolder = (ViewHolder) dataView.getTag();

        Registration mItem = (Registration) getItem(position);


        return dataView;

    }

    static class ViewHolder {
        int position;
        RelativeLayout mItemLayout;
        TextView mTitleView;
        CheckBox mStatusView;
        TextView mPriorityView;
        TextView mDateView;
    }
}
