package android.example.yourclassroom;

import android.content.Intent;
import android.example.yourclassroom.View_Classroom.ListClassActivity;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnMain = findViewById(R.id.btn_Main);
        btnMain.setOnClickListener(v -> {
            Intent myIntent = new Intent(MainActivity.this, ListClassActivity.class);
            startActivity(myIntent);
        });

    }
}
