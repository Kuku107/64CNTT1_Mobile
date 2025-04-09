package android.example.yourclassroom.controllers;

import android.content.Intent;
import android.example.yourclassroom.models.Post;
import android.example.yourclassroom.views.post.PostActivity;
import android.example.yourclassroom.views.post.PostViewHolder;

import android.content.Context;
import android.example.yourclassroom.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {
    private List<Post> postList;
    private Context context;

    public PostAdapter(List<Post> postList, Context context) {
        this.postList = postList;
        this.context = context;
    }


    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.bind(postList.get(position),this);

    }


    @Override
    public int getItemCount() {
        return postList.size();
    }


    public void getAllPost() {
        FirebaseDatabase database =  FirebaseDatabase.getInstance("https://yourclassroom-6d328-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("posts");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    postList.add(post);
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Lấy dữ liệu thất bại", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void createPost(String content, String idTeacher, String idClass) {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://yourclassroom-6d328-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("posts");

        String newId = myRef.push().getKey();
        Post post = new Post(newId, idTeacher, idClass, null, "Nguyễn Trung Đức", new Date(), content);

        myRef.child(newId).setValue(post, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(context, "Thêm thành công!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void deletePost(Context context, Post post) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa không?");

        builder.setPositiveButton("Xóa", (dialog, which) -> {
            DatabaseReference myRef = FirebaseDatabase.getInstance("https://yourclassroom-6d328-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("posts").child(String.valueOf(post.getIdPost()));

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



//    public void createPostWithExercise(String content, String idTeacher, String idClass, String idExercise) {
//        FirebaseDatabase database = FirebaseDatabase.getInstance("https://yourclassroom-6d328-default-rtdb.asia-southeast1.firebasedatabase.app/");
//        DatabaseReference myRef = database.getReference("posts");
//
//        String newId = myRef.push().getKey();
//        Post post = new Post(newId, idTeacher, idClass, idExercise, "Nguyễn Trung Đức", new Date(), content);
//
//        myRef.child(newId).setValue(post, new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
//                Toast.makeText(context, "Thêm thành công!", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}

