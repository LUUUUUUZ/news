package com.example.covid19_news;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;

public class MainActivity extends BasicActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.main_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.main_navigation_view)
    NavigationView navigationView;
    private BasicFragment currentFragment;
    public FragmentAllocator fragmentAllocator;
    private boolean checkExit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentAllocator = new FragmentAllocator();

        navigationView.setNavigationItemSelectedListener(this);

        //显示主页面
        navigationView.getMenu().findItem(R.id.main_navigation_menu_Home).setChecked(true);
        switchFragment(fragmentAllocator.getHomeFragment());

//       setContentView(R.layout.activity_main);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    void switchFragment(BasicFragment fragment){
        if(currentFragment==fragment)
            return;
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        if(!fragment.isAdded()){
            ft.add(R.id.main_layout,fragment);
        }
        if(currentFragment!=null){
            if(currentFragment instanceof HomeFragment)
                ft.hide(currentFragment);
            else
                ft.remove(currentFragment);
        }
        ft.show(fragment);
        ft.commitAllowingStateLoss();
        currentFragment=fragment;
    }

    @Override
    public void onBackPressed() {
        if(currentFragment instanceof HomeFragment){
            if(!checkExit){
                Toast.makeText(this, "Press the back key again to exit", Toast.LENGTH_SHORT).show();
                checkExit=true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkExit=false;
                    }
                },2000);
                return;
            }
        }else{
            navigationView.getMenu().findItem(R.id.main_navigation_menu_Home).setChecked(true);
            switchFragment(fragmentAllocator.getHomeFragment());
            return;
        }
        super.onBackPressed();
    }

    void refreshHomeFragment(int index){
        HomeFragment f= fragmentAllocator.getHomeFragment();
        getSupportFragmentManager().beginTransaction().remove(f).commitAllowingStateLoss();
        fragmentAllocator.refreshIndexFragment(index);
        if(currentFragment==f){
            currentFragment=null;
            navigationView.getMenu().findItem(R.id.main_navigation_menu_Home).setChecked(true);
            switchFragment(fragmentAllocator.getHomeFragment());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1 && resultCode==RESULT_OK){
            refreshHomeFragment(data!=null? data.getIntExtra("selectPosition",-1):-1);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.main_navigation_menu_Home){
            switchFragment(fragmentAllocator.getHomeFragment());
        }else if(id==R.id.main_navigation_menu_History){
            switchFragment(fragmentAllocator.getHistoryFragment());
        }else if(id==R.id.main_navigation_menu_ChinaDataMap){
            switchFragment(fragmentAllocator.getChinaDataMapFragment());
        }else if(id==R.id.main_navigation_menu_GlobalDataMap){
            switchFragment(fragmentAllocator.getGlobalDataMapFragment());
        }else if(id==R.id.main_navigation_menu_GraphSchema){
            switchFragment(fragmentAllocator.getGraphSchemaFragment());
        }else if(id==R.id.main_navigation_menu_Cluster){
            switchFragment(fragmentAllocator.getClusterFragment());
        }else if(id==R.id.main_navigation_menu_Scholar){
            switchFragment(fragmentAllocator.getScholarFragment());
        }else if(id==R.id.main_navigation_menu_About){
            switchFragment(fragmentAllocator.getAboutFragment());
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    class FragmentAllocator{
        private HomeFragment homeFragment;

        HomeFragment refreshIndexFragment(int index){
            return homeFragment = new HomeFragment(index);
        }

        HomeFragment getHomeFragment(){
            if(homeFragment==null)
                return refreshIndexFragment(-1);
            return homeFragment;
        }

        HistoryFragment getHistoryFragment(){
            return new HistoryFragment();
        }

        ChinaDataMapFragment getChinaDataMapFragment(){
            return new ChinaDataMapFragment();
        }

        GlobalDataMapFragment getGlobalDataMapFragment(){
            return new GlobalDataMapFragment();
        }

        GraphSchemaFragment getGraphSchemaFragment(){
            return new GraphSchemaFragment();
        }

        ClusterFragment getClusterFragment(){
            return new ClusterFragment();
        }

        ScholarFragment getScholarFragment(){
            return new ScholarFragment();
        }

        AboutFragment getAboutFragment(){
            return new AboutFragment();
        }
    }
}