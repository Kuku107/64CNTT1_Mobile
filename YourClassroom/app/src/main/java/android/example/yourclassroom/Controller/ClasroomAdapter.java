package android.example.yourclassroom.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.example.yourclassroom.R;
import android.example.yourclassroom.model.classroom;
import android.example.yourclassroom.view.classroom.ClassViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class ClasroomAdapter extends RecyclerView.Adapter<ClassViewHolder> {

    private List<classroom> classroomList; // Danh sách các lớp học
    private Context context; // Context để hiển thị Toast và Dialog
    private String databaseUrl = "https://yourclassroom-6d328-default-rtdb.asia-southeast1.firebasedatabase.app/";

    public String getCurrentUserId() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("userUID", null);
    }
    public ClasroomAdapter(List<classroom> classroomList, Context context) {
        this.classroomList = (classroomList != null) ? classroomList : new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clasroom, parent, false);
        return new ClassViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        holder.bind(classroomList.get(position), this);
    }

    @Override
    public int getItemCount() {
        return (classroomList != null) ? classroomList.size() : 0;
    }

    public void loadData() {
        DatabaseReference myRef = FirebaseDatabase.getInstance(databaseUrl).getReference("classrooms");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                classroomList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    classroom classroom = dataSnapshot.getValue(classroom.class);
                    if (classroom != null) {
                        classroomList.add(classroom);
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Lấy dữ liệu thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void joinClassroom(String codeClass) {
        String currentUserId = getCurrentUserId();
        DatabaseReference joinRef = FirebaseDatabase.getInstance(databaseUrl).getReference("users_classrooms");
        DatabaseReference classRef = FirebaseDatabase.getInstance(databaseUrl).getReference("classrooms");

        classRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    classroom classroom = dataSnapshot.getValue(classroom.class);
                    if (classroom != null && classroom.getCodeClass().equals(codeClass)) {
                        String newId = joinRef.push().getKey();
                        if (newId != null) {
                            joinRef.child(newId).child("idClassroom").setValue(classroom.getIdClass());
                            joinRef.child(newId).child("idUser").setValue(currentUserId);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void insertClassroom(String classname) {
        String currentUserId = getCurrentUserId();
        DatabaseReference classRef = FirebaseDatabase.getInstance(databaseUrl).getReference("classrooms");
        String newId = classRef.push().getKey();
        String generatedCode = generateClassCode();

        if (newId != null) {
            classroom classroom = new classroom(newId, classname, generatedCode, currentUserId, "1", "teacher");
            classRef.child(newId).setValue(classroom)
                    .addOnSuccessListener(aVoid ->
                            Toast.makeText(context, "Thêm thành công!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(context, "Thêm thất bại!", Toast.LENGTH_SHORT).show());
        }
    }

    private String generateClassCode() {
        SecureRandom random = new SecureRandom();
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int CODE_LENGTH = 8;

        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }

    public void deleteClasroom(classroom classroom) {
        if (classroom == null || classroom.getIdClass() == null) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    DatabaseReference myRef = FirebaseDatabase.getInstance(databaseUrl).getReference("classrooms").child(classroom.getIdClass());
                    myRef.removeValue()
                            .addOnSuccessListener(aVoid ->
                                    Toast.makeText(context, "Xóa thành công!", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e ->
                                    Toast.makeText(context, "Xóa thất bại!", Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
