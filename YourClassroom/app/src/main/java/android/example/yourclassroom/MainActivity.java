package android.example.yourclassroom;

import android.content.Intent;
import android.example.yourclassroom.view.exercise.ListExerciseActivity;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shows_class);

        String idClass = "-ONFpWG_4Kch5GD26f7x";
        String idUser = "1";
        String idTeacher = "1";

        Intent ListExerciseIntent = new Intent(this, ListExerciseActivity.class);
        ListExerciseIntent.putExtra("idClass", idClass);
        ListExerciseIntent.putExtra("idUser", idUser);
        ListExerciseIntent.putExtra("idTeacher", idTeacher);
        startActivity(ListExerciseIntent);
    }
}

