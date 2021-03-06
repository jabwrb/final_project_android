package com.jabwrb.nutridiary.activity;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.jabwrb.nutridiary.R;
import com.jabwrb.nutridiary.adapter.FoodEntryRecyclerViewAdapter;
import com.jabwrb.nutridiary.adapter.FoodRecyclerViewAdapter;
import com.jabwrb.nutridiary.database.DatabaseSingleton;
import com.jabwrb.nutridiary.database.Food;
import com.jabwrb.nutridiary.database.FoodEntry;
import com.jabwrb.nutridiary.fragment.AddToDiaryFragment;
import com.jabwrb.nutridiary.fragment.MyFoodFragment;
import com.jabwrb.nutridiary.fragment.SearchFoodFragment;
import com.jabwrb.nutridiary.fragment.CreateFoodFragment;
import com.jabwrb.nutridiary.fragment.DatePickerFragment;
import com.jabwrb.nutridiary.fragment.HomeFragment;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements HomeFragment.HomeFragmentListener,
                                                            MyFoodFragment.MyFoodFragmentListener,
                                                            CreateFoodFragment.CreateFoodFragmentListener,
                                                            AddToDiaryFragment.AddToDiaryFragmentListener,
                                                            FoodEntryRecyclerViewAdapter.OnFoodEntryClickListener,
                                                            FoodRecyclerViewAdapter.OnFoodClickListener {

    private static final String KEY_CURRENT_DATE = "currentDate";
    private Date currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            DatabaseSingleton.getDatabaseInstance().initDb(getApplicationContext());

            currentDate = new Date();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragmentContainer, new HomeFragment(), HomeFragment.TAG)
                    .commit();
        } else {
            currentDate = new Date(savedInstanceState.getLong(KEY_CURRENT_DATE));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(KEY_CURRENT_DATE, currentDate.getTime());
    }

    /**
     * HomeFragment
     */
    @Override
    public void onFabAddEntryPressed() {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragmentContainer, new MyFoodFragment(), MyFoodFragment.TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBtnDatePickerPressed() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setTargetFragment(getSupportFragmentManager().findFragmentByTag(HomeFragment.TAG), 0);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onMenuSearchPressed() {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragmentContainer, new SearchFoodFragment(), SearchFoodFragment.TAG)
                .addToBackStack(null)
                .commit();
    }

    /**
     * AddToDiaryFragment
     */
    @Override
    public void onActionAddPressed() {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().popBackStack();
    }

    /**
     * MyFoodFragment
     */
    @Override
    public void onActionCreatePressed() {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragmentContainer, new CreateFoodFragment().newInstance(new Food(), CreateFoodFragment.ACTION_INSERT))
                .addToBackStack(null)
                .commit();
    }

    /**
     * FoodRecyclerViewAdapter
     */
    @Override
    public void onFoodClicked(Food food) {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragmentContainer, new AddToDiaryFragment().newInstance(food))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onFoodInfoClicked(Food food) {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragmentContainer, new CreateFoodFragment().newInstance(food, CreateFoodFragment.ACTION_UPDATE))
                .addToBackStack(null)
                .commit();
    }

    /**
     * CreateFoodFragment
     */
    @Override
    public void onMenuConfirmPressed() {
        MyFoodFragment fragment = (MyFoodFragment) getSupportFragmentManager().findFragmentByTag(MyFoodFragment.TAG);
        fragment.queryFoods();
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onUpdated() {
        MyFoodFragment fragment = (MyFoodFragment) getSupportFragmentManager().findFragmentByTag(MyFoodFragment.TAG);
        fragment.queryFoods();
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onFoodEntryLongClicked(FoodEntry foodEntry) {
        HomeFragment fragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(HomeFragment.TAG);
        fragment.showDialogDelete(foodEntry);
    }

    // getter & setter
    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }
}
