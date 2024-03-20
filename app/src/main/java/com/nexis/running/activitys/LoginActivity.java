package com.nexis.running.activitys;

import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.nexis.running.model.ContactDbHelper;
import com.nexis.running.databinding.ActivityLoginBinding;
import com.nexis.running.model.IUser;
import com.nexis.running.model.User;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    ContactDbHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseHelper = new ContactDbHelper(this);

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.loginEmail.getText().toString();
                String password = binding.loginPassword.getText().toString();

                if (email.equals("") || password.equals("")) {
                    showAlertMessage("All fields are mandatory!");
                } else {
                    // Create a User object using the entered data
                    IUser user = new User(email, password);

                    // Check user credentials
                    Boolean checkCredentials = databaseHelper.checkEmailPassword(user.getEmail(), user.getPassword());

                    if (checkCredentials) {
                        // Fetch complete user information from the database
                        User completeUser = databaseHelper.getUserByEmail(email);

                        // Convert the complete user object to JSON
                        Gson gson  = new Gson();
                        String userAsString = gson.toJson(completeUser);

                        showAlertMessage("Login Successfully!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra("user", userAsString);
                                startActivity(intent);
                            }
                        });
                    } else {
                        showAlertMessage("Invalid Credentials!");
                    }
                }
            }
        });

        binding.signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    public void showAlertMessage(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    public void showAlertMessage(String message, DialogInterface.OnClickListener positiveClickListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", positiveClickListener)
                .show();
    }
}
