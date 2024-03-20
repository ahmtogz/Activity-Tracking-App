package com.nexis.running;



import com.nexis.running.activitys.LoginActivity;
import com.nexis.running.activitys.RegisterActivity;
import com.nexis.running.activitys.WelcomeActivity;


import org.junit.Rule;
import org.junit.Test;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.rule.IntentsTestRule;


public class WelcomeActivityTest {
    @Rule
    public IntentsTestRule<WelcomeActivity> intentsTestRule = new IntentsTestRule<>(WelcomeActivity.class);

    @Test
    public void createAccountButtonClicked_opensRegisterActivity() {
        onView(withId(R.id.welcome_CreateAccount_button)).perform(click());
        intended(hasComponent(RegisterActivity.class.getName()));
    }

    @Test
    public void loginButtonClicked_opensLoginActivity() {
        onView(withId(R.id.welcome_login_button)).perform(click());
        intended(hasComponent(LoginActivity.class.getName()));
    }

    @Test
    public void activityIsCreatedAndUIIsDisplayed() {
        ActivityScenario<WelcomeActivity> scenario = ActivityScenario.launch(WelcomeActivity.class);

        // Check if the welcome screen is displayed
        onView(withId(R.id.welcome_CreateAccount_button)).check(matches(isDisplayed()));
        onView(withId(R.id.welcome_login_button)).check(matches(isDisplayed()));

        scenario.close(); // Close the activity scenario
    }
}