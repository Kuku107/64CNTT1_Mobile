package android.example.yourclassroom.view.exercise;

import android.content.Intent;
import android.example.yourclassroom.controller.ClassroomAdapter;
import android.example.yourclassroom.controller.ExerciseAdapter;
import android.example.yourclassroom.model.Exercise;
import android.example.yourclassroom.repository.ClassroomRepository;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.example.yourclassroom.R;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class ListExerciseActivity extends AppCompatActivity implements ExerciseAdapter.OnItemClickListener {

    private Button btnAddExercise;
    private ImageButton btnBack;
    private RecyclerView rvListExercise;
    private ExerciseAdapter exerciseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_exercise);

        btnAddExercise = findViewById(R.id.btn_add_exercise);
        btnBack = findViewById(R.id.btn_back_from_list_exercise);
        rvListExercise = findViewById(R.id.rv_list_exercise);

        String idClass = "-OMoA2dc1KxbDwPrnbQG"; // get from intent extra
        String idUser = "1"; // get from intent extra
        final String[] idTeacher = new String[1];

//        Lay truong idTeacher tu truong idClass
        ClassroomRepository.getIdTeacherByIdClass(idClass, new ClassroomRepository.OnGetDataListener() {
            @Override
            public void onSuccess(Object value) {
                idTeacher[0] = (String) value;
                if (value != null) {

                    // Kiểm tra quyền để hiển thị nút thêm bài tập
                    if (idUser.equals(idTeacher[0])) {
                        btnAddExercise.setVisibility(View.VISIBLE);
                    } else {
                        btnAddExercise.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ListExerciseActivity.this, "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
                // Mặc định ẩn nút thêm bài tập khi có lỗi
                btnAddExercise.setVisibility(View.GONE);
            }
        });

//        Hien thi lish bai tap tu idClass
        exerciseAdapter = new ExerciseAdapter(this, idClass);
        exerciseAdapter.loadExercise();
        exerciseAdapter.setOnItemClickListener(this);
        rvListExercise.setLayoutManager(new LinearLayoutManager(this));
        rvListExercise.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvListExercise.setAdapter(exerciseAdapter);

//        Tao bai tap moi
        btnAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(ListExerciseActivity.this, AddExerciseActivity.class);
                addIntent.putExtra("idClass", idClass);
                addIntent.putExtra("idAuthor", idUser);
                startActivity(addIntent);
            }
        });

//        Dong intent
        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    public void onItemClick(Exercise exercise) {
        String idClass = "2"; // get from intent extra
        String idUser = "1"; // get from intent extra
        final String[] idTeacher = new String[1];
        ClassroomRepository.getIdTeacherByIdClass(idClass, new ClassroomRepository.OnGetDataListener() {
            @Override
            public void onSuccess(Object value) {
                idTeacher[0] = (String) value;

                // Di chuyển logic kiểm tra và chuyển activity vào đây
                if (idUser.equals(idTeacher[0])) {
                    Intent gradeIntent = new Intent(ListExerciseActivity.this, GradeActivity.class);
                    startActivity(gradeIntent);
                } else {
                    Intent submitAssignmentIntent = new Intent(ListExerciseActivity.this, SubmitAssignmentActivity.class);
                    submitAssignmentIntent.putExtra("exercise", exercise);
                    startActivity(submitAssignmentIntent);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ListExerciseActivity.this, "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}