package com.example.hackernam.smartcontact;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hackernam.smartcontact.lib.ContactHelper;
import com.example.hackernam.smartcontact.lib.XuLyBitmap;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ChiTietLichSuDanhBaActivity extends AppCompatActivity implements View.OnClickListener {
    String ContactID;
    Cursor cur;
    TextView tvTenDanhBa;
    TextView tvSoDienThoai;
    ImageView imgNhanTin;
    LinearLayout llayoutSoDienThoai;
    TextView txtNhatKy;
    LinearLayout linearChiTiet;
    ImageView imgHinhDaiDien;

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
        setContentView(R.layout.activity_chi_tiet_lich_su_danh_ba);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ContactID= getIntent().getStringExtra("ContactID");

        Uri callUri = Uri.parse("content://call_log/calls");
        cur = getContentResolver().query(callUri, null, CallLog.Calls._ID + "=?", new String[] { ContactID.toString() }, null);

        tvTenDanhBa = (TextView) findViewById(R.id.lblTenDanhBa);
        tvSoDienThoai = (TextView) findViewById(R.id.lblSoDienThoai);
        imgNhanTin = (ImageView) findViewById(R.id.imgNhanTin);
        llayoutSoDienThoai = (LinearLayout) findViewById(R.id.LLayoutSoDienThoai);
        txtNhatKy = (TextView) findViewById(R.id.txtNhatKy);
        linearChiTiet = (LinearLayout) findViewById(R.id.linearChiTiet);
        imgHinhDaiDien = (ImageView) findViewById(R.id.imgHinhDaiDien);
        String html = "";
        while (cur.moveToNext()) {
            tvTenDanhBa.setText(cur.getString(cur.getColumnIndex(CallLog.Calls.CACHED_NAME)));
            tvSoDienThoai.setText(cur.getString(cur.getColumnIndex(CallLog.Calls.NUMBER)));
            int TrangThaiCuocGoi = cur.getInt(cur.getColumnIndex(android.provider.CallLog.Calls.TYPE));
            String LoaiTrangThai = "";
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd/MM/yyyy");
            String dateString = formatter.format(new Date(Long.parseLong(cur.getString(cur.getColumnIndex(CallLog.Calls.DATE)))));
            String duration = cur.getString(cur.getColumnIndex(android.provider.CallLog.Calls.DURATION));
            if(TrangThaiCuocGoi == CallLog.Calls.INCOMING_TYPE)
            {
                LoaiTrangThai = "Cuộc gọi đến";
            }
            if(TrangThaiCuocGoi == CallLog.Calls.OUTGOING_TYPE)
            {
                LoaiTrangThai = "Cuộc gọi đi";
            }
            if(TrangThaiCuocGoi == CallLog.Calls.MISSED_TYPE)
            {
                LoaiTrangThai = "Cuộc gọi nhỡ";
            }
            html = "<p><strong><span style='color:#ff0000;'>"+ LoaiTrangThai + "</span></strong></p>\n" +
                    "<p>Thời gian: "+ dateString +"</p>\n" +
                    "<p>Thời gian gọi: " + duration + "s</p>\n";
        }
        txtNhatKy.setText(Html.fromHtml(html));

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
        Bitmap myBitmap = Bitmap.createBitmap(80, 80, Bitmap.Config.RGB_565);
        Paint paint = new Paint();
        Canvas canvas = new Canvas(myBitmap);
        ColorFilter filter = new PorterDuffColorFilter(getResources().getColor(MaMau), PorterDuff.Mode.SRC_IN);
        paint.setColorFilter(filter);
        canvas.drawBitmap(myBitmap, 0, 0, paint);
        imgHinhDaiDien.setImageBitmap(XuLyBitmap.getRoundedBitmap(myBitmap));

        imgNhanTin.setOnClickListener(this);
        llayoutSoDienThoai.setOnClickListener(this);
        linearChiTiet.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_suadanhba) {
            return true;
        }
        if(id == android.R.id.home)
        {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == imgNhanTin.getId())
        {
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("address", tvSoDienThoai.getText().toString());
            smsIntent.putExtra("sms_body","");
            startActivity(smsIntent);
        }
        if(v.getId() == llayoutSoDienThoai.getId())
        {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tvSoDienThoai.getText().toString()));
            startActivity(intent);
        }
        if(v.getId() == linearChiTiet.getId())
        {
            long PhoneID = ContactHelper.getContactID(getContentResolver(),tvSoDienThoai.getText().toString());
            Intent intent = new Intent(getApplicationContext(), ChiTietDanhBaActivity.class);
            intent.putExtra("ContactID",Long.toString(PhoneID));
            startActivity(intent);
        }
    }
}
