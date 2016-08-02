package com.example.hackernam.smartcontact;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class CaiDatActivity extends Activity {

    String[] arraySapXep;
    Spinner spSapXep;
    Button btnApDung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cai_dat);

        arraySapXep = new String[] {
                "Tăng dần", "Giảm dần"
        };

        spSapXep = (Spinner) findViewById(R.id.spSapXep);
        btnApDung = (Button) findViewById(R.id.btnThayDoi);

        ArrayAdapter adapterThang = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arraySapXep);
        adapterThang.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        spSapXep.setAdapter(adapterThang);

        btnApDung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Loai = spSapXep.getSelectedItemPosition();
                if(Loai == 0)
                {
                    SharedPreferences sharedPreferences= getSharedPreferences("CaiDatSapXep", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putInt("SapXep", 0);
                    editor.apply();

                    finish();
                }
                if(Loai == 1)
                {
                    SharedPreferences sharedPreferences= getSharedPreferences("CaiDatSapXep", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putInt("SapXep", 1);
                    editor.apply();

                    finish();
                }
            }
        });
    }
}
