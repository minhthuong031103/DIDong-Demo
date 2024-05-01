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
        tvTotalPayment.setText(String.format("%,d", totalPayment).replace(',', '.') + " VNĐ");

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
        foods.add(new Food(1, "COMBO SOLO", "1 Bắp Ngọt 60oz + 1 Coke 32oz", 84000, "https://bit.ly/4dmeIuW"));
        foods.add(new Food(2, "COMBO COUPLE 2 NGĂN", "1 Bắp Ngọt 60oz + 2 Coke 32oz", 84000, "https://bit.ly/4dmeIuW"));
        foods.add(new Food(3, "COMBO SOLO", "1 Bắp Ngọt 60oz + 1 Coke 32oz", 84000, "https://bit.ly/4dmeIuW"));
        foods.add(new Food(4, "COMBO COUPLE", "1 Bắp Ngọt 60oz + 1 Coke 32oz", 84000, "https://bit.ly/4dmeIuW"));
        foods.add(new Food(5, "COMBO SOLO", "1 Bắp Ngọt 60oz + 2 Coke", 84000, "https://bit.ly/4dmeIuW"));
        foods.add(new Food(6, "CCOMBO COUPLE 2 NGĂN", "1 Bắp Ngọt 60oz + 1 Coke 32oz", 184000, "https://bit.ly/4dmeIuW"));
        foods.add(new Food(7, "COMBO COUPLE", "1 Bắp Ngọt 60oz + 1 Coke 32oz", 284000, "https://bit.ly/4dmeIuW"));

        FoodAdapter foodAdapter = new FoodAdapter(this, R.layout.food_item, foods);
        lvFood.setAdapter(foodAdapter);
    }
    public void updateTotalPayment() {
        totalPayment = getIntent().getIntExtra("totalPayment", 0);
        for(Food food : foods) {
            totalPayment += food.getPrice() * food.getQuantity();
        }
        tvTotalPayment.setText(String.format("%,d", totalPayment).replace(',', '.') + " VNĐ");
    }
}