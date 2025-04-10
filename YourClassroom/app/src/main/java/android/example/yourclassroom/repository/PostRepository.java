package android.example.yourclassroom.repository;

import android.content.Context;
import android.example.yourclassroom.model.Post;
import android.webkit.ValueCallback;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class PostRepository {

    public static void createPost(Context context, String content, String idTeacher, String idClass) {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://yourclassroom-6d328-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("posts");

        String newId = myRef.push().getKey();
        UserRepository.getNameById(idTeacher, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                Post post = new Post(value, new Date(), content, newId, idClass, idTeacher, null);

                myRef.child(newId).setValue(post, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(context, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public static void createPostWithExercise(Context context, String content, String idTeacher, String idClass, String idExercise) {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://yourclassroom-6d328-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("posts");

        String newId = myRef.push().getKey();
        UserRepository.getNameById(idTeacher, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                Post post = new Post(value, new Date(), content, newId, idClass, idTeacher, idExercise);

                myRef.child(newId).setValue(post, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(context, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
    public static void deletePost(Context context, Post post) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa không?");

        builder.setPositiveButton("Xóa", (dialog, which) -> {
            DatabaseReference myRef = FirebaseDatabase.getInstance("https://yourclassroom-6d328-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("posts").child(String.valueOf(post.getId()));

            myRef.removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
                    });
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        builder.show();
    }
}
