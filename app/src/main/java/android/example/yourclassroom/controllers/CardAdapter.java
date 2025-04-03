//package android.example.yourclassroom.controllers;
//
//
//import android.content.Context;
//import android.example.yourclassroom.R;
//import android.example.yourclassroom.models.CardItem;
//import android.example.yourclassroom.View_Classroom.ClassViewHolder;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.security.SecureRandom;
//import java.util.Date;
//import java.util.List;
//
//public class CardAdapter extends RecyclerView.Adapter<ClassViewHolder> {
//
//    private List<CardItem> cardList;
//    private Context context; // Context của ứng dụng
//
//
//    public CardAdapter(List<CardItem> cardList, Context context) {
//        this.cardList = cardList;
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false); // Tạo view từ file XML
//        return new ClassViewHolder(v); // Trả về ViewHolder
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
//        holder.bind(cardList.get(position), this); // Gán dữ liệu cho ViewHolder
//    }
//
//    @Override
//    public int getItemCount() {
//        return cardList.size();
//    }
//
//
//    public void loadData() {
//        FirebaseDatabase database = FirebaseDatabase.getInstance("https://yourclassroom-6d328-default-rtdb.asia-southeast1.firebasedatabase.app/");
//        DatabaseReference myRef = database.getReference("classrooms"); // Truy vấn đến "posts" trong Firebase
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                cardList.clear(); // Xóa danh sách cũ
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    CardItem post = dataSnapshot.getValue(CardItem.class); // Chuyển dữ liệu từ Firebase thành đối tượng Post
//                    cardList.add(post); // Thêm vào danh sách
//                }
//                notifyDataSetChanged(); // Cập nhật giao diện
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(context, "Lấy dữ liệu thất bại", Toast.LENGTH_SHORT).show(); // Hiển thị lỗi nếu truy vấn thất bại
//            }
//        });
//    }
//
//    public void insertCard(String content) {
//        FirebaseDatabase database = FirebaseDatabase.getInstance("https://yourclassroom-6d328-default-rtdb.asia-southeast1.firebasedatabase.app/");
//        DatabaseReference myRef = database.getReference("classrooms"); // Truy vấn đến "posts"
//        String generatedCode = generateClassCode(); // Gọi hàm tạo mã lớp
//
//        String newId = myRef.push().getKey(); // Tạo ID mới cho lớp
//        CardItem card = new CardItem(newId, content, generateClassCode(), "1", "1","teacher" );
//        // Tạo bài đăng mới
//
//        myRef.child(newId).setValue(card, new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
//                Toast.makeText(context, "Thêm thành công!", Toast.LENGTH_SHORT).show(); // Thông báo thêm thành công
//            }
//        });
//    }
//
//    // ✅ Thêm phương thức tạo mã lớp vào Adapter
//    private String generateClassCode() {
//        SecureRandom random = new SecureRandom();
//        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
//        int CODE_LENGTH = 8;
//
//        StringBuilder code = new StringBuilder(CODE_LENGTH);
//        for (int i = 0; i < CODE_LENGTH; i++) {
//            code.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
//        }
//        return code.toString();
//    }
//
//
//
//    public void deleteCard(Context context, CardItem card) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("Xác nhận xóa"); // Tiêu đề hộp thoại
//        builder.setMessage("Bạn có chắc chắn muốn xóa không?"); // Nội dung hộp thoại
//
//        builder.setPositiveButton("Xóa", (dialog, which) -> {
//            DatabaseReference myRef = FirebaseDatabase.getInstance("https://yourclassroom-6d328-default-rtdb.asia-southeast1.firebasedatabase.app/")
//                    .getReference("classrooms").child(String.valueOf(card.getIdClass())); // Truy vấn bài đăng cần xóa
//
//            myRef.removeValue()
//                    .addOnSuccessListener(aVoid -> {
//                        Toast.makeText(context, "Xóa thành công!", Toast.LENGTH_SHORT).show(); // Hiển thị thông báo khi xóa thành công
//                    })
//                    .addOnFailureListener(e -> {
//                        Toast.makeText(context, "Xóa thất bại!", Toast.LENGTH_SHORT).show(); // Hiển thị thông báo khi xóa thất bại
//                    });
//        });
//
//        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss()); // Đóng hộp thoại khi chọn Hủy
//
//        builder.show(); // Hiển thị hộp thoại xác nhận
//    }
//
//
//}


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
import androidx.annotation.Nullable;
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

public class CardAdapter extends RecyclerView.Adapter<ClassViewHolder> {

    private List<CardItem> cardList;
    private Context context; // Context của ứng dụng

    public CardAdapter(List<CardItem> cardList, Context context) {
        this.cardList = (cardList != null) ? cardList : new ArrayList<>(); // Đảm bảo danh sách không bị null
        this.context = context;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false); // Tạo view từ file XML
        return new ClassViewHolder(v); // Trả về ViewHolder
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        holder.bind(cardList.get(position), this); // Gán dữ liệu cho ViewHolder
    }

    @Override
    public int getItemCount() {
        return (cardList != null) ? cardList.size() : 0; // Tránh lỗi NullPointerException
    }

    public void loadData() {
        DatabaseReference myRef = FirebaseDatabase.getInstance()
                .getReference("classrooms"); // Truy vấn đến "classrooms"

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cardList.clear(); // Xóa danh sách cũ
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CardItem card = dataSnapshot.getValue(CardItem.class); // Chuyển dữ liệu từ Firebase thành đối tượng CardItem
                    if (card != null) {
                        cardList.add(card); // Thêm vào danh sách
                    }
                }
                notifyDataSetChanged(); // Cập nhật giao diện
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Lấy dữ liệu thất bại", Toast.LENGTH_SHORT).show(); // Hiển thị lỗi nếu truy vấn thất bại
            }
        });
    }

    public void joinCard(String codeClass) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("classrooms");

        // Truy vấn Firebase để tìm lớp học có codeClass tương ứng
        myRef.orderByChild("codeClass").equalTo(codeClass).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    // Nếu lớp tồn tại, tham gia lớp
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        CardItem card = dataSnapshot.getValue(CardItem.class); // Lấy thông tin lớp từ Firebase

                        // Cập nhật lớp học với thông tin người tham gia (ví dụ, thêm người tham gia)
                        String userId = "user123"; // Thay thế bằng ID người dùng thực tế
                        card.setIdUser(userId); // Giả sử bạn thêm ID người tham gia vào lớp

                        myRef.child(dataSnapshot.getKey()).setValue(card) // Cập nhật lại lớp với người tham gia
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(context, "Tham gia thành công!", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Tham gia thất bại!", Toast.LENGTH_SHORT).show();
                                });
                    }
                } else {
                    // Nếu không tìm thấy lớp với codeClass
                    Toast.makeText(context, "Không tìm thấy lớp!", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Nếu truy vấn thất bại
                Toast.makeText(context, "Lỗi khi tìm lớp!", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void insertCard(String content) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("classrooms"); // Truy vấn đến "classrooms"
        String newId = myRef.push().getKey(); // Tạo ID mới
        String generatedCode = generateClassCode(); // Gọi hàm tạo mã lớp

        if (newId != null) {
            CardItem card = new CardItem(newId, content, generatedCode, "1", "1", "teacher"); // Tạo đối tượng mới
            myRef.child(newId).setValue(card)
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

    public void deleteCard(CardItem card) {
        if (card == null || card.getIdClass() == null) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
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


