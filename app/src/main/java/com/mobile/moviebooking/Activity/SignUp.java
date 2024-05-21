package com.mobile.moviebooking.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.apollographql.apollo3.exception.ApolloException;
import com.apollographql.apollo3.runtime.java.ApolloCallback;
import com.apollographql.apollo3.runtime.java.ApolloClient;
import com.example.rocketreserver.RegisterMutation;
import com.example.rocketreserver.type.UsersPermissionsRegisterInput;
import com.google.android.material.button.MaterialButton;
import com.mobile.moviebooking.R;

import org.jetbrains.annotations.NotNull;

public class SignUp extends AppCompatActivity {
    private MaterialButton btnSignUp;
    private EditText etUsername, etEmail, etPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findView();
        doSignUp();
    }

    private void doSignUp() {
        btnSignUp.setOnClickListener(v -> {

            String username = etUsername.getText().toString();
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            ApolloClient apolloClient = new ApolloClient.Builder()
                    .serverUrl("http://77.37.47.87:1338/graphql")
                    .build();

            final UsersPermissionsRegisterInput input = UsersPermissionsRegisterInput.builder()
                    .username(username)
                    .email(email)
                    .password(password).build();

            RegisterMutation registerMutation = RegisterMutation.builder()
                    .usersPermissionsRegisterInput(input)
                    .build();

            apolloClient.mutation(registerMutation).enqueue(apolloResponse -> {
                if (apolloResponse.hasErrors()) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    runOnUiThread(() -> {
//                        Toast.makeText(this, "Sign Up Success", Toast.LENGTH_SHORT).show();
//
//                        Log.e("username", apolloResponse.data.register.user.email);
//                        // create new share preference for user
//                        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString("token", apolloResponse.data.register.jwt);
//                        editor.putString("userName", apolloResponse.data.register.user.username);
//                        editor.putString("email", apolloResponse.data.register.user.email);
//                        editor.putString("userID", apolloResponse.data.register.user.id);
//                        editor.putString("jwt", apolloResponse.data.register.jwt);
//                        editor.apply();

                        // go to login page
                        startActivity(new Intent(SignUp.this, SignIn.class));
                    });
                }
            });






        });
    }

    private void findView() {
        btnSignUp = findViewById(R.id.btnSignUp);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etUsername = findViewById(R.id.etUserName);
    }
}