package android.example.yourclassroom;

import android.content.Intent;
import android.example.yourclassroom.view.exercise.ListExerciseActivity;
import android.os.Bundle;

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

        String idClass = "-OMoA2dc1KxbDwPrnbQG";
        String idUser = "3";
        String idTeacher = "1";

        Intent ListExerciseIntent = new Intent(this, ListExerciseActivity.class);
        ListExerciseIntent.putExtra("idClass", idClass);
        ListExerciseIntent.putExtra("idUser", idUser);
        ListExerciseIntent.putExtra("idTeacher", idTeacher);
        startActivity(ListExerciseIntent);
    }
}