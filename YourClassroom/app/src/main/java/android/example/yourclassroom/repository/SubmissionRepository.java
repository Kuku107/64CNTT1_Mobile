package android.example.yourclassroom.repository;

import android.example.yourclassroom.model.Attachment;
import android.example.yourclassroom.model.Submission;
import android.webkit.ValueCallback;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class SubmissionRepository {
    private static FirebaseDatabase database = FirebaseDatabase.getInstance("https://yourclassroom-6d328-default-rtdb.asia-southeast1.firebasedatabase.app/");
    public static void addSubmission(Submission submission, List<Attachment> attachments) {
        DatabaseReference submissionRef = database.getReference("submissions");

        String submissionId = submissionRef.push().getKey();
        submission.setId(submissionId);

        DatabaseReference attachmentRef = database.getReference("attachments");
        for (Attachment attachment : attachments) {
            String attachmentId = attachmentRef.push().getKey();
            attachment.setId(attachmentId);
            attachment.setIdSubmission(submissionId);
            attachmentRef.child(attachmentId).setValue(attachment);
        }

        submissionRef.child(submissionId).setValue(submission, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
            }
        });
    }

    public static void getScoreByIdExerciseAndIdUser(String idExercise, String idUser, ValueCallback<Integer> callBack) {
        DatabaseReference submissionRef = database.getReference("submissions");
        submissionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapShot : snapshot.getChildren()) {
                    Submission submission = snapShot.getValue(Submission.class);
                    if (submission != null && submission.getIdExercise().equals(idExercise) && submission.getIdUser().equals(idUser)) {
                        callBack.onReceiveValue(submission.getScore());
                        return;
                    }
                }
                callBack.onReceiveValue(0); // No submission found
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
