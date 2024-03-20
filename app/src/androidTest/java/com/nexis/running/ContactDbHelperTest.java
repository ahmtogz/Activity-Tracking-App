package com.nexis.running;

import static org.junit.Assert.*;
import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.nexis.running.model.ContactDbHelper;
import com.nexis.running.model.IUser;
import com.nexis.running.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ContactDbHelperTest {

    private ContactDbHelper dbHelper;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        dbHelper = new ContactDbHelper(context);
    }

    @Test
    public void testInsertUserData() {
        IUser user = new User("Ahmet", "ahmet@example.com", "pass123", "Male", 75, 30);
        boolean insertionResult = dbHelper.insertData(user);
        assertTrue("Insertion failed", insertionResult);
    }

    @Test
    public void testNonExistentUser() {
        assertNull(dbHelper.getUserByEmail("nonexistent@example.com"));
    }


    @Test
    public void testCheckEmail() {
        // Fügen Sie der Datenbank einen Benutzer hinzu und überprüfen Sie seine Existenz mit der checkEmail-Methode
        IUser testUser = new User("Test User", "test@example.com", "password", "Male", 70, 25);
        dbHelper.insertData(testUser);

        assertTrue(dbHelper.checkEmail("test@example.com"));
        assertFalse(dbHelper.checkEmail("nonexistent@example.com"));
    }

    @Test
    public void testCheckEmailPassword() {

        IUser testUser = new User("Test User", "test@example.com", "password", "Male", 70, 25);
        dbHelper.insertData(testUser);

        assertTrue(dbHelper.checkEmailPassword("test@example.com", "password"));
        assertFalse(dbHelper.checkEmailPassword("test@example.com", "wrongpassword"));
        assertFalse(dbHelper.checkEmailPassword("nonexistent@example.com", "password"));
    }

    @Test
    public void testGetUserByEmail() {
        IUser user = new User("Jane Doe", "jane@example.com", "pass456", "Female", 65, 28);
        dbHelper.insertData(user);
        User retrievedUser = dbHelper.getUserByEmail("jane@example.com");
        assertNotNull(retrievedUser);
        assertEquals("Jane Doe", retrievedUser.getName());
    }
    @Test
    public void testDisplayAllUserData() {
        IUser user1 = new User("John Doe", "john@example.com", "pass123", "Male", 75, 30);
        IUser user2 = new User("Jane Doe", "jane@example.com", "pass456", "Female", 65, 28);

        dbHelper.insertData(user1);
        dbHelper.insertData(user2);

        // This test would usually involve checking the printed output, but here we're using logging as an example
        dbHelper.displayData();
    }
}
