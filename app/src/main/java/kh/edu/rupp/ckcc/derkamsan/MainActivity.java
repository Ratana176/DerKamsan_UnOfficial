package kh.edu.rupp.ckcc.derkamsan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import kh.edu.rupp.ckcc.derkamsan.Events.EventFragment;
import kh.edu.rupp.ckcc.derkamsan.Homes.HomeFragment;
import kh.edu.rupp.ckcc.derkamsan.Offline.OfflineActivity;
import kh.edu.rupp.ckcc.derkamsan.PopularPlaces.PopularPlaceFragment;
import kh.edu.rupp.ckcc.derkamsan.Profiles.LoginActivity;
import kh.edu.rupp.ckcc.derkamsan.Profiles.Profile;
import kh.edu.rupp.ckcc.derkamsan.Profiles.ProfileActivity;
import kh.edu.rupp.ckcc.derkamsan.Profiles.SignUpActivity;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,NavigationView.OnNavigationItemSelectedListener{

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private EmptyDataFragment emptyDataFragment;
    private MenuItem menuItem;
    private Profile profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();



        HomeFragment fragment = new HomeFragment();
        emptyDataFragment = new EmptyDataFragment();

        if (fragment.isHomeContainData()){
            LibraryHelper.displayFragment(emptyDataFragment,getSupportFragmentManager(),R.id.lyt_container);
        }else
            LibraryHelper.displayFragment(fragment,getSupportFragmentManager(),R.id.lyt_container);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();


        SharedPreferences preferences = getSharedPreferences("pro", MODE_PRIVATE);
        String profileJsonString = preferences.getString("profile", null);
        Gson gson = new Gson();
//        profile = gson.fromJson(profileJsonString, Profile.class);
        profile = Singleton.getInstance().getProfile();
        Singleton.getInstance().setProfile(profile);

        menuItem = menu.findItem(R.id.nav_menu_logout);
        if (profile == null)
            menuItem.setTitle("Login");
        else menuItem.setTitle("logout");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        switch (id){
            case R.id.bottom_menu_home:
                HomeFragment homeFragment = new HomeFragment();
                if (homeFragment.isHomeContainData()){
                    LibraryHelper.displayFragment(emptyDataFragment,getSupportFragmentManager(),R.id.lyt_container);
                }else
                    LibraryHelper.displayFragment(homeFragment,getSupportFragmentManager(),R.id.lyt_container);
                break;
            case R.id.bottom_menu_event:
                EventFragment eventFragment = new EventFragment();
                if (eventFragment.isHomeContainData()){
                    LibraryHelper.displayFragment(emptyDataFragment,getSupportFragmentManager(),R.id.lyt_container);
                }else
                    LibraryHelper.displayFragment(eventFragment,getSupportFragmentManager(),R.id.lyt_container);
                break;
            case R.id.bottom_menu_popplace:
                PopularPlaceFragment popularPlaceFragment = new PopularPlaceFragment();
                LibraryHelper.displayFragment(popularPlaceFragment,getSupportFragmentManager(),R.id.lyt_container);
                break;
            case R.id.bottom_menu_map:
                LibraryHelper.showToast("map",this);
                break;
            case R.id.bottom_menu_profile:

                if (profile == null){
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.nav_menu_offline:
                startActivity(new Intent(this, OfflineActivity.class));
                break;
            case R.id.nav_menu_about:
                startActivity(new Intent(this,AboutUsActivity.class));
                break;
            case R.id.nav_menu_contact_us:
                startActivity(new Intent(this,ContactUsActivity.class));
                break;
            case R.id.nav_menu_settings:
                LibraryHelper.showToast("Setting",this);
                break;
            case R.id.nav_menu_logout:
                if (profile != null ){
                    Singleton.getInstance().setProfile(null);
                    SharedPreferences preferences = this.getSharedPreferences("pro", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.remove("profile");
                    editor.apply();
                    Singleton.getInstance().setProfile(null);
                    menuItem.setTitle("Login");
                }else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
