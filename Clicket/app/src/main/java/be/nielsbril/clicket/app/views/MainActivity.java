package be.nielsbril.clicket.app.views;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import be.nielsbril.clicket.app.App;
import be.nielsbril.clicket.app.R;
import be.nielsbril.clicket.app.api.ClicketInstance;
import be.nielsbril.clicket.app.helpers.ApiHelper;
import be.nielsbril.clicket.app.helpers.AuthHelper;
import be.nielsbril.clicket.app.helpers.Interfaces;
import be.nielsbril.clicket.app.api.UserResult;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        Interfaces.changeToolbar {

    private NestedScrollView scrollView;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private MenuItem navPark;
    private MenuItem navAccount;
    private MenuItem navCars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        setSupportActionBar(toolbar);

        scrollView = (NestedScrollView) findViewById(R.id.scrollview_main);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        navPark = (MenuItem) menu.findItem(R.id.nav_park);
        navAccount = (MenuItem) menu.findItem(R.id.nav_account);
        navCars = (MenuItem) menu.findItem(R.id.nav_cars);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!AuthHelper.isLoggedIn(this)) {
            AuthHelper.logUserOff(this);
            showLoginActivity();
        } else {
            navigate(ParkFragment.newInstance(), "parkFragment");
            ApiHelper.subscribe(ClicketInstance.getClicketserviceInstance().user(AuthHelper.getAuthToken(this)), new Action1<UserResult>() {
                @Override
                public void call(UserResult userResult) {
                    if (userResult.isSuccess()) {
                        ((App) MainActivity.this.getApplication()).setUser(userResult.getData());

                        View header = navigationView.getHeaderView(0);

                        TextView txtName = (TextView) header.findViewById(R.id.txtName);
                        txtName.setText(userResult.getData().getFirstname() + " " + userResult.getData().getName());

                        TextView txtEmail = (TextView) header.findViewById(R.id.txtEmail);
                        txtEmail.setText(userResult.getData().getEmail());
                    } else {
                        AuthHelper.logUserOff(MainActivity.this);
                        showLoginActivity();
                    }
                }
            });
        }
    }

    private void showLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager.getBackStackEntryCount() == 1) {
                fragmentManager.popBackStack();
            } else {
                int i = fragmentManager.getBackStackEntryCount() - 1;
                FragmentManager.BackStackEntry backStackEntry = fragmentManager.getBackStackEntryAt(i);
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }

            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            AuthHelper.logUserOff(this);
            showLoginActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_park) {
            toggleNavItems(navPark, navAccount, navCars);
            navigate(ParkFragment.newInstance(), "parkFragment");
        } else if (id == R.id.nav_account) {
            toggleNavItems(navAccount, navPark, navCars);
            navigate(AccountFragment.newInstance(), "accountFragment");
        } else if (id == R.id.nav_cars) {
            toggleNavItems(navCars, navPark, navAccount);
            navigate(CarFragment.newInstance(), "carFragment");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void toggleNavItems(MenuItem selected, MenuItem nonSelectedA, MenuItem nonSelectedB) {
        selected.setChecked(true);
        nonSelectedA.setChecked(false);
        nonSelectedB.setChecked(false);
    }

    private void navigate(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayout_main, fragment, tag);
        fragmentTransaction.addToBackStack("navigation_to_" + tag);
        fragmentTransaction.commit();
        scrollView.fullScroll(ScrollView.FOCUS_UP);
    }

    @Override
    public void setTitle(String title) {
        toolbar.setTitle(title);
    }

}