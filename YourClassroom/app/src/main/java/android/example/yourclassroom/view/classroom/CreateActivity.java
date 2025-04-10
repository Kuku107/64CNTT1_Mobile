package android.example.yourclassroom.view.classroom;

import android.annotation.SuppressLint;
import android.example.yourclassroom.R;
import android.example.yourclassroom.controller.ClasroomAdapter;
import android.example.yourclassroom.model.Classroom;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class CreateActivity extends AppCompatActivity {

    private ImageButton imbClose; // Nút đóng màn hình
    private EditText edtClassname; // Ô nhập tên lớp
    private Button btnCreate; // Nút tạo lớp
    private List<Classroom> classroomList; // Danh sách lớp học
    private ClasroomAdapter classroomAdapter; // Adapter quản lý lớp học

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        // Khởi tạo danh sách và adapter
        classroomList = new ArrayList<>();
        classroomAdapter = new ClasroomAdapter(classroomList, this);

        // Ánh xạ view từ layout
        edtClassname = findViewById(R.id.edt_classname);
        btnCreate = findViewById(R.id.btnCreateClass);
        imbClose = findViewById(R.id.imb_close);

        // Xử lý sự kiện khi nhấn nút đóng
        imbClose.setOnClickListener(v -> finish()); // Đóng Activity

        // Xử lý sự kiện khi nhấn nút tạo lớp
        btnCreate.setOnClickListener(v -> {
            String className = edtClassname.getText().toString().trim(); // Lấy nội dung từ EditText
            if (!className.isEmpty()) { // Kiểm tra nội dung không rỗng
                classroomAdapter.insertClassroom(className); // Gửi dữ liệu vào Adapter để xử lý
                finish(); // Đóng màn hình sau khi tạo lớp thành công
            } else {
                Toast.makeText(CreateActivity.this, "Tên lớp không được để trống!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
