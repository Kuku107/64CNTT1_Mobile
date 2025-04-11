package android.example.yourclassroom.controller;

import android.example.yourclassroom.model.Post;
import android.example.yourclassroom.view.post.PostViewHolder;

import android.content.Context;
import android.example.yourclassroom.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {
    private List<Post> postList;
    private Context context;
    private String idUser, idTeacher;

    public PostAdapter(List<Post> postList, Context context, String idUser, String idTeacher) {
        this.postList = postList;
        this.context = context;
        this.idUser = idUser;
        this.idTeacher = idTeacher;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getIdTeacher() {
        return idTeacher;
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


    public void getAllPost(String idClass) {
        FirebaseDatabase database =  FirebaseDatabase.getInstance("https://yourclassroom-6d328-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("posts");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    if (post.getIdClass().equals(idClass)) {
                        postList.add(post);
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Lấy danh sách tin thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Context getContext() {
        return context;
    }
}

