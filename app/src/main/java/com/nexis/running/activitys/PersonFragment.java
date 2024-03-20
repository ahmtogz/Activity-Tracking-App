package com.nexis.running.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nexis.running.R;
import com.nexis.running.model.ContactDbHelper;
import com.nexis.running.model.IUser;
import com.nexis.running.model.User;

public class PersonFragment extends Fragment {
    private TextView textViewWelcome;
    private String userName;
    private Button logoutButton;
    private Button personalParametersButton;

    public PersonFragment(){
    }
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, container, false);

        String userAsString = getArguments().getString("user");
        if (userAsString!=null){
            Gson gson = new Gson();

            user = gson.fromJson(userAsString, User.class);
            Log.d("PersonFragment", "User: " + gson.toJson(user));

        }
        userName = user.getName();
        textViewWelcome = view.findViewById(R.id.textViewWelcome);
        textViewWelcome.setText("Hi " + userName + "!");



        personalParametersButton = view.findViewById(R.id.personal_parameters_button);

        logoutButton = view.findViewById(R.id.logout_button);


        personalParametersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle personal parameters button click if needed
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logoutUser();
            }
        });

        return view;
    }
    private void logoutUser() {
        /*
        SharedPreferences preferences = getActivity().getSharedPreferences("user_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
         */

        Intent intent = new Intent(getActivity(), WelcomeActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
