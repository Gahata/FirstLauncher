package org.example.paweljezierski.firstlauncher;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PackageManager manager;
    private List<AppDetail> apps;
    private GridView grid;

    int statusBarHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusBarHeight = getStatusBarHeight();

       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        loadApps();
        loadListView();
        addClickListener();
        setStatusBarTranslucent(true);

        //setColumnWidth();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        /*ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();*/

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            result = getResources().getDimensionPixelSize(resourceId);
        return result;

    }

    protected void setStatusBarTranslucent(boolean makeTranslucent) {

        grid.setPadding(0, makeTranslucent ? statusBarHeight+16 : 16, 0, 0);

        if (makeTranslucent)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        else
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    private void loadApps() {
        manager = getPackageManager();
        apps = new ArrayList<>();

        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> availableActivities = manager.queryIntentActivities(i, 0);
        for (ResolveInfo ri : availableActivities) {
            AppDetail app = new AppDetail();
            app.label = ri.loadLabel(manager);
            app.name = ri.activityInfo.packageName;
            app.icon = ri.activityInfo.loadIcon(manager);
            apps.add(app);
        }
    }

    private void loadListView() {
        grid = findViewById(R.id.appGrid);
        System.out.println(grid.getPaddingLeft() + " " + grid.getPaddingRight() + " " + grid.getHorizontalSpacing());

        ArrayAdapter<AppDetail> adapter = new ArrayAdapter<AppDetail>(this, R.layout.list_item, apps) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null)
                    convertView = getLayoutInflater().inflate(R.layout.list_item,  null);

                ImageView appIcon = convertView.findViewById(R.id.item_app_icon);
                appIcon.setImageDrawable(apps.get(position).icon);

                TextView appLabel = convertView.findViewById(R.id.item_app_label);
                appLabel.setText(apps.get(position).label);

                return convertView;
            }
        };

        grid.setAdapter(adapter);
    }

    private void addClickListener() {
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos,
                                    long id) {
                Intent i = manager.getLaunchIntentForPackage(apps.get(pos).name.toString());
                MainActivity.this.startActivity(i);
            }
        });
    }

    private void setColumnWidth() {
        Point screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        int screenWidth = screenSize.x;
        int paddingsWidthSum = grid.getPaddingLeft() + grid.getPaddingRight() + grid.getHorizontalSpacing() * 4;
        System.out.println(grid.getPaddingLeft() + " " + grid.getPaddingRight() + " " + grid.getHorizontalSpacing());
        int columnWidth = (screenWidth - paddingsWidthSum) / 5;
        System.out.println(screenWidth + " " + paddingsWidthSum + " " + columnWidth);
        grid.setColumnWidth(columnWidth);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        /*else
            super.onBackPressed();*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
            return true;
        return super.onOptionsItemSelected(item);
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}