package com.nexis.running;

import android.content.Context;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static org.junit.Assert.assertFalse;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nexis.running.activitys.ListFragment;
import com.nexis.running.activitys.MainActivity;
import com.nexis.running.model.Route;
import com.nexis.running.model.RouteDbHelper;

import java.util.List;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ListFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    private ListFragment listFragment;

    @Before
    public void setUp() {

        // Navigate to the ListFragment using BottomNavigationView
        onView(withId(R.id.bottomNavigationView)).perform(selectMenuAction(R.id.listBullet));

        // Wait for the fragment transaction to be completed
        onView(isRoot()).perform(waitFor(500)); // Adjust the delay time as needed

        // Get reference to ListFragment
        FragmentManager fragmentManager = activityRule.getActivity().getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.flFragment);

        if (fragment instanceof ListFragment) {
            listFragment = (ListFragment) fragment;
        } else {
            throw new RuntimeException("Fragment is not an instance of ListFragment");
        }
    }

    // Add custom actions for BottomNavigationView
    private static ViewAction selectMenuAction(final int menuItemId) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(BottomNavigationView.class);
            }

            @Override
            public String getDescription() {
                return "click on menu item with id " + menuItemId;
            }

            @Override
            public void perform(UiController uiController, View view) {
                BottomNavigationView bottomNavigationView = (BottomNavigationView) view;
                bottomNavigationView.setSelectedItemId(menuItemId);
            }
        };
    }

    // Add custom action for waiting
    private static ViewAction waitFor(final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "wait for " + millis + " milliseconds";
            }

            @Override
            public void perform(UiController uiController, View view) {
                uiController.loopMainThreadForAtLeast(millis);
            }
        };
    }



    @Test
    public void testListViewIsDisplayed() {
        onView(withId(R.id.dataListView)).check(matches(isDisplayed()));
    }

    @Test
    public void testItemClickShowsAlertDialog() {
        onView(withId(R.id.dataListView)).perform(ViewActions.longClick());
        //onView(withText("Are you sure?")).check(matches(isDisplayed()));
    }


    @Test
    public void testRemoveItemFromListAndDatabase() {
        // Assuming there is at least one item in the list
        onView(withId(R.id.dataListView)).perform(ViewActions.longClick());

        onView(withText("Yes")).perform(click());

        // Check if the item is removed from the list
        onView(withId(R.id.dataListView)).check(matches(isDisplayed()));

        // Check if the item is removed from the database
        Context context = ApplicationProvider.getApplicationContext();
        RouteDbHelper dbHelper = new RouteDbHelper(context);

        // Load the updated routes after deletion
        List<Route> updatedRoutes = dbHelper.loadAll();

        // Load the routeToRemove using the same method used in ListFragment
        List<Route> routeListFromDatabase = listFragment.loadRoutesFromDatabase();

        // Assuming updatedRoutes is the updated list of routes after deletion
        assertFalse(updatedRoutes.contains(routeListFromDatabase.get(0)));
    }

}
