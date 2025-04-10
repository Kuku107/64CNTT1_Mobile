package android.example.yourclassroom.view.post;

import android.example.yourclassroom.controller.PostAdapter;
import android.example.yourclassroom.model.Post;
import android.example.yourclassroom.R;
import android.example.yourclassroom.repository.PostRepository;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity {
    private ImageButton imbClose;
    private Button btnPost;
    private EditText edtPostContent;
    private List<Post> postList;
    private PostAdapter postAdapter;

    String idTeacher, idClass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // Get the intent data
        idClass = getIntent().getStringExtra("idClass");
        idTeacher = getIntent().getStringExtra("idTeacher");

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(postList, this, idTeacher, idClass);

        imbClose = findViewById(R.id.imb_close);
        btnPost = findViewById(R.id.btn_post);
        edtPostContent = findViewById(R.id.edt_post_content);



        imbClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edtPostContent.getText().toString().trim();
                if (!content.isEmpty()) {
                    PostRepository.createPost(PostActivity.this, content, idTeacher, idClass);
                    finish();
                } else {
                    Toast.makeText(PostActivity.this, "Nội dung không được để trống!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
