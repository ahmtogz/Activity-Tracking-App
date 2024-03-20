package com.nexis.running;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import com.nexis.running.activitys.LoginActivity;
import com.nexis.running.model.ContactDbHelper;
import com.nexis.running.model.User;


import org.junit.Rule;
import org.junit.Test;

public class LoginActivityTest {
    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule(LoginActivity.class);


    @Test
    public void testLoginWithValidCredentials() {
        ContactDbHelper dbHelper = new ContactDbHelper(mActivityRule.getActivity());
        User user = new User("Ahmet","ahmet@hotmail.com","123","123",70,25);
        dbHelper.insertData(user);

        String validEmail = "ahmet@hotmail.com";
        String validPassword = "123";


        Espresso.onView(ViewMatchers.withId(R.id.login_email)).perform(ViewActions.typeText(validEmail));
        Espresso.onView(ViewMatchers.withId(R.id.login_password)).perform(ViewActions.typeText(validPassword));


        Espresso.onView(ViewMatchers.withId(R.id.login_button)).perform(ViewActions.click());



        onView(withText("Login Successfully!"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
    }

    @Test
    public void testLoginWithInvalidCredentials() {
        ContactDbHelper dbHelper = new ContactDbHelper(mActivityRule.getActivity());
        User user = new User("Ahmet","ahmet@hotmail.com","123","123",70,25);
        dbHelper.insertData(user);

        String invalidEmail = "invalid@example.com";
        String invalidPassword = "invalidPassword";


        Espresso.onView(ViewMatchers.withId(R.id.login_email)).perform(ViewActions.typeText(invalidEmail));
        Espresso.onView(ViewMatchers.withId(R.id.login_password)).perform(ViewActions.typeText(invalidPassword));


        Espresso.onView(ViewMatchers.withId(R.id.login_button)).perform(ViewActions.click());



        onView(withText("Invalid Credentials!"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));

    }

    @Test
    public void testLoginWithEmptyFields() {

        String emptyEmail = "";
        String emptyPassword = "";


        Espresso.onView(ViewMatchers.withId(R.id.login_email)).perform(ViewActions.typeText(emptyEmail));
        Espresso.onView(ViewMatchers.withId(R.id.login_password)).perform(ViewActions.typeText(emptyPassword));


        Espresso.onView(ViewMatchers.withId(R.id.login_button)).perform(ViewActions.click());

        onView(withText("All fields are mandatory!"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
    }
}