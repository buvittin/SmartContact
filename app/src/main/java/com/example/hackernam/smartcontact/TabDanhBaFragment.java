package com.example.hackernam.smartcontact;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hackernam.smartcontact.lib.ContactHelper;
import com.example.hackernam.smartcontact.lib.XuLyBitmap;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabDanhBaFragment extends ListFragment implements SearchView.OnQueryTextListener {

    public TabDanhBaFragment() {
        // Required empty public constructor
    }

    ArrayList<String> conPhoneID;
    ArrayList<String> conNames;
    ArrayList<String> conImage;
    ArrayList<String> conLookUpKey;
    Cursor crContacts;
    SearchView svTimKiem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        conNames = new ArrayList<String>();
        conImage = new ArrayList<String>();
        conPhoneID = new ArrayList<String>();
        conLookUpKey = new ArrayList<String>();

        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.fragment_tab_danh_ba, container, false);

        int SapXep = 0;
        SharedPreferences sharedPreferences= getContext().getSharedPreferences("CaiDatSapXep", Context.MODE_PRIVATE);
        if(sharedPreferences!= null) {
            SapXep = sharedPreferences.getInt("SapXep", 1);
        }
        else
        {
            SapXep = 0;
        }

        crContacts = ContactHelper.getContactCursor(getContext().getContentResolver(), "",SapXep);
        crContacts.moveToFirst();

        while (!crContacts.isAfterLast()) {
            conNames.add(crContacts.getString(1));
            conLookUpKey.add(crContacts.getString(5));
            if(crContacts.isNull(2))
            {
                conImage.add("no");
            }
            else
            {
                conImage.add(crContacts.getString(2));
            }
            conPhoneID.add(crContacts.getString(0));
            crContacts.moveToNext();
        }
        setListAdapter(new MyAdapter(getContext(), android.R.layout.simple_list_item_1, R.id.lblTenDanhBa, conNames));

        svTimKiem = (SearchView) view.findViewById(R.id.svTimKiem);
        svTimKiem.clearFocus();

        svTimKiem.setOnQueryTextListener(this);
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle("Chọn hành động");
        menu.add(0, v.getId(), 0, "Chỉnh sửa");//groupId, itemId, order, title
        menu.add(0, v.getId(), 0, "Xóa");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerForContextMenu(getListView());
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int index = info.position;

        if(item.getTitle()=="Chỉnh sửa"){
            Uri mContactUri = ContactsContract.Contacts.getLookupUri(Long.parseLong(conPhoneID.get(index).toString()),conLookUpKey.get(index).toString());

            Intent intent = new Intent(Intent.ACTION_EDIT, mContactUri);

            intent.putExtra("finishActivityOnSaveCompleted", true);

            startActivity(intent);
        }
        else if(item.getTitle()=="Xóa"){
            final int a = index;
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Xóa danh bạ");
            builder.setMessage("Bạn có chắc chắn muốn xóa ?");
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //TODO
                    ContactHelper.deleteContact(getContext().getContentResolver(),conPhoneID.get(a).toString());
                    ReloadList();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //TODO
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }else{
            return false;
        }
        return true;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long id) {


                return false;
            }
        });

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        Intent intent = new Intent(getContext(), ChiTietDanhBaActivity.class);
        intent.putExtra("ContactID",conPhoneID.get(position).toString());
        startActivity(intent);

    }

    @Override
    public void onResume() {
        super.onResume();
        ReloadList();
    }

    public void ReloadList()
    {
        conNames.clear();
        conPhoneID.clear();
        conImage.clear();
        int SapXep = 0;
        SharedPreferences sharedPreferences= getContext().getSharedPreferences("CaiDatSapXep", Context.MODE_PRIVATE);
        if(sharedPreferences!= null) {
            SapXep = sharedPreferences.getInt("SapXep", 1);
        }
        else
        {
            SapXep = 0;
        }
        crContacts = ContactHelper.getContactCursor(getContext().getContentResolver(), "",SapXep);
        crContacts.moveToFirst();

        while (!crContacts.isAfterLast()) {
            conNames.add(crContacts.getString(1));
            if(crContacts.isNull(2))
            {
                conImage.add("no");
            }
            else
            {
                conImage.add(crContacts.getString(2));
            }
            conPhoneID.add(crContacts.getString(0));
            crContacts.moveToNext();
        }
        setListAdapter(new MyAdapter(getContext(), android.R.layout.simple_list_item_1, R.id.lblTenDanhBa, conNames));
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        MyAdapter sf = new MyAdapter(getContext(), android.R.layout.simple_list_item_1, R.id.lblTenDanhBa, conNames);
        sf.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        conNames.clear();
        conPhoneID.clear();
        conImage.clear();
        int SapXep = 0;
        SharedPreferences sharedPreferences= getContext().getSharedPreferences("CaiDatSapXep", Context.MODE_PRIVATE);
        if(sharedPreferences!= null) {
            SapXep = sharedPreferences.getInt("SapXep", 1);
        }
        else
        {
            SapXep = 0;
        }
        crContacts = ContactHelper.getContactCursor(getContext().getContentResolver(), newText,SapXep);
        crContacts.moveToFirst();

        while (!crContacts.isAfterLast()) {
            conNames.add(crContacts.getString(1));
            if(crContacts.isNull(2))
            {
                conImage.add("no");
            }
            else
            {
                conImage.add(crContacts.getString(2));
            }
            conPhoneID.add(crContacts.getString(0));
            crContacts.moveToNext();
        }
        setListAdapter(new MyAdapter(getContext(), android.R.layout.simple_list_item_1, R.id.lblTenDanhBa, conNames));
        return true;
    }


    private class MyAdapter extends ArrayAdapter<String> {

        public MyAdapter(Context context, int resource, int textViewResourceId,
                         ArrayList<String> conNames) {
            super(context, resource, textViewResourceId, conNames);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View row = setList(position, parent);
            return row;
        }

        private View setList(int position, ViewGroup parent) {
            LayoutInflater inf = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row = inf.inflate(R.layout.danhba_list_item, parent, false);

            TextView tvName = (TextView) row.findViewById(R.id.lblTenDanhBa);
            ImageView tvImage = (ImageView) row.findViewById(R.id.imgHinhDaiDien);
            TextView tvKyTuDau = (TextView) row.findViewById(R.id.txtChuCaiDauTien);

            tvName.setText(conNames.get(position));
            if(conImage.get(position) != "no") {
                Uri imgUri = Uri.parse(conImage.get(position));
                Bitmap bitmap;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imgUri);
                    tvImage.setImageBitmap(XuLyBitmap.getRoundedBitmap(bitmap));
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
            else
            {
                String KyTuDau = conNames.get(position).substring(0,1).toUpperCase();
                Random r = new Random();
                int rd = (r.nextInt(9));
                int MaMau = R.color.color1;
                if(rd == 1)
                {
                    MaMau = R.color.color1;
                }
                if(rd == 2)
                {
                    MaMau = R.color.color2;
                }
                if(rd == 3)
                {
                    MaMau = R.color.color3;
                }
                if(rd == 4)
                {
                    MaMau = R.color.color4;
                }
                if(rd == 5)
                {
                    MaMau = R.color.color5;
                }
                if(rd == 6)
                {
                    MaMau = R.color.color6;
                }
                if(rd == 7)
                {
                    MaMau = R.color.color7;
                }
                if(rd == 8)
                {
                    MaMau = R.color.color8;
                }
                if(rd == 9)
                {
                    MaMau = R.color.color9;
                }
                Bitmap myBitmap = Bitmap.createBitmap(70, 70, Bitmap.Config.RGB_565);
                Paint paint = new Paint();
                Canvas canvas = new Canvas(myBitmap);
                ColorFilter filter = new PorterDuffColorFilter(getResources().getColor(MaMau), PorterDuff.Mode.SRC_IN);
                paint.setColorFilter(filter);
                canvas.drawBitmap(myBitmap, 0, 0, paint);
                tvKyTuDau.setText(KyTuDau.toString());
                tvImage.setImageBitmap(XuLyBitmap.getRoundedBitmap(myBitmap));
            }

            return row;
        }

    }
}
