package com.nexis.running;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

import static java.util.regex.Pattern.matches;

import androidx.preference.PreferenceManager;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.nexis.running.activitys.LoginActivity;
import com.nexis.running.activitys.MainActivity;
import android.content.SharedPreferences;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import com.nexis.running.R;
import com.nexis.running.activitys.MainActivity;

import org.junit.Rule;
import org.junit.Test;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.core.app.ApplicationProvider;

import com.google.gson.Gson;
import com.nexis.running.activitys.PersonFragment;
import com.nexis.running.model.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PersonFragmentTest {

    @Mock
    private SharedPreferences sharedPreferences;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void verifyLoggedInUsernameIsDisplayed() {
        // Create a mock User
        User user = new User("Paul", "paul@example.com", "pass123", "Male", 70, 25);

        // Convert User to JSON string
        String userJson = new Gson().toJson(user);

        // Create a bundle with the user JSON string
        Bundle bundle = new Bundle();
        bundle.putString("user", userJson);

        // Launch the fragment with the provided bundle
        FragmentScenario<PersonFragment> scenario = FragmentScenario.launchInContainer(PersonFragment.class, bundle);

        // Use Espresso to check if the TextView displays the correct username
        onView(withId(R.id.textViewWelcome)).check(matches(withText("Hi Paul!")));
    }


    @Test
    public void verifyLogoutClearsSharedPreferencesAndNavigatesToLogin() {
        // Create a mock User
        User user = new User("Paul", "paul@example.com", "pass123", "Male", 70, 25);

        // Convert User to JSON string
        String userJson = new Gson().toJson(user);

        // Create a bundle with the user JSON string
        Bundle bundle = new Bundle();
        bundle.putString("user", userJson);

        // Launch the fragment with the provided bundle
        FragmentScenario<PersonFragment> scenario = FragmentScenario.launchInContainer(PersonFragment.class, bundle);

        // Mock the SharedPreferences.Editor
        SharedPreferences.Editor editor = mock(SharedPreferences.Editor.class);
        when(sharedPreferences.edit()).thenReturn(editor);

        // Set up the context to return the mocked SharedPreferences
        Context context = ApplicationProvider.getApplicationContext();
        when(context.getSharedPreferences(any(String.class), any(Integer.class))).thenReturn(sharedPreferences);

        // Trigger the logout button click
        onView(withId(R.id.logout_button)).perform(click());

        // Verify that SharedPreferences.Editor.clear() is called
        verify(editor).clear();

        // Verify that startActivity is called with the LoginActivity class
        intended(hasComponent(LoginActivity.class.getName()));
    }

    // You can similarly write a test for the personalParametersButton click that navigates to the desired screen.
}

