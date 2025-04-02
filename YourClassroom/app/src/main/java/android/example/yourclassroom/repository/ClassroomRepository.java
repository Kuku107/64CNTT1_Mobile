package android.example.yourclassroom.repository;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ClassroomRepository {
    public static void getIdTeacherByIdClass(String idClass, final OnGetDataListener callback) {
        DatabaseReference reference = FirebaseDatabase.getInstance("https://yourclassroom-6d328-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("classrooms/" + idClass + "/idTeacher");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Object value = dataSnapshot.getValue();
                    callback.onSuccess(value);
                } else {
                    callback.onFailure("Dữ liệu không tồn tại");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFailure(databaseError.getMessage());
            }
        });
    }

    // Interface để xử lý callback
    public interface OnGetDataListener {
        void onSuccess(Object value);
        void onFailure(String errorMessage);
    }
}
