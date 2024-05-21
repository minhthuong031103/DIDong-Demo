package com.mobile.moviebooking.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.mobile.moviebooking.R;

public class MainActivity extends AppCompatActivity {
    private MaterialButton btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findView();
        onClick();
    }

    private void onClick() {
        btnLogin.setOnClickListener(v -> {
            // Open login activity
            startActivity(new Intent(MainActivity.this, SignIn.class));
        });

        btnRegister.setOnClickListener(v -> {
            // Open register activity
            startActivity(new Intent(MainActivity.this, SignUp.class));
        });
    }

    private void findView() {
        btnLogin = findViewById(R.id.signIn);
        btnRegister = findViewById(R.id.signUp);
    }
}