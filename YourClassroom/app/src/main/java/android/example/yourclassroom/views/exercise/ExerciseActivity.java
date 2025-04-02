package android.example.yourclassroom.views.exercise;

import android.content.Intent;
import android.example.yourclassroom.R;
import android.example.yourclassroom.views.post.NewsFeedActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ExerciseActivity extends AppCompatActivity {
    private ImageButton imbBack;
    private RecyclerView rcvExercise;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        rcvExercise = findViewById(R.id.rcvExercise);

        imbBack = findViewById(R.id.imb_back);
        imbBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_item_exercise) {
                    rcvExercise.smoothScrollToPosition(0);
                    return true;
                } else if (itemId == R.id.menu_item_news_feed) {
                    Intent intent = new Intent(ExerciseActivity.this, NewsFeedActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }

        });
    }
}
