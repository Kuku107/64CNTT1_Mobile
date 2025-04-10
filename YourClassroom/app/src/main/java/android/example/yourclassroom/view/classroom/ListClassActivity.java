package android.example.yourclassroom.view.classroom;

import android.content.Intent;
import android.example.yourclassroom.R;
import android.example.yourclassroom.controller.ClasroomAdapter;
import android.example.yourclassroom.model.Classroom;
import android.example.yourclassroom.repository.UserRepository;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class ListClassActivity extends AppCompatActivity {

    private List<Classroom> classroomList;  // Danh sách các lớp học
    private ClasroomAdapter classroomAdapter;  // Adapter để hiển thị danh sách lớp
    private RecyclerView recyclerView;  // RecyclerView để hiển thị danh sách lớp học
    private String idUser;


    // Khởi tạo ActivityResultLauncher để xử lý kết quả từ JoinActivity
    private final ActivityResultLauncher<Intent> joinClassLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String classID = result.getData().getStringExtra("classID");  // Lấy mã lớp học từ JoinActivity
                    if (classID != null && !classID.isEmpty()) {
                        // Xử lý việc tham gia lớp học
                    } else {
                        Log.e("ListClassActivity", "Lỗi: classID nhận được là null hoặc rỗng");
                    }
                }
            }
    );

    // Khởi tạo ActivityResultLauncher để xử lý kết quả từ CreateActivity
    private final ActivityResultLauncher<Intent> createClassLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String className = result.getData().getStringExtra("className");  // Lấy tên lớp học từ CreateActivity
                    if (className != null && !className.isEmpty()) {
                        // Xử lý việc tạo lớp học mới
                    } else {
                        Log.e("ListClassActivity", "Lỗi: className nhận được là null hoặc rỗng");
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shows_class);  // Gán layout cho activity

        idUser = UserRepository.getCurrentUserId(this);

        // Khởi tạo các thành phần UI
        ImageButton ImbCircleAdd = findViewById(R.id.imb_CircleAdd);  // Nút thêm lớp học
        recyclerView = findViewById(R.id.listclass);  // RecyclerView để hiển thị danh sách lớp học

        if (recyclerView == null) {
            Log.e("ListClassActivity", "Lỗi: recyclerView không tìm thấy trong layout");
        }

        ImbCircleAdd.setOnClickListener(v -> showChooseDialog());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        classroomList = new ArrayList<>();
        classroomAdapter = new ClasroomAdapter(classroomList, this);
        recyclerView.setAdapter(classroomAdapter);



        classroomAdapter.loadData(idUser);  // Tải dữ liệu từ Firebase

    }

    private void showChooseDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_choose, null);

        TextView joinClass = dialogView.findViewById(R.id.joinClass);
        TextView createClass = dialogView.findViewById(R.id.createClass);
        ImageButton closeChoose = dialogView.findViewById(R.id.closeChoose);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(dialogView);

        closeChoose.setOnClickListener(v -> bottomSheetDialog.dismiss());

        joinClass.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            Intent intent = new Intent(ListClassActivity.this, JoinActivity.class);
            joinClassLauncher.launch(intent);
        });

        createClass.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            Intent intent = new Intent(ListClassActivity.this, CreateActivity.class);
            createClassLauncher.launch(intent);
        });

        bottomSheetDialog.show();
    }
}
