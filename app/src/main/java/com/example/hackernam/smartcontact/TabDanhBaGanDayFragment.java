package com.example.hackernam.smartcontact;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hackernam.smartcontact.lib.XuLyBitmap;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabDanhBaGanDayFragment extends ListFragment {


    public TabDanhBaGanDayFragment() {
        // Required empty public constructor
    }

    ArrayList<String> conPhoneID;
    ArrayList<String> conNames;
    ArrayList<String> conImage;
    ArrayList<String> conLookUpKey;
    ArrayList<Integer> conTrangThaiCuocGoi;
    ArrayList<Long> conNgayThang;
    ArrayList<String> conNumber;
    ContentResolver cr;
    Cursor cur;



    protected static class ViewHolder {
        public LinearLayout lnTenDanhBa;
        public LinearLayout lnThongTin;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        conNames = new ArrayList<String>();
        conImage = new ArrayList<String>();
        conPhoneID = new ArrayList<String>();
        conLookUpKey = new ArrayList<String>();
        conTrangThaiCuocGoi = new ArrayList<Integer>();
        conNgayThang = new ArrayList<Long>();
        conNumber = new ArrayList<String>();



        String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
        Uri callUri = Uri.parse("content://call_log/calls");
        cur = getActivity().getContentResolver().query(callUri, null, null, null, strOrder);
        while (cur.moveToNext()) {
            conNames.add(cur.getString(cur.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME)));
            conTrangThaiCuocGoi.add(cur.getInt(cur.getColumnIndex(android.provider.CallLog.Calls.TYPE)));
            conNgayThang.add(cur.getLong(cur.getColumnIndex(CallLog.Calls.DATE)));
            conNumber.add(cur.getString(cur.getColumnIndex(CallLog.Calls.NUMBER)));
            conPhoneID.add(cur.getString(cur.getColumnIndex(CallLog.Calls._ID)));
        }
        setListAdapter(new MyAdapter(getContext(), android.R.layout.simple_list_item_1, R.id.txtTenDanhBa, conNames));
        return inflater.inflate(R.layout.fragment_tab_danh_ba_gan_day, container, false);
    }

    private class MyAdapter extends ArrayAdapter<String> {

        public MyAdapter(Context context, int resource, int textViewResourceId,
                         ArrayList<String> conNames) {
            super(context, resource, textViewResourceId, conNames);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View row = setList(position, parent);
            ViewHolder holder;

            holder = new ViewHolder();
            holder.lnThongTin = (LinearLayout) row.findViewById(R.id.linear2);
            holder.lnThongTin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ChiTietLichSuDanhBaActivity.class);
                    intent.putExtra("ContactID",conPhoneID.get(position).toString());
                    startActivity(intent);
                }
            });
            holder.lnTenDanhBa = (LinearLayout) row.findViewById(R.id.linear1);
            holder.lnTenDanhBa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + conNumber.get(position).toString()));
                    startActivity(intent);
                }
            });
            return row;
        }

        private View setList(int position, ViewGroup parent) {
            LayoutInflater inf = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row = inf.inflate(R.layout.lichsu_list_item, parent, false);

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
