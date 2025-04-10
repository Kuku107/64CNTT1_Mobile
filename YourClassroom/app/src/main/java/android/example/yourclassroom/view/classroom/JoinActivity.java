package android.example.yourclassroom.view.classroom;

import android.annotation.SuppressLint;
import android.example.yourclassroom.R;
import android.example.yourclassroom.controller.ClasroomAdapter;
import android.example.yourclassroom.model.classroom;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class JoinActivity extends AppCompatActivity {

    private ImageButton imbclose;  // Nút đóng activity
    private EditText edtcode;  // EditText để nhập mã lớp
    private Button btnjoin;  // Nút tham gia lớp
    private List<classroom> classroomList;  // Danh sách các lớp học
    private ClasroomAdapter classroomAdapter;  // Adapter để hiển thị danh sách lớp

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);  // Gán layout cho activity

        classroomList = new ArrayList<>();  // Khởi tạo danh sách lớp học
        classroomAdapter = new ClasroomAdapter(classroomList, this);  // Khởi tạo Adapter

        edtcode = findViewById(R.id.edt_code);  // Lấy tham chiếu đến EditText
        btnjoin = findViewById(R.id.btn_join);  // Lấy tham chiếu đến Button
        imbclose = findViewById(R.id.imb_close);  // Lấy tham chiếu đến ImageButton (nút đóng)

        // Xử lý sự kiện khi nhấn nút đóng
        imbclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // Đóng Activity khi nhấn nút
            }
        });

        // Xử lý sự kiện khi nhấn nút tham gia lớp
        btnjoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codeclass = edtcode.getText().toString().trim();  // Lấy mã lớp từ EditText
                if (!codeclass.isEmpty()) {  // Kiểm tra mã lớp có rỗng không
                    classroomAdapter.joinClassroom(codeclass);  // Gọi phương thức tham gia lớp
                    finish();  // Đóng màn hình sau khi tham gia
                } else {
                    Toast.makeText(JoinActivity.this, "Mã lớp không được để trống!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
