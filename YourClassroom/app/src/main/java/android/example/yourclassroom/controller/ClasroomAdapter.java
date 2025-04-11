package android.example.yourclassroom.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.example.yourclassroom.R;
import android.example.yourclassroom.model.Classroom;
import android.example.yourclassroom.repository.UserRepository;
import android.example.yourclassroom.view.classroom.ClassViewHolder;
import android.util.Log;
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

    private List<Classroom> classroomList; // Danh sách các lớp học

    private List<String> idClassroomList;
    private Context context; // Context để hiển thị Toast và Dialog
    private String databaseUrl = "https://yourclassroom-6d328-default-rtdb.asia-southeast1.firebasedatabase.app/";


    public ClasroomAdapter(List<Classroom> classroomList, Context context) {
        this.classroomList = (classroomList != null) ? classroomList : new ArrayList<>();
        this.idClassroomList = new ArrayList<>();
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
        holder.bind(classroomList.get(position), this, context);
    }

    @Override
    public int getItemCount() {
        return (classroomList != null) ? classroomList.size() : 0;
    }

    public void loadData(String idUser) {
        DatabaseReference myRef = FirebaseDatabase.getInstance(databaseUrl).getReference("users_classrooms");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                classroomList.clear(); // Xóa danh sách lớp học trước khi tải lại
                idClassroomList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String getIdUser = dataSnapshot.child("idUser").getValue(String.class);
                    String getIdClassroom = dataSnapshot.child("idClassroom").getValue(String.class);

                    if (getIdUser != null && getIdUser.equals(idUser) && getIdClassroom != null) {
                        DatabaseReference classRef = FirebaseDatabase.getInstance(databaseUrl).getReference("classrooms").child(getIdClassroom);
                        classRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Classroom classroom = snapshot.getValue(Classroom.class);
                                if (classroom != null && !idClassroomList.contains(classroom.getId())) {
                                    classroomList.add(classroom);
                                    idClassroomList.add(classroom.getId());
                                    notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void joinClassroom(String codeClass) {
        String currentUserId = UserRepository.getCurrentUserId(context);
        DatabaseReference joinRef = FirebaseDatabase.getInstance(databaseUrl).getReference("users_classrooms");
        DatabaseReference classRef = FirebaseDatabase.getInstance(databaseUrl).getReference("classrooms");

        classRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Classroom classroom = dataSnapshot.getValue(Classroom.class);
                    if (classroom != null && classroom.getCodeClass().equals(codeClass)) {
                        Log.d("DMMMMMMMMMMM", classroom.getClassName() + " co id: " + classroom.getId());
                        // Kiểm tra xem người dùng đã trong lớp chưa
                        joinRef.orderByChild("idUser").equalTo(currentUserId)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                        boolean alreadyJoined = false;
                                        for (DataSnapshot item : userSnapshot.getChildren()) {
                                            String joinedClassId = item.child("idClassroom").getValue(String.class);
                                            if (classroom.getId().equals(joinedClassId)) {
                                                alreadyJoined = true;
                                                break;
                                            }
                                        }

                                        if (alreadyJoined) {
                                            Toast.makeText(context, "Bạn đã tham gia lớp học này!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            String newId = joinRef.push().getKey();
                                            if (newId != null) {
                                                joinRef.child(newId).child("idClassroom").setValue(classroom.getId());
                                                joinRef.child(newId).child("idUser").setValue(currentUserId);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.e("Firebase", "Lỗi kiểm tra lớp học đã tham gia: " + error.getMessage());
                                    }
                                });

                        break; // dừng sau khi tìm thấy lớp phù hợp
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Lỗi truy cập classrooms: " + error.getMessage());
            }
        });
    }

    public void insertClassroom(String classname) {
        String currentUserId = UserRepository.getCurrentUserId(context);
        DatabaseReference classRef = FirebaseDatabase.getInstance(databaseUrl).getReference("classrooms");
        DatabaseReference joinRef = FirebaseDatabase.getInstance(databaseUrl).getReference("users_classrooms");
        String newId = classRef.push().getKey();
        String generatedCode = generateClassCode();


        if (newId != null) {
            Classroom classroom = new Classroom(newId, classname, generatedCode, currentUserId);
            classRef.child(newId).setValue(classroom)
                    .addOnSuccessListener(aVoid -> {
                            Toast.makeText(context, "Tạo mới lớp học thành công!", Toast.LENGTH_SHORT).show();
                            String users_classrooms_id = joinRef.push().getKey();
                            if (users_classrooms_id != null) {
                                joinRef.child(users_classrooms_id).child("idClassroom").setValue(classroom.getId());
                                joinRef.child(users_classrooms_id).child("idUser").setValue(currentUserId);
                            }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(context, "Tạo mới lớp học thất bại!", Toast.LENGTH_SHORT).show());
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

    public void deleteClasroom(Classroom classroom) {
        if (classroom == null || classroom.getId() == null) return;
        String currentUserId = UserRepository.getCurrentUserId(context);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa không?")
                .setPositiveButton("Xóa", (dialog, which) -> {

                    if (!classroom.getIdTeacher().equals(currentUserId)) {
                        DatabaseReference joinRef = FirebaseDatabase.getInstance(databaseUrl).getReference("users_classrooms");
                        joinRef.orderByChild("idUser").equalTo(currentUserId)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                        for (DataSnapshot item : userSnapshot.getChildren()) {
                                            String joinedClassId = item.child("idClassroom").getValue(String.class);
                                            if (classroom.getId().equals(joinedClassId)) {
                                                item.getRef().removeValue();
                                                break;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.e("Firebase", "Lỗi xóa lớp học: " + error.getMessage());
                                    }
                                });
                    } else {
                        DatabaseReference myRef = FirebaseDatabase.getInstance(databaseUrl).getReference("classrooms").child(classroom.getId());
                        myRef.removeValue()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(context, "Xóa lớp thành công!", Toast.LENGTH_SHORT).show();
                                    classroomList.remove(classroom);
                                    notifyDataSetChanged();
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(context, "Xóa lớp thất bại!", Toast.LENGTH_SHORT).show());
                    }
                })
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                .show();

    }
}
