package com.nexis.running.model;

import static org.junit.Assert.*;
import org.junit.Test;

public class UserTest {
    @Test
    public void testGetNamePositive() {
        User user = new User("Paul", "paul@example.com", "pass123", "Male", 70, 25);
        assertEquals("Paul", user.getName());
    }
    @Test
    public void testGetNameWrongName() {
        User user = new User("Paul", "paul@example.com", "pass123", "Male", 70, 25);
        assertNotEquals("pa", user.getName());
    }

    @Test
    public void testGetNameCaseSensitive() {
        User user = new User("Paul", "paul@example.com", "pass123", "Male", 70, 25);
        assertNotEquals("paul", user.getName());
    }

    @Test
    public void testGetNameEmptyName() {
        User user = new User("Paul", "paul@example.com", "pass123", "Male", 70, 25);
        assertFalse(user.getName().equals(""));
    }

    @Test
    public void testGetNameNullName() {
        User user = new User("Paul", "paul@example.com", "pass123", "Male", 70, 25);
        assertFalse(user.getName().equals(null));
    }
}