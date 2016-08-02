package com.example.hackernam.smartcontact;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hackernam.smartcontact.lib.ContactHelper;

public class ChiTietDanhBaActivity extends AppCompatActivity implements View.OnClickListener {

    Cursor crContacts;
    TextView lblDanhBa;
    ImageView imgHinhDaiDien;
    TextView lblSoDienThoai;
    TextView lblLoaiDienThoai;
    ImageView imgNhanTin;
    LinearLayout llayoutSoDienThoai;
    String ContactID;
    String LookupKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int GiaoDien = 0;
        SharedPreferences sharedPreferences= this.getSharedPreferences("CaiDatGiaoDien", Context.MODE_PRIVATE);
        if(sharedPreferences!= null) {
            GiaoDien = sharedPreferences.getInt("MaGiaoDien", 2);
        }
        else
        {
            GiaoDien = 1;
        }
        if(GiaoDien == 1) {
            setTheme(R.style.AppActionBarColor1);
        }
        if(GiaoDien == 2) {
            setTheme(R.style.AppActionBarColor2);
        }
        if(GiaoDien == 3) {
            setTheme(R.style.AppActionBarColor3);
        }
        if(GiaoDien == 4) {
            setTheme(R.style.AppActionBarColor4);
        }
        if(GiaoDien == 5) {
            setTheme(R.style.AppActionBarColor5);
        }
        if(GiaoDien == 6) {
            setTheme(R.style.AppActionBarColor6);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_danh_ba);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        lblDanhBa = (TextView) findViewById(R.id.lblTenDanhBa);
        imgHinhDaiDien = (ImageView) findViewById(R.id.imgHinhDaiDien);
        lblSoDienThoai = (TextView) findViewById(R.id.lblSoDienThoai);
        lblLoaiDienThoai = (TextView) findViewById(R.id.lblLoaiDienThoai);
        imgNhanTin = (ImageView) findViewById(R.id.imgNhanTin);
        llayoutSoDienThoai = (LinearLayout) findViewById(R.id.LLayoutSoDienThoai);

        ContactID= getIntent().getStringExtra("ContactID");

        crContacts = ContactHelper.getContactToIDCursor(getApplication().getContentResolver(), ContactID);
        String TenDanhBa = crContacts.getString(1);
        String SoDienThoai = crContacts.getString(3);
        String LoaiDienThoai = crContacts.getString(4);
        LookupKey = crContacts.getString(5);

        if(LoaiDienThoai.equals("2"))
        {
            LoaiDienThoai = "Di động";
        }
        else
        {
            LoaiDienThoai = "Điện thoại bàn";
        }

        lblDanhBa.setText(TenDanhBa);
        lblSoDienThoai.setText(SoDienThoai);
        lblLoaiDienThoai.setText(LoaiDienThoai);


        if(crContacts.isNull(2))
        {
            imgHinhDaiDien.setImageResource(R.drawable.avatar);

        }
        else
        {
            Uri imgUri = Uri.parse(crContacts.getString(2));
            imgHinhDaiDien.setImageURI(imgUri);
        }

        imgNhanTin.setOnClickListener(this);
        llayoutSoDienThoai.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == imgNhanTin.getId())
        {
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("address", lblSoDienThoai.getText().toString());
            smsIntent.putExtra("sms_body","");
            startActivity(smsIntent);
        }
        if(v.getId() == llayoutSoDienThoai.getId())
        {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + lblSoDienThoai.getText().toString()));
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chitiet_danhba, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_suadanhba) {
            Uri mContactUri = ContactsContract.Contacts.getLookupUri(Long.parseLong(ContactID.toString()),LookupKey.toString());

            Intent intent = new Intent(Intent.ACTION_EDIT, mContactUri);

            intent.putExtra("finishActivityOnSaveCompleted", true);

            startActivity(intent);
            return true;
        }
        if(id == R.id.action_guiemail)
        {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
            intent.putExtra(Intent.EXTRA_SUBJECT, lblDanhBa.getText().toString());
            intent.putExtra(Intent.EXTRA_TEXT, lblDanhBa.getText().toString() + " - "+lblSoDienThoai.getText().toString());
            startActivity(Intent.createChooser(intent, ""));
        }
        if(id == android.R.id.home)
        {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
