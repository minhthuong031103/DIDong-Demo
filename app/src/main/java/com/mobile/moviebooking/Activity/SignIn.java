package com.mobile.moviebooking.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.apollographql.apollo3.runtime.java.ApolloClient;
import com.example.rocketreserver.LoginMutation;
import com.example.rocketreserver.type.UsersPermissionsLoginInput;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mobile.moviebooking.R;

public class SignIn extends AppCompatActivity {
    private MaterialButton signinBtn;
    private TextInputEditText etUsername, etPassword;
    private ImageView backBtnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findView();

        onLogin();
    }

    private void findView() {
        signinBtn = findViewById(R.id.signinBtn);
        etUsername = findViewById(R.id.gddn_txtusername);
        etPassword = findViewById(R.id.gddn_txtpass);
        backBtnSignIn = findViewById(R.id.backBtnSignIn);



        backBtnSignIn.setOnClickListener(v -> {
            finish();
        });
    }

    private void onLogin() {
        signinBtn.setOnClickListener(v -> {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();

            // Add your code here to authenticate the user
            ApolloClient apolloClient = new ApolloClient.Builder()
                    .serverUrl("http://77.37.47.87:1338/graphql")
                    .build();

            final UsersPermissionsLoginInput input = UsersPermissionsLoginInput.builder()
                    .identifier(username)
                    .password(password)
                    .provider("local")
                    .build();

            LoginMutation loginMutaion = LoginMutation.builder()
                    .usersPermissionsLoginInput(input)
                    .build();

            apolloClient.mutation(loginMutaion).enqueue(apolloResponse -> {
                if (apolloResponse.hasErrors()) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Sign In Success", Toast.LENGTH_SHORT).show();

                        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("token", apolloResponse.data.login.jwt);
                        editor.putString("userName", apolloResponse.data.login.user.username);
                        editor.putString("email", apolloResponse.data.login.user.email);
                        editor.putString("userID", apolloResponse.data.login.user.id);
                        editor.putString("jwt", apolloResponse.data.login.jwt);
                        editor.apply();

                        // go to login page
                        startActivity(new Intent(SignIn.this, HomePage.class));
                    });
                }
            });
        });
    }
}