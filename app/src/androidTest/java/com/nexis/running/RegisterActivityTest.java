package com.nexis.running;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import org.junit.Rule;
import org.junit.Test;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import com.nexis.running.activitys.RegisterActivity;
import com.nexis.running.model.ContactDbHelper;
import com.nexis.running.model.User;


public class RegisterActivityTest {

    @Rule
    public ActivityTestRule<RegisterActivity> mActivityRule = new ActivityTestRule(RegisterActivity.class);

    @Test
    public void testValidRegistration() {

        onView(withId(R.id.editTextName)).perform(typeText("John Doe"), closeSoftKeyboard());
        onView(withId(R.id.editTextEmail)).perform(typeText("john.doe@example.com"), closeSoftKeyboard());
        onView(withId(R.id.editTextPassword)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.editTextConfirmPassword)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.radioMale)).perform(click());
        onView(withId(R.id.editTextWeight)).perform(typeText("70"), closeSoftKeyboard());
        onView(withId(R.id.editTextAge)).perform(typeText("25"), closeSoftKeyboard());

        onView(withId(R.id.btnRegister)).perform(click());

        onView(withText("Signup Successfully!"))
                .inRoot(isDialog()) // Use the isDialog() matcher to check if the view is in a dialog
                .check(matches(isDisplayed()));
    }

    @Test
    public void testInvalidPassword() {

        onView(ViewMatchers.withId(R.id.editTextName)).perform(ViewActions.typeText("John Doe"), ViewActions.closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.editTextEmail)).perform(ViewActions.typeText("john.doe@example.com"), ViewActions.closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.editTextPassword)).perform(ViewActions.typeText("password123"), ViewActions.closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.editTextConfirmPassword)).perform(ViewActions.typeText("wrongpassword"), ViewActions.closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.radioMale)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.editTextWeight)).perform(ViewActions.typeText("70"), ViewActions.closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.editTextAge)).perform(ViewActions.typeText("25"), ViewActions.closeSoftKeyboard());

        onView(ViewMatchers.withId(R.id.btnRegister)).perform(ViewActions.click());

        onView(withText("Invalid Password!"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));

    }
    @Test
    public void testDatabaseIntegration() {

        onView(withId(R.id.editTextName)).perform(typeText("John Doe"), closeSoftKeyboard());
        onView(withId(R.id.editTextEmail)).perform(typeText("john.doe@example.com"), closeSoftKeyboard());
        onView(withId(R.id.editTextPassword)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.editTextConfirmPassword)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.radioMale)).perform(click());
        onView(withId(R.id.editTextWeight)).perform(typeText("70"), closeSoftKeyboard());
        onView(withId(R.id.editTextAge)).perform(typeText("25"), closeSoftKeyboard());

        // Simuliere die Registrierung
        onView(withId(R.id.btnRegister)).perform(click());

        // Überprüfe, ob die Daten korrekt in die Datenbank eingefügt wurden
        ContactDbHelper dbHelper = new ContactDbHelper(mActivityRule.getActivity());
        User user = dbHelper.getUserByEmail("john.doe@example.com");

        assertNotNull(user);
        assertEquals("John Doe", user.getName());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("Male", user.getGender());
        assertEquals(70, user.getWeight());
        assertEquals(25, user.getAge());
    }
    @Test
    public void testExistingUserAlready() {
        ContactDbHelper dbHelper = new ContactDbHelper(mActivityRule.getActivity());
        User user = new User("John Doe","existing-email@example.com","password123","password123",70,25);
        dbHelper.insertData(user);

        onView(withId(R.id.editTextName)).perform(typeText("John Doe"), closeSoftKeyboard());
        onView(withId(R.id.editTextEmail)).perform(typeText("existing-email@example.com"), closeSoftKeyboard());
        onView(withId(R.id.editTextPassword)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.editTextConfirmPassword)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.radioMale)).perform(click());
        onView(withId(R.id.editTextWeight)).perform(typeText("70"), closeSoftKeyboard());
        onView(withId(R.id.editTextAge)).perform(typeText("25"), closeSoftKeyboard());

        onView(withId(R.id.btnRegister)).perform(click());

        onView(withText("User already exists! Please login"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
    }

    @Test
    public void testAllFieldsAreMandatory() {

        onView(withId(R.id.editTextEmail)).perform(typeText("john@example.com"), closeSoftKeyboard());
        onView(withId(R.id.editTextPassword)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.editTextConfirmPassword)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.editTextWeight)).perform(typeText("70"), closeSoftKeyboard());
        onView(withId(R.id.editTextAge)).perform(typeText("25"), closeSoftKeyboard());

        onView(withId(R.id.btnRegister)).perform(click());

        onView(withText("All fields are mandatory"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
    }







}
