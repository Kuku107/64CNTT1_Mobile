package android.example.yourclassroom.repository;

import android.webkit.ValueCallback;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ClassroomRepository {
    static FirebaseDatabase database = FirebaseDatabase.getInstance("https://yourclassroom-6d328-default-rtdb.asia-southeast1.firebasedatabase.app/");
    public static void getClassNameByIdClass(String idClass, ValueCallback<String> callBack) {
        DatabaseReference classRef = database.getReference("classrooms");
        classRef.child(idClass).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String className = snapshot.child("className").getValue(String.class);
                    callBack.onReceiveValue(className);
                } else {
                    callBack.onReceiveValue("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
