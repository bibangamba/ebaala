package com.bibangamba.ebaala.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.bibangamba.ebaala.R;
import com.bibangamba.ebaala.model.Event;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by davy on 4/18/2018.
 */

public class EventAdapter extends BaseExpandableListAdapter {

    private List<String> listHeader;
    private HashMap<String, List<Event>> listData;
    private Context context;

    public EventAdapter(List<String> listHeader, HashMap<String, List<Event>> listData, Context context) {
        this.listHeader = listHeader;
        this.listData = listData;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return this.listHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listData.get(this.listHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listData.get(this.listHeader.get(groupPosition)).get(childPosition);
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        final String eventTitle = (String) getGroup(groupPosition);

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
        }

        TextView listHeader = (TextView) convertView.findViewById(R.id.list_header);
        listHeader.setText(eventTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        Event event = (Event) getChild(groupPosition, childPosition);

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.event, null);
        }

        TextView eventDay = (TextView) convertView.findViewById(R.id.event_day);
        TextView eventName = (TextView) convertView.findViewById(R.id.event_name);
        TextView eventTime = (TextView) convertView.findViewById(R.id.event_time);
        TextView eventHappening = (TextView) convertView.findViewById(R.id.happeningtv);

        eventDay.setText(event.getDay());
        eventName.setText(event.getName());
        eventTime.setText(event.getTime());
        eventHappening.setText(event.getHappening());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
