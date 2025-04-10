package android.example.yourclassroom.view.exercise;

import android.content.Intent;
import android.example.yourclassroom.controller.SubmissionAdapter;
import android.example.yourclassroom.model.Attachment;
import android.example.yourclassroom.model.User;
import android.example.yourclassroom.repository.AttachmentRepository;
import android.example.yourclassroom.repository.SubmissionRepository;
import android.example.yourclassroom.repository.UserRepository;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.example.yourclassroom.R;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GradeActivity extends AppCompatActivity {

    private Intent intent;
    private String idClass;
    private String idTeacher;
    private String idExercise;
    private ImageButton btnClose;
    private TextView btnSubmit;
    private RecyclerView rvGrading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_grade);

        intent = getIntent();
        idClass = intent.getStringExtra("idClass");
        idExercise = intent.getStringExtra("idExercise");
        idTeacher = intent.getStringExtra("idTeacher");

//        Binding view
        btnClose = findViewById(R.id.btn_close_from_grade);
        btnSubmit = findViewById(R.id.grading_btn_submit);
        rvGrading = findViewById(R.id.grading_rv);

//        Lấy danh sách học sinh
        List<User> students = new ArrayList<>();
        Map<String, List<Attachment>> studentAttachments = new HashMap<>();
        SubmissionAdapter adapter = new SubmissionAdapter(this, students, studentAttachments, idExercise);

        // Lấy danh sách id của học sinh trong lớp học
        UserRepository.getAllUserByIdClass(idClass, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                if (idTeacher != null && !idTeacher.equals(value)) {
                    // Lấy object user từ id của học sinh
                    UserRepository.getUserById(value, new ValueCallback<User>() {
                        @Override
                        public void onReceiveValue(User value) {
                            students.add(value);
                            String userId = value.getId();
                            studentAttachments.put(userId, new ArrayList<>());
                            adapter.notifyDataSetChanged();

                            // Lấy danh sách bài làm của học sinh
                            AttachmentRepository.getAllAttachmentByIdUser(userId, new ValueCallback<Attachment>() {
                                @Override
                                public void onReceiveValue(Attachment value) {
                                    if (value != null && value.getIdExercise().equals(idExercise)) {
                                        studentAttachments.get(userId).add(value);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });

        rvGrading.setLayoutManager(new LinearLayoutManager(this));
        rvGrading.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvGrading.setAdapter(adapter);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < students.size(); ++i) {
                    View itemView = rvGrading.getChildAt(i);
                    EditText scoreEdt = itemView.findViewById(R.id.grade_item_student_score);
                    String score = scoreEdt.getText().toString();
                    SubmissionRepository.updateScore(students.get(i).getId(), idExercise, score);
//                    Log.d("GradeActivity", "Student name: " + students.get(i).getFullname() + ", Score: " + score);
                }
                finish();
            }
        });

        btnClose.setOnClickListener(v -> finish());
    }
}