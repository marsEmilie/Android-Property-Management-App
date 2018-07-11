package com.example.emilie.property_management_5;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PropertyList extends ArrayAdapter<Property> {

    private Activity context;
    private ArrayList<Property> properties;

    public PropertyList(Activity context, ArrayList<Property> properties) {
        super(context, R.layout.layout_property_list, properties);
        this.context = context;
        this.properties = properties;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_property_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewProperty = (TextView) listViewItem.findViewById(R.id.textViewProperty);

        Property property = properties.get(position);
        textViewName.setText(property.getPropertyName());
        textViewProperty.setText(property.getPropertyType());



        return listViewItem;
    }

    /* To update the size of the array */
    /* Without this, will cause java.lang.IndexOutOfBoundException error */
    @Override
    public int getCount(){
        return properties.size();
    }

    public void setFilter( ArrayList<Property> newList )
    {
        properties = new ArrayList<>();
        properties.addAll( newList );
        notifyDataSetChanged();
    }
}