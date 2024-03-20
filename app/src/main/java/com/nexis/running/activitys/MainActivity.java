package com.nexis.running.activitys;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nexis.running.R;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    BottomNavigationView bottomNavigationView;
    ListFragment listFragment;
    HomeFragment homeFragment;
    PersonFragment personFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = new Bundle();
        String userAsString = getIntent().getStringExtra("user" );
        bundle.putString("user", userAsString);


        CreateHomeFragment(bundle);
        CreatePersonFragment(bundle);
        CreateListFragment(bundle);




        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.listBullet) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, listFragment).commit();
            return true;
        } else if (itemId == R.id.home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, homeFragment).commit();
            return true;
        } else if (itemId == R.id.person) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, personFragment).commit();
            return true;
        }
        return false;
    }



        /*
        int itemId = item.getItemId();

        if (itemId == R.id.listBullet) {
            if (listFragment == null) {
                CreateListFragment(new Bundle());  // Initialize listFragment if null
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, listFragment).commit();
            return true;
        } else if (itemId == R.id.home) {
            if (homeFragment == null) {
                CreateHomeFragment(new Bundle());  // Initialize homeFragment if null
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, homeFragment).commit();
            return true;
        } else if (itemId == R.id.person) {
            if (personFragment == null) {
                CreatePersonFragment(new Bundle());  // Initialize personFragment if null
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, personFragment).commit();
            return true;
        }
        return false;

         */

    public void CreateHomeFragment(Bundle bundle){
        homeFragment = new HomeFragment();
        homeFragment.setArguments(bundle);
    }
    public void CreatePersonFragment(Bundle bundle){
        personFragment = new PersonFragment();
        personFragment.setArguments(bundle);
    }
    public void CreateListFragment(Bundle bundle){
        listFragment = new ListFragment();
        listFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flFragment, listFragment, ListFragment.class.getSimpleName())
                .commit();
        getSupportFragmentManager().executePendingTransactions();
    }
}

