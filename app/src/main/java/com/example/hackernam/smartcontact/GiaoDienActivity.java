package com.example.hackernam.smartcontact;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.hackernam.smartcontact.lib.CustomGrid;
import com.example.hackernam.smartcontact.lib.CustomGridGiaoDien;

import java.util.ArrayList;

public class GiaoDienActivity extends Activity {

    ArrayList<String> conNames;
    ArrayList<String> conImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giao_dien);

        conNames = new ArrayList<String>();
        conImage = new ArrayList<String>();

        Button btnDoiGiaoDien = (Button) findViewById(R.id.btnDoiGiaoDien);
        btnDoiGiaoDien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        for(int i=1;i<7;i++)
        {
            conNames.add("Màu "+i);
            conImage.add(""+i);
        }

        GridView gridView = (GridView) findViewById(R.id.gvGiaoDien);
        CustomGridGiaoDien adapter = new CustomGridGiaoDien(getApplicationContext(), conNames, conImage);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences sharedPreferences= getSharedPreferences("CaiDatGiaoDien", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putInt("MaGiaoDien", Integer.parseInt(conImage.get(position).toString()));
                editor.apply();

                finish();
                Intent intent = new Intent(getApplicationContext(), DanhBaActivity.class);

                startActivity(intent);
            }
        });
    }
}
