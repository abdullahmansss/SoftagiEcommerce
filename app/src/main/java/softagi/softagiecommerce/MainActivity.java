package softagi.softagiecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import softagi.softagiecommerce.Fragments.CategoriesFragment;
import softagi.softagiecommerce.Fragments.HomeFragment;
import softagi.softagiecommerce.Fragments.ProfileFragment;
import softagi.softagiecommerce.Fragments.WishlistFragment;

public class MainActivity extends AppCompatActivity
{
    BottomNavigationView navigation;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = findViewById(R.id.navigation);
        fragmentManager = getSupportFragmentManager();

        HomeFragment homeFragment = new HomeFragment();
        loadFragment(homeFragment);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.home:
                        HomeFragment homeFragment = new HomeFragment();
                        loadFragment(homeFragment);
                        return true;
                    case R.id.categories:
                        CategoriesFragment categoriesFragment = new CategoriesFragment();
                        loadFragment(categoriesFragment);
                        return true;
                    case R.id.favourite:
                        WishlistFragment wishlistFragment = new WishlistFragment();
                        loadFragment(wishlistFragment);
                        return true;
                    case R.id.profile:
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        if (user == null)
                        {
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        } else
                            {
                                ProfileFragment profileFragment = new ProfileFragment();
                                loadFragment(profileFragment);
                            }
                        return true;
                    default:
                        return true;
                }
            }
        });
    }

    public void loadFragment(Fragment fragment)
    {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}