package android.example.yourclassroom.views.post;

import android.example.yourclassroom.R;
import android.example.yourclassroom.models.Post;
import android.example.yourclassroom.controllers.PostAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        imbBack = findViewById(R.id.imb_back);
        imbBack.setOnClickListener(v -> finish());

        // Hiển thị menu logout
        imbAccount = findViewById(R.id.imb_account);
        imbAccount.setOnClickListener(v -> {
            PopupMenu popupmenu = new PopupMenu(this, v);
            popupmenu.getMenuInflater().inflate(R.menu.account_menu, popupmenu.getMenu());
            popupmenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.item_logout) {

                }
                return false;
            });
            popupmenu.show();
        });

        cvPost = findViewById(R.id.cv_post);
        cvPost.setOnClickListener(v -> {
            Intent intent = new Intent(NewsFeedActivity.this, PostActivity.class);
            startActivityForResult(intent, REQUEST_CODE_POST);
        });


        rcvPost = findViewById(R.id.listContent);
        rcvPost.setLayoutManager(new LinearLayoutManager(this));

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(postList, this);
        rcvPost.setAdapter(postAdapter);


        postAdapter.loadData();
    }

}
