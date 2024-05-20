package com.mobile.moviebooking.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.apollographql.apollo3.runtime.java.ApolloClient;
import com.example.rocketreserver.GetFoodQuery;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.mobile.moviebooking.Adapter.FoodAdapter;
import com.mobile.moviebooking.Entity.Food;
import com.mobile.moviebooking.R;

import java.util.ArrayList;
import java.util.List;

public class SelectFood extends AppCompatActivity {
    private List<Food> foods = new ArrayList<>();
    private ListView lvFood;
    private int totalPayment = 0;
    private TextView tvTotalPayment;
    private ImageView backBtn;
    private ExtendedFloatingActionButton ctnBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_food);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById();

        totalPayment = getIntent().getIntExtra("totalPayment", 0);
        tvTotalPayment.setText(String.format("%,d", totalPayment).replace(',', '.') + " VND");

        loadFood();

        backBtn.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        ctnBtn.setOnClickListener(v -> {
            Intent intent = new Intent(SelectFood.this, Payment.class);
            intent.putExtra("totalPayment", totalPayment);
            startActivity(intent);
        });
    }

    private void findViewById() {
        lvFood = findViewById(R.id.lv_food);
        tvTotalPayment = findViewById(R.id.totalPayment);
        backBtn = findViewById(R.id.back);
        ctnBtn = findViewById(R.id.btn);
    }

    private void loadFood() {
        String Token = "Bearer " +
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MywiaWF0IjoxNzE2MTM0ODA1LCJleHAiOjE3MTg3MjY4MDV9.vQ0PURIP7d4zQUjWaSMFUQe1Ff5jpJ_OC04rzcPlH4A";
        ApolloClient apolloClient = new ApolloClient.Builder()
                .serverUrl("http://77.37.47.87:1338/graphql")
                .addHttpHeader("Authorization", Token)
                .build();
        apolloClient.query(new GetFoodQuery())
                .enqueue(response -> {
                    List<GetFoodQuery.Data1> data = response.data.foodItems.data;
                    for (GetFoodQuery.Data1 food : data) {
                        foods.add(new Food(
                                    Integer.parseInt(food.id),
                                    food.attributes.name,
                                    food.attributes.description,
                                    Integer.parseInt(food.attributes.price.toString()),
                                    food.attributes.image.data.attributes.url));
                    }
                    runOnUiThread(() -> {
                        FoodAdapter foodAdapter = new FoodAdapter(SelectFood.this, R.layout.food_item, foods);
                        lvFood.setAdapter(foodAdapter);
                    });
                });


    }
    public void updateTotalPayment() {
        totalPayment = getIntent().getIntExtra("totalPayment", 0);
        for(Food food : foods) {
            totalPayment += food.getPrice() * food.getQuantity();
        }
        tvTotalPayment.setText(String.format("%,d", totalPayment).replace(',', '.') + " VNĐ");
    }
}