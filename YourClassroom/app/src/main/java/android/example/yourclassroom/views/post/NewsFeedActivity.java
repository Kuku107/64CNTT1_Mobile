package android.example.yourclassroom.views.post;

import android.example.yourclassroom.MainActivity;
import android.example.yourclassroom.R;
import android.example.yourclassroom.models.Post;
import android.example.yourclassroom.controllers.PostAdapter;
import android.content.Intent;
import android.example.yourclassroom.views.exercise.ExerciseActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class NewsFeedActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_POST = 1; // Mã request để nhận dữ liệu
    private ImageButton imbBack, imbAccount;
    private RecyclerView rcvPost;
    private PostAdapter postAdapter;
    private CardView cvPost;
    private List<Post> postList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_news_feed);

        rcvPost = findViewById(R.id.rcvPost);
        rcvPost.setLayoutManager(new LinearLayoutManager(this));

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(postList, this);
        rcvPost.setAdapter(postAdapter);


        postAdapter.loadData();


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
                if (itemId == R.id.menu_item_news_feed) {
                    rcvPost.smoothScrollToPosition(0);
                    return true;
                } else if (itemId == R.id.menu_item_exercise) {
                    Intent intent = new Intent(NewsFeedActivity.this, ExerciseActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }

        });


        cvPost = findViewById(R.id.cv_post);
        cvPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsFeedActivity.this, PostActivity.class);
                startActivityForResult(intent, REQUEST_CODE_POST);
            }
        });

    }

}
