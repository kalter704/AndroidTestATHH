package ru.vas.lan.test.androidtestathh;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "main_activity";

    private CardsFragment mCardsFragment;
    private ContactFragment mContactFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mCardsFragment = CardsFragment.newInstance();
        mContactFragment = ContactFragment.newInstance();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, mCardsFragment)
                .commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.cards:
                    ft.replace(R.id.content, mCardsFragment);
                    break;
                case R.id.contact:
                    ft.replace(R.id.content, mContactFragment);
                    break;
                default:
                    return false;
            }
            ft.commit();
            return true;
        }

    };

}
