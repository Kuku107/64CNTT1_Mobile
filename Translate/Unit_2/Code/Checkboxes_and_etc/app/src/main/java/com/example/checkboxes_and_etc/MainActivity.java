package com.example.checkboxes_and_etc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

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

        CheckBox checkBox1 = findViewById(R.id.checkBox);
        CheckBox checkBox2 = findViewById(R.id.checkBox2);
        CheckBox checkBox3 = findViewById(R.id.checkBox3);
        CheckBox checkBox4 = findViewById(R.id.checkBox4);
        CheckBox checkBox5 = findViewById(R.id.checkBox5);
        Button showToastButton = findViewById(R.id.button);
        showToastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder selectedItems = new StringBuilder("Toppings: ");
                if (checkBox1.isChecked()) selectedItems.append(getString(R.string.chocolate_syrup)).append(" ");
                if (checkBox2.isChecked()) selectedItems.append(getString(R.string.sprinkles)).append(" ");
                if (checkBox3.isChecked()) selectedItems.append(getString(R.string.crushed_nuts)).append(" ");
                if (checkBox4.isChecked()) selectedItems.append(getString(R.string.cherries)).append(" ");
                if (checkBox5.isChecked()) selectedItems.append(getString(R.string.orio_cookies_crumbles)).append(" ");

                String message = selectedItems.toString().trim();
                if (message.equals("Toppings:")) {
                    message = "No toppings selected";
                }

                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}