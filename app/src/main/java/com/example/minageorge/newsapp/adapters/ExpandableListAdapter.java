package com.example.minageorge.newsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.minageorge.newsapp.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by mina george on 01-Jul-17.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<String> dataHeader;
    private HashMap<String, List<String>> dataChild;

    public ExpandableListAdapter(Context context, List<String> dataHeader, HashMap<String,
            List<String>> dataChild) {
        this.mContext = context;
        this.dataHeader = dataHeader;
        this.dataChild = dataChild;
    }

    @Override
    public int getGroupCount() {
        return this.dataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (this.dataChild.get(this.dataHeader.get(groupPosition)) != null)
            return this.dataChild.get(this.dataHeader.get(groupPosition)).size();
        else return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.dataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.dataChild.get(this.dataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String HeaderTitle = (String) getGroup(groupPosition);
        LayoutInflater layoutInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.expandable_header, null);
        TextView textView = (TextView) convertView.findViewById(R.id.expandable_header);
        textView.setText(HeaderTitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String headerItems = (String) getChild(groupPosition, childPosition);
        LayoutInflater layoutInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.expandable_header_items, null);
        TextView textView = (TextView) convertView.findViewById(R.id.expandable_item);
        textView.setText(headerItems);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
