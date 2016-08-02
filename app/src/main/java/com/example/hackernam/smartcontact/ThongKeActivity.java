package com.example.hackernam.smartcontact;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.hackernam.smartcontact.lib.CallLogHelper;
import com.example.hackernam.smartcontact.lib.ContactHelper;
import com.example.hackernam.smartcontact.lib.XuLyBitmap;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class ThongKeActivity extends AppCompatActivity implements View.OnClickListener {

    String[] arrayThang;
    String[] arrayNam;
    Spinner spThang;
    Spinner spNam;
    Button btnThongKe;
    Cursor cur;
    ArrayList<String> conPhoneID;
    ArrayList<String> conNames;
    ArrayList<String> conImage;
    ArrayList<String> conLookUpKey;
    ArrayList<Integer> conTrangThaiCuocGoi;
    ArrayList<Long> conNgayThang;
    ArrayList<String> conNumber;
    ListView lvThongKe;
    TextView txtTongSoLanLienLac;
    TextView txtTongThoiGianGoi;
    TextView txtSoCuocGoiDen;
    TextView txtSoCuocGoiDi;
    TextView txtSoCuocGoiNho;

    int TongThoiGianGoi;
    int SoCuocGoiDen;
    int SoCuocGoiDi;
    int SoCuocGoiNho;

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
        setContentView(R.layout.activity_thong_ke);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        conNames = new ArrayList<String>();
        conImage = new ArrayList<String>();
        conPhoneID = new ArrayList<String>();
        conLookUpKey = new ArrayList<String>();
        conTrangThaiCuocGoi = new ArrayList<Integer>();
        conNgayThang = new ArrayList<Long>();
        conNumber = new ArrayList<String>();


        arrayThang = new String[] {
                "1", "2", "3", "4", "5","6","7","8","9","10","11","12"
        };

        arrayNam = new String[] {
                "2014", "2015", "2016"
        };

        spThang = (Spinner) findViewById(R.id.spThang);
        spNam = (Spinner) findViewById(R.id.spNam);
        btnThongKe = (Button) findViewById(R.id.btnThongKe);
        lvThongKe = (ListView) findViewById(R.id.lvThongKe);
        txtTongSoLanLienLac = (TextView) findViewById(R.id.txtTongSoLanLienLac);
        txtTongThoiGianGoi = (TextView) findViewById(R.id.txtTongThoiGianGoi);
        txtSoCuocGoiDen = (TextView) findViewById(R.id.txtSoCuocGoiDen);
        txtSoCuocGoiDi = (TextView) findViewById(R.id.txtSoCuocGoiDi);
        txtSoCuocGoiNho = (TextView) findViewById(R.id.txtSoCuocGoiNho);

        ArrayAdapter adapterThang = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayThang);
        adapterThang.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);
        spThang.setAdapter(adapterThang);
        ArrayAdapter adapterNam = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayNam);
        adapterNam.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);
        spNam.setAdapter(adapterNam);
        btnThongKe.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home)
        {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == btnThongKe.getId())
        {


            SoCuocGoiDi = 0;
            SoCuocGoiDen = 0;
            SoCuocGoiNho = 0;
            TongThoiGianGoi = 0;
            conNames.clear();
            conImage.clear();
            conPhoneID.clear();
            conLookUpKey.clear();
            conTrangThaiCuocGoi.clear();
            conNgayThang.clear();
            conNumber.clear();
            Integer Thang = Integer.parseInt(spThang.getSelectedItem().toString());
            Integer Nam = Integer.parseInt(spNam.getSelectedItem().toString());
            cur = CallLogHelper.getCallLogByDateCursor(getApplicationContext().getContentResolver(),Thang,Nam);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                conNames.add(cur.getString(1));
                conTrangThaiCuocGoi.add(cur.getInt(3));
                conNgayThang.add(cur.getLong(4));
                conNumber.add(cur.getString(2));
                conPhoneID.add(cur.getString(0));

                TongThoiGianGoi = TongThoiGianGoi + Integer.parseInt(cur.getString(5).toString());

                if(cur.getInt(3) == CallLog.Calls.INCOMING_TYPE)
                {
                    SoCuocGoiDen = SoCuocGoiDen + 1;
                }
                if(cur.getInt(3) == CallLog.Calls.OUTGOING_TYPE)
                {
                    SoCuocGoiDi = SoCuocGoiDi + 1;
                }
                if(cur.getInt(3) == CallLog.Calls.MISSED_TYPE)
                {
                    SoCuocGoiNho = SoCuocGoiNho + 1;
                }

                cur.moveToNext();
            }
            txtTongSoLanLienLac.setText(cur.getCount() + " lần");
            lvThongKe.setAdapter(new MyAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, R.id.lblTenDanhBa, conNames));
            txtSoCuocGoiDen.setText(SoCuocGoiDen + " lần");
            txtSoCuocGoiDi.setText(SoCuocGoiDi + " lần");
            txtSoCuocGoiNho.setText(SoCuocGoiNho + " lần");
            txtTongThoiGianGoi.setText(TongThoiGianGoi+ " s");

            lvThongKe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    long PhoneID = ContactHelper.getContactID(getContentResolver(), conNumber.get(position).toString());
                    Intent intent = new Intent(getApplicationContext(), ChiTietDanhBaActivity.class);
                    intent.putExtra("ContactID",Long.toString(PhoneID));
                    startActivity(intent);
                }
            });
        }
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
            LayoutInflater inf = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row = inf.inflate(R.layout.thongke_list_item, parent, false);

            TextView tvName = (TextView) row.findViewById(R.id.txtTenDanhBa);
            ImageView tvImage = (ImageView) row.findViewById(R.id.imvHinhDaiDien);
            TextView tvKyTuDau = (TextView) row.findViewById(R.id.txtKyTuDau);
            TextView tvTrangThai = (TextView) row.findViewById(R.id.txtTrangThaiCuocGoi);
            TextView tvNgayThang = (TextView) row.findViewById(R.id.txtNgayThucHien);
            if(conNames.get(position) == null)
            {
                tvName.setText(conNumber.get(position));
            }
            else {
                tvName.setText(conNames.get(position));
            }
            String KyTuDau = "";
            if(conNames.get(position) == null)
            {
                KyTuDau = "#";
            }
            else {
                KyTuDau = conNames.get(position).substring(0, 1).toUpperCase();
            }
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
            Bitmap myBitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.RGB_565);
            Paint paint = new Paint();
            Canvas canvas = new Canvas(myBitmap);
            ColorFilter filter = new PorterDuffColorFilter(getResources().getColor(MaMau), PorterDuff.Mode.SRC_IN);
            paint.setColorFilter(filter);
            canvas.drawBitmap(myBitmap, 0, 0, paint);
            tvKyTuDau.setText(KyTuDau.toString());
            tvImage.setImageBitmap(XuLyBitmap.getRoundedBitmap(myBitmap));
            if(conTrangThaiCuocGoi.get(position) == CallLog.Calls.INCOMING_TYPE)
            {
                tvTrangThai.setText("Cuộc gọi đến");
                tvTrangThai.setTextColor(Color.GREEN);
            }
            if(conTrangThaiCuocGoi.get(position) == CallLog.Calls.OUTGOING_TYPE)
            {
                tvTrangThai.setText("Cuộc gọi đi");
                tvTrangThai.setTextColor(Color.BLUE);
            }
            if(conTrangThaiCuocGoi.get(position) == CallLog.Calls.MISSED_TYPE)
            {
                tvTrangThai.setText("Cuộc gọi nhỡ");
                tvTrangThai.setTextColor(Color.RED);
            }
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd/MM/yyyy");
            String dateString = formatter.format(new Date(Long.parseLong(conNgayThang.get(position).toString())));

            tvNgayThang.setText(dateString);

            return row;
        }

    }
}
