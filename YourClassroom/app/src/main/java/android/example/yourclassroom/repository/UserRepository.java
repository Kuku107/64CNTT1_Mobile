package android.example.yourclassroom.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.example.yourclassroom.model.User;
import android.util.Log;
import android.webkit.ValueCallback;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserRepository {

    public static void getNameById(String uid, ValueCallback<String> callback) {
        DatabaseReference userRef = FirebaseDatabase
                .getInstance("https://yourclassroom-6d328-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("users").child(uid).child("fullname");

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                String fullname = task.getResult().getValue(String.class);
                callback.onReceiveValue(fullname);
            } else {
                callback.onReceiveValue("Không xác định");
            }
        });
    }


    public static void getAllUserByIdClass(String idClass, ValueCallback<String> callBack) {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://yourclassroom-6d328-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference userRef = database.getReference("users_classrooms");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapShot : snapshot.getChildren()) {
                    String idUser = snapShot.child("idUser").getValue(String.class);
                    String idClassroom = snapShot.child("idClassroom").getValue(String.class);
                    if (idClassroom.equals(idClass)) {
                        callBack.onReceiveValue(idUser);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void getUserById(String idUser, ValueCallback<User> callBack) {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://yourclassroom-6d328-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference userRef = database.getReference("users");
        userRef.child(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    callBack.onReceiveValue(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static String getCurrentUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("userUID", null);
    }
}
