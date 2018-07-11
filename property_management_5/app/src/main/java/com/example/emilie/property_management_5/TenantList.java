package com.example.emilie.property_management_5;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TenantList extends ArrayAdapter<Tenant> {

    private Activity context;
    private ArrayList<Tenant> tenants;

    public TenantList(Activity context, ArrayList<Tenant> tenants){
        super(context, R.layout.activity_tenant_list, tenants);
        this.context = context;
        this.tenants = tenants;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_tenant_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.text_view_name);
        TextView textViewContact = (TextView) listViewItem.findViewById(R.id.text_view_contact);
        TextView textViewEmail = (TextView) listViewItem.findViewById(R.id.text_view_email);


        Tenant tenant = tenants.get(position);
        textViewName.setText(tenant.getName());
        textViewContact.setText(tenant.getContactNumber());
        textViewEmail.setText(tenant.getEmail());



        return listViewItem;
    }

    /* To update the size of the array */
    /* Without this, will cause java.lang.IndexOutOfBoundException error */
    @Override
    public int getCount(){
        return tenants.size();
    }

    public void setFilter( ArrayList<Tenant> newList )
    {
        tenants = new ArrayList<>();
        tenants.addAll( newList );
        notifyDataSetChanged();
    }
}