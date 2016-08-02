package com.example.hackernam.smartcontact;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hackernam.smartcontact.lib.ContactHelper;
import com.example.hackernam.smartcontact.lib.CustomGrid;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabDanhBaYeuThichFragment extends Fragment {


    public TabDanhBaYeuThichFragment() {
        // Required empty public constructor
    }

    ArrayList<String> conPhoneID;
    ArrayList<String> conNames;
    ArrayList<String> conImage;
    ArrayList<String> conLookUpKey;
    ArrayList<String> conNumber;
    Cursor crContacts;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        conNames = new ArrayList<String>();
        conImage = new ArrayList<String>();
        conPhoneID = new ArrayList<String>();
        conLookUpKey = new ArrayList<String>();
        conNumber = new ArrayList<String>();

        crContacts = ContactHelper.getContactFavouritesCursor(getContext().getContentResolver(), "");
        crContacts.moveToFirst();

            while (!crContacts.isAfterLast()) {
                conNames.add(crContacts.getString(1));
                conLookUpKey.add(crContacts.getString(5));
                conNumber.add(crContacts.getString(3));
                if (crContacts.isNull(2)) {
                    conImage.add("no");
                } else {
                    conImage.add(crContacts.getString(2));
                }
                conPhoneID.add(crContacts.getString(0));
                crContacts.moveToNext();
            }

        View view = inflater.inflate(R.layout.fragment_tab_danh_ba_yeu_thich,container,false);
        GridView gridView = (GridView) view.findViewById(R.id.gridView1);

        CustomGrid adapter = new CustomGrid(view.getContext(), conNames, conImage);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + conNumber.get(position).toString()));
                startActivity(intent);
            }
        });

        return view;
    }
}
