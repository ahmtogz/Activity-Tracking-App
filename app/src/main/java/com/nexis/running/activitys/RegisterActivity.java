package com.nexis.running.activitys;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.nexis.running.model.ContactDbHelper;
import com.nexis.running.databinding.ActivityRegisterBinding;
import com.nexis.running.model.IUser;
import com.nexis.running.model.User;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    public ContactDbHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseHelper = new ContactDbHelper(this);

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.editTextName.getText().toString();
                String email = binding.editTextEmail.getText().toString();
                String password = binding.editTextPassword.getText().toString();
                String confirmPassword = binding.editTextConfirmPassword.getText().toString();

                int selectedRadioButtonId = binding.radioGroupGender.getCheckedRadioButtonId();
                String gender = "";
                if (selectedRadioButtonId != -1) {
                    RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                    gender = selectedRadioButton.getText().toString();
                }

                int weight = Integer.parseInt(binding.editTextWeight.getText().toString());
                int age = Integer.parseInt(binding.editTextAge.getText().toString());

                if (name.equals("") || email.equals("") || password.equals("") || confirmPassword.equals("") || gender.equals("") || weight == 0 || age == 0) {
                    showAlertMessage("All fields are mandatory");
                } else {
                    if (password.equals(confirmPassword)) {
                        // Create a User object using the entered data
                        IUser user = new User(name, email, password, gender, weight, age);

                        // Check if the user with the email already exists
                        Boolean checkUserEmail = databaseHelper.checkEmail(user.getEmail());

                        if (!checkUserEmail) {
                            // Insert the user data into the database
                            Boolean insert = databaseHelper.insertData(user);

                            if (insert) {
                                showAlertMessage("Signup Successfully!", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                showAlertMessage("Signup Failed!");
                            }
                        } else {
                            showAlertMessage("User already exists! Please login");
                        }
                    } else {
                        showAlertMessage("Invalid Password!");
                    }
                }
            }
        });

        binding.loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
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
