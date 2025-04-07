package android.example.yourclassroom.controllers;

import android.content.Context;
import android.example.yourclassroom.R;
import android.example.yourclassroom.models.CardItem;
import android.example.yourclassroom.View_Classroom.ClassViewHolder;
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

// Adapter để đổ dữ liệu lớp học vào RecyclerView
public class CardAdapter extends RecyclerView.Adapter<ClassViewHolder> {

    private List<CardItem> cardList; // Danh sách các lớp học
    private Context context; // Context để hiển thị Toast và Dialog

    public CardAdapter(List<CardItem> cardList, Context context) {
        // Nếu danh sách null thì khởi tạo danh sách rỗng
        this.cardList = (cardList != null) ? cardList : new ArrayList<>();
        this.context = context;
    }

    // Tạo ViewHolder cho mỗi item trong RecyclerView
    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new ClassViewHolder(v); // Trả về ViewHolder đã khởi tạo
    }

    // Gán dữ liệu vào ViewHolder tại vị trí cụ thể
    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        holder.bind(cardList.get(position), this);
    }

    // Trả về số lượng phần tử trong danh sách
    @Override
    public int getItemCount() {
        return (cardList != null) ? cardList.size() : 0;
    }

    // Hàm load dữ liệu từ Firebase Realtime Database
    public void loadData() {
        DatabaseReference myRef = FirebaseDatabase.getInstance()
                .getReference("classrooms"); // Tham chiếu tới node "classrooms"

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cardList.clear(); // Xóa dữ liệu cũ
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CardItem card = dataSnapshot.getValue(CardItem.class); // Đọc dữ liệu từng lớp học
                    if (card != null) {
                        cardList.add(card); // Thêm vào danh sách
                    }
                }
                notifyDataSetChanged(); // Cập nhật RecyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Nếu có lỗi xảy ra
                Toast.makeText(context, "Lấy dữ liệu thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hàm cho phép người dùng tham gia lớp bằng mã lớp
    public void joinCard(String codeClass) {
        DatabaseReference classRef = FirebaseDatabase.getInstance().getReference("classrooms");

        classRef.orderByChild("codeClass").equalTo(codeClass).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        CardItem card = dataSnapshot.getValue(CardItem.class);

                        String userId = "user123"; // ⚠️ Cần thay bằng FirebaseAuth.getInstance().getCurrentUser().getUid()
                        card.setIdUser(userId); // Gán ID người dùng vào lớp

                        classRef.child(dataSnapshot.getKey()).setValue(card) // Cập nhật lớp học
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(context, "Tham gia thành công!", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Tham gia thất bại!", Toast.LENGTH_SHORT).show();
                                });
                    }
                } else {
                    Toast.makeText(context, "Không tìm thấy lớp!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Lỗi khi tìm lớp!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hàm thêm lớp học mới
    public void insertCard(String classname) {
        DatabaseReference classRef = FirebaseDatabase.getInstance().getReference("classrooms");
        String newId = classRef.push().getKey(); // Tạo ID ngẫu nhiên
        String generatedCode = generateClassCode(); // Tạo mã lớp ngẫu nhiên

        if (newId != null) {
            // Tạo đối tượng lớp học mới
            CardItem card = new CardItem(newId, classname, generatedCode, "1", "1", "teacher");
            classRef.child(newId).setValue(card)
                    .addOnSuccessListener(aVoid ->
                            Toast.makeText(context, "Thêm thành công!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(context, "Thêm thất bại!", Toast.LENGTH_SHORT).show());
        }
    }

    // Hàm tạo mã lớp học ngẫu nhiên gồm 8 ký tự
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

    // Hàm xóa lớp học
    public void deleteCard(CardItem card) {
        if (card == null || card.getIdClass() == null) return;

        // Hiển thị dialog xác nhận xóa
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    // Xóa lớp học khỏi Firebase
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("classrooms").child(card.getIdClass());
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
