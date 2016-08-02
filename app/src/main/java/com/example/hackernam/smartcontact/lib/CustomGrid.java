package com.example.hackernam.smartcontact.lib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hackernam.smartcontact.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by HackerNam on 6/15/2016.
 */
public class CustomGrid extends BaseAdapter {
    private Context mContext;
    private final ArrayList<String> names;
    private final ArrayList<String> image;

    public CustomGrid(Context c,ArrayList<String> web,ArrayList<String> Imageid ) {
        mContext = c;
        this.image = Imageid;
        this.names = web;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_single, null);
            TextView textView = (TextView) grid.findViewById(R.id.txtTenDanhBa);
            ImageView imageView = (ImageView)grid.findViewById(R.id.imgDanhBa);
            TextView tvKyTuDau = (TextView) grid.findViewById(R.id.txtKyTuDau);
            textView.setText(names.get(position));
            if(image.get(position) != "no") {
                Uri imgUri = Uri.parse(image.get(position));
                Bitmap bitmap;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(grid.getContext().getContentResolver(), imgUri);
                    imageView.setImageBitmap(XuLyBitmap.getRoundedBitmap(bitmap));
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
            else
            {
                String KyTuDau = names.get(position).substring(0,1).toUpperCase();
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
                Bitmap myBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565);
                Paint paint = new Paint();
                Canvas canvas = new Canvas(myBitmap);
                ColorFilter filter = new PorterDuffColorFilter(grid.getContext().getResources().getColor(MaMau), PorterDuff.Mode.SRC_IN);
                paint.setColorFilter(filter);
                canvas.drawBitmap(myBitmap, 0, 0, paint);
                tvKyTuDau.setText(KyTuDau.toString());
                imageView.setImageBitmap(XuLyBitmap.getRoundedBitmap(myBitmap));
            }
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}
