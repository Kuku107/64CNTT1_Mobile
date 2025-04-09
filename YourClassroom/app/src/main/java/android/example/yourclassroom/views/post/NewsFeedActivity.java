package android.example.yourclassroom.views.post;

import android.example.yourclassroom.R;
import android.example.yourclassroom.model.Post;
import android.example.yourclassroom.controllers.PostAdapter;
import android.content.Intent;
import android.example.yourclassroom.repository.ClassroomRepository;
import android.example.yourclassroom.view.exercise.ListExerciseActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ValueCallback;
import android.widget.ImageButton;
import android.widget.TextView;

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
    private ImageButton imbBack;
    private RecyclerView rcvPost;
    private PostAdapter postAdapter;
    private CardView cvPost;
    private List<Post> postList;

    private TextView bannerTitleTv;

    String idTeacher, idClass, idUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_news_feed);


        rcvPost = findViewById(R.id.rcvPost);
        rcvPost.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        idClass = intent.getStringExtra("idClass");
        idUser = intent.getStringExtra("idUser");
        idTeacher = intent.getStringExtra("idTeacher");


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
                    Intent intent = new Intent(NewsFeedActivity.this, ListExerciseActivity.class);
                    intent.putExtra("idClass", idClass);
                    intent.putExtra("idUser", idUser);
                    intent.putExtra("idTeacher", idTeacher);
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
                intent.putExtra("idClass", idClass);
                intent.putExtra("idTeacher", idTeacher);
                startActivity(intent);
            }
        });


        postList = new ArrayList<>();
        postAdapter = new PostAdapter(postList, this);
        rcvPost.setAdapter(postAdapter);
        postAdapter.getAllPost();


        bannerTitleTv = findViewById(R.id.tv_bannerTitle);
        ClassroomRepository.getClassNameByIdClass(idClass, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String className) {
                bannerTitleTv.setText(className);
            }
        });
    }

}
