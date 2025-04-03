//package android.example.yourclassroom.View_Classroom;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.example.yourclassroom.R;
//import android.example.yourclassroom.controllers.CardAdapter;
//import android.example.yourclassroom.models.CardItem;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.security.SecureRandom;
//import java.util.ArrayList;
//import java.util.List;
//
//public class CreateActivity extends AppCompatActivity {
//
//    private ImageButton imbClose;
//    private EditText edtClassname;
//    private Button btncreate;
//    private List<CardItem> cardList;
//    private CardAdapter cardAdapter;
//
//
//    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
//    private static final int CODE_LENGTH = 8;
//
//    @SuppressLint("MissingInflatedId")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_create);
//
//
//        cardList = new ArrayList<>();
//        cardAdapter = new CardAdapter(cardList, this);
//
//        edtClassname = findViewById(R.id.edt_classname);
//        btncreate = findViewById(R.id.btnCreateClass);
//        imbClose = findViewById(R.id.imb_close);
//
//        // Xử lý sự kiện khi nhấn nút đóng
//        imbClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish(); // Đóng Activity khi nhấn nút
//            }
//        });
//
//        // Xử lý sự kiện khi nhấn nút tạo lớp
//        btncreate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String classname = edtClassname.getText().toString().trim(); // Lấy nội dung từ EditText
//                if (!classname.isEmpty()) { // Kiểm tra nội dung có rỗng không
//                    cardAdapter.insertCard(classname); // Gửi bài đăng vào adapter để xử lý
//                    finish(); // Đóng màn hình sau khi đăng
//                } else {
//                    Toast.makeText(CreateActivity.this, "Nội dung không được để trống!", Toast.LENGTH_SHORT).show(); // Hiển thị thông báo lỗi
//                }
//            }
//        });
//    }
//
//    private String generateClassCode() {
//        SecureRandom random = new SecureRandom();
//        StringBuilder code = new StringBuilder(CODE_LENGTH);
//        for (int i = 0; i < CODE_LENGTH; i++) {
//            code.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
//        }
//        return code.toString();
//    }
//}

package android.example.yourclassroom.View_Classroom;

import android.annotation.SuppressLint;
import android.example.yourclassroom.R;
import android.example.yourclassroom.controllers.CardAdapter;
import android.example.yourclassroom.models.CardItem;
import android.os.Bundle;
import android.view.View;
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
    private List<CardItem> cardList; // Danh sách lớp học
    private CardAdapter cardAdapter; // Adapter quản lý lớp học

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        // Khởi tạo danh sách và adapter
        cardList = new ArrayList<>();
        cardAdapter = new CardAdapter(cardList, this);

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
                cardAdapter.insertCard(className); // Gửi dữ liệu vào Adapter để xử lý
                finish(); // Đóng màn hình sau khi tạo lớp thành công
            } else {
                Toast.makeText(CreateActivity.this, "Tên lớp không được để trống!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
