package com.mobile.moviebooking.Activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.apollographql.apollo3.api.Optional;
import com.apollographql.apollo3.runtime.java.ApolloClient;
import com.mobile.moviebooking.R;
import  com.example.rocketreserver.CreateSeatMutation;
public class Empty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_empty);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String Token = "Bearer " +
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MywiaWF0IjoxNzE2MTM0ODA1LCJleHAiOjE3MTg3MjY4MDV9.vQ0PURIP7d4zQUjWaSMFUQe1Ff5jpJ_OC04rzcPlH4A";
        ApolloClient apolloClient = new ApolloClient.Builder()
                .serverUrl("http://77.37.47.87:1338/graphql")
                .addHttpHeader("Authorization", Token)
                .build();

        for (String row : new String[]{"A", "B", "C", "D", "E", "F", "G"}){
            for (int number = 1; number <= 10; number++){
                Optional<String> rowOptional = Optional.present(row);
                Optional<Integer> numberOptional = Optional.present(number);
                apolloClient.mutation(new CreateSeatMutation(rowOptional, numberOptional)).enqueue(repose->{
                    if (repose.hasErrors()){
                        System.out.println(repose.errors);
                    }else {
                        System.out.println("Seat created");
                    }
                });
            }
        }
    }
}