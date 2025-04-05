package android.example.yourclassroom.controller;

import android.content.Context;
import android.example.yourclassroom.R;
import android.example.yourclassroom.model.Attachment;
import android.example.yourclassroom.model.Exercise;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseHolder> {

    private List<Exercise> exerciseList;
    private Context context;
    private OnItemClickListener listener;
    private String idClass;

    public ExerciseAdapter(Context context, String idClass) {
        this.exerciseList = new ArrayList<>();
        this.context = context;
        this.idClass = idClass;
    }

    public ExerciseAdapter(List<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExerciseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
        return new ExerciseHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseHolder holder, int position) {
        Exercise exercise = exerciseList.get(position);

//        Binding
        holder.nameExercise.setText(exercise.getTitle());
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        holder.expiredDate.setText(format.format(exercise.getExpired()));

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(exercise);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (exerciseList != null) {
            return exerciseList.size();
        }
        return 0;
    }

    class ExerciseHolder extends RecyclerView.ViewHolder {
        private TextView nameExercise;
        private TextView expiredDate;
        private LinearLayout container;
        public ExerciseHolder(@NonNull View itemView) {
            super(itemView);
            this.nameExercise = itemView.findViewById(R.id.item_exercise_tv_name_exercise);
            this.expiredDate = itemView.findViewById(R.id.item_exercise_tv_expired);
            this.container = itemView.findViewById(R.id.container_item_exercise);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(Exercise exercise);
    }
    public void loadExercise() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://yourclassroom-6d328-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference exerciseRef = database.getReference("exercises");
        exerciseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                exerciseList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Exercise exercise = dataSnapshot.getValue(Exercise.class);
                    if (exercise != null && exercise.getIdClass().equals(idClass)) {
                        exercise.setId(dataSnapshot.getKey());
                        exerciseList.add(exercise);
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ExerciseAdapter", "Failed to read exercise data", error.toException());
                Toast.makeText(context, "Failed to load exercises", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void addExercise(Exercise exercise, List<Attachment> attachmentsList) {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://yourclassroom-6d328-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference exerciseRef = database.getReference("exercises");

        String idExercise = exerciseRef.push().getKey();
        exercise.setId(idExercise);

        DatabaseReference attachmentRef = database.getReference("attachments");
        for (Attachment attachment : attachmentsList) {
            String idAttachment = attachmentRef.push().getKey();
            attachment.setId(idAttachment);
            attachment.setIdExercise(idExercise);
            attachmentRef.child(idAttachment).setValue(attachment);
        }

        exerciseRef.child(idExercise).setValue(exercise, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
            }
        });
    }
}
