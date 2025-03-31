package android.example.yourclassroom.view.exercise.post;

import android.content.Intent;
import android.example.yourclassroom.controller.ExerciseAdapter;
import android.example.yourclassroom.model.Exercise;
import android.example.yourclassroom.view.exercise.AddExerciseActivity;
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

import java.util.Date;
import java.util.List;

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

        exerciseAdapter = new ExerciseAdapter(this);
        exerciseAdapter.loadExercise();
        exerciseAdapter.setOnItemClickListener(this);
        rvListExercise.setLayoutManager(new LinearLayoutManager(this));
        rvListExercise.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvListExercise.setAdapter(exerciseAdapter);

        btnAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(ListExerciseActivity.this, AddExerciseActivity.class);
                startActivity(addIntent);
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    public void onItemClick(Exercise exercise) {
        Toast.makeText(this, exercise.getTitle(), Toast.LENGTH_SHORT).show();
    }
}