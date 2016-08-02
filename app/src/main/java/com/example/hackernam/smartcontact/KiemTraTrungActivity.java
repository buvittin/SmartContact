package com.example.hackernam.smartcontact;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hackernam.smartcontact.lib.ContactHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class KiemTraTrungActivity extends AppCompatActivity {

    Button btnKiemTraTrung;
    TextView txtThongTin;
    TextView txtTongSoDanhBa;
    ArrayList<String> conPhoneID;
    ArrayList<String> conNames;
    ArrayList<String> conNumber;
    ArrayList<String> conPhoneIDXoa;
    ArrayList<String> conSoDienThoaiTrung;
    Cursor crContacts;
    Cursor crDanhSachTrung;

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
        setContentView(R.layout.activity_kiem_tra_trung);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        conNames = new ArrayList<String>();
        conPhoneID = new ArrayList<String>();
        conNumber = new ArrayList<String>();
        conPhoneIDXoa = new ArrayList<String>();
        conSoDienThoaiTrung = new ArrayList<String>();

        btnKiemTraTrung = (Button) findViewById(R.id.btnKiemTraTrung);
        txtThongTin = (TextView) findViewById(R.id.txtThongTin);
        txtTongSoDanhBa = (TextView) findViewById(R.id.txtTongSoDanhBa);
        String html = "";

        crContacts = ContactHelper.getContactCursor(getContentResolver(), "",0);
        crContacts.moveToFirst();

        while (!crContacts.isAfterLast()) {
            conNumber.add(crContacts.getString(3));
            crContacts.moveToNext();
        }
        txtTongSoDanhBa.setText(String.valueOf(crContacts.getCount()));
        for(int i=0;i<conNumber.size();i++)
        {
            for(int j=i+1;j<conNumber.size();j++)
            {
                if(conNumber.get(i).toString().equals(conNumber.get(j).toString()) && KiemTraTonTai(conSoDienThoaiTrung,conNumber.get(i).toString()) == 0)
                {
                    conSoDienThoaiTrung.add(conNumber.get(i).toString());
                    break;
                }
            }
        }
        String test = "";
        for(int i =0;i<conSoDienThoaiTrung.size();i++) {
            crDanhSachTrung = null;
            String TenDanhBa = "";
            String SoDienThoai = "";
            int SoLuong = 0;
            crDanhSachTrung = ContactHelper.getContactByNumberCursor(getContentResolver(), conSoDienThoaiTrung.get(i).toString());
            crDanhSachTrung.moveToFirst();

            while (!crDanhSachTrung.isAfterLast()) {
                TenDanhBa = TenDanhBa + crDanhSachTrung.getString(1).toString()+", ";
                SoLuong = SoLuong + 1;
                SoDienThoai = crDanhSachTrung.getString(3).toString();
                crDanhSachTrung.moveToNext();
            }
            html = html + "<p><strong>Tên danh bạ trùng: <span style='color:#ff0000;'>"+ TenDanhBa.toString() + "</span></strong></p>\n" +
                    "<p>Số lượng trùng: "+ String.valueOf(SoLuong) +"</p>\n"+
                    "<p>Số điện thoại trùng: "+ SoDienThoai +"</p>\n<br />";
        }


        txtThongTin.setText(Html.fromHtml(html));


        btnKiemTraTrung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i =0;i<conSoDienThoaiTrung.size();i++) {
                    crDanhSachTrung = null;
                    conPhoneIDXoa.clear();
                    crDanhSachTrung = ContactHelper.getContactByNumberCursor(getContentResolver(), conSoDienThoaiTrung.get(i).toString());
                    crDanhSachTrung.moveToFirst();

                    while (!crDanhSachTrung.isAfterLast()) {
                        conPhoneIDXoa.add(crDanhSachTrung.getString(0));
                        crDanhSachTrung.moveToNext();
                    }
                    for(int k=0;k<conPhoneIDXoa.size();k++)
                    {
                        if(k != 0) {
                            String PhoneID = conPhoneIDXoa.get(k).toString();
                            ContactHelper.deleteContact(getContentResolver(), PhoneID.toString());
                        }
                    }
                }
                txtThongTin.setText("");
                Toast.makeText(getApplicationContext(), "Ghép danh bạ thành công !", Toast.LENGTH_LONG).show();
            }
        });
    }

    public int KiemTraTonTai(ArrayList<String> DanhSach,String chuoi)
    {
        for(int i=0;i<DanhSach.size();i++)
        {
            if(DanhSach.get(i).toString().equals(chuoi.toString()))
            {
                return 1;
            }
        }
        return 0;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == android.R.id.home)
        {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
