package com.example.hackernam.smartcontact;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hackernam.smartcontact.lib.ContactHelper;

public class ThemDanhBaActivity extends AppCompatActivity implements View.OnClickListener {

    EditText txtTenDanhBa;
    EditText txtSoDienThoai;
    Button btnThem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_danh_ba);

        txtTenDanhBa = (EditText) findViewById(R.id.txtTenDanhBa);
        txtSoDienThoai = (EditText) findViewById(R.id.txtSoDienThoai);
        btnThem = (Button) findViewById(R.id.btnThem);

        btnThem.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, DanhBaActivity.class);
        if(v.getId() == btnThem.getId())
        {
                if (txtTenDanhBa.getText().toString().equals("")
                        && txtSoDienThoai.getText().toString().equals("")) {
                    Toast.makeText(this, "Bạn chưa điền đầy đủ thông tin !",
                            Toast.LENGTH_SHORT).show();
                } else {
                    ContactHelper.insertContact(getContentResolver(),
                            txtTenDanhBa.getText().toString(), txtSoDienThoai.getText().toString());
                    txtTenDanhBa.setText("");
                    txtSoDienThoai.setText("");
                    startActivity(intent);
                }
        }
    }
}
