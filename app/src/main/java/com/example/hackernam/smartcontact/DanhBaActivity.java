package com.example.hackernam.smartcontact;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DanhBaActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public static final String PREFS_NAME = "MyPrefsFile";
    public static final int LOGIN_REQUEST_CODE = 10;

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
            setTheme(R.style.AppNoActionBarColor1);
        }
        if(GiaoDien == 2) {
            setTheme(R.style.AppNoActionBarColor2);
        }
        if(GiaoDien == 3) {
            setTheme(R.style.AppNoActionBarColor3);
        }
        if(GiaoDien == 4) {
            setTheme(R.style.AppNoActionBarColor4);
        }
        if(GiaoDien == 5) {
            setTheme(R.style.AppNoActionBarColor5);
        }
        if(GiaoDien == 6) {
            setTheme(R.style.AppNoActionBarColor6);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_ba);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                //Intent intent = new Intent(getApplicationContext(), ThemDanhBaActivity.class);
                //startActivity(intent);
                Intent intent = new Intent(Intent.ACTION_INSERT,
                        ContactsContract.Contacts.CONTENT_URI);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                // Do whatever you want here
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                String Username = settings.getString("username", "");
                String Email = settings.getString("email", "");
                String Imagepath = settings.getString("imagepath", "");
                String FBid = settings.getString("fbid", "");
                if (!Username.isEmpty() || !Email.isEmpty() || !Imagepath.isEmpty()){
                    TextView tvusername = (TextView) findViewById(R.id.username);
                    tvusername.setText(Username);
                    TextView tvuseremail = (TextView) findViewById(R.id.useremail);
                    tvuseremail.setText(Email);
                    ImageView imageViewIcon = (ImageView)findViewById(R.id.imageViewIcon);
                    Picasso.with(DanhBaActivity.this)
                            .load(Imagepath)
                            .resize(100,80)
                            .into(imageViewIcon);
                }
            }
        };
        drawer.setDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TabDanhBaFragment(), "Danh bạ");
        adapter.addFragment(new TabDanhBaYeuThichFragment(), "Yêu thích");
        adapter.addFragment(new TabDanhBaGanDayFragment(), "Gần đây");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.danh_ba, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.KiemTraTrung) {
            Intent intent = new Intent(getApplicationContext(), KiemTraTrungActivity.class);
            startActivity(intent);
        } else if (id == R.id.SaoLuuPhucHoi) {

        } else if (id == R.id.ThongKe) {
            Intent intent = new Intent(getApplicationContext(), ThongKeActivity.class);
            startActivity(intent);
        } else if (id == R.id.GiaoDien) {
            Intent intent = new Intent(getApplicationContext(), GiaoDienActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.CaiDat) {
            Intent intent = new Intent(getApplicationContext(), CaiDatActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "SmartContact");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Tải ngay tại https://smartcontact.com/ ");
            startActivity(Intent.createChooser(sharingIntent, "Chia sẻ"));

        } else if (id == R.id.nav_send) {


        } else if (id == R.id.nav_login){
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivityForResult(loginIntent, LOGIN_REQUEST_CODE);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if ((requestCode == LOGIN_REQUEST_CODE) && (resultCode == Activity.RESULT_OK)){

                Intent intent = new Intent(this, DanhBaActivity.class);
                startActivity(intent);
                finish();
            }
        }catch (Exception e){
            Toast.makeText(DanhBaActivity.this, "Lỗi !!!", Toast.LENGTH_SHORT).show();
        }
    }
}
