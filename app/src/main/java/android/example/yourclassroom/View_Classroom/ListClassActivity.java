//package android.example.yourclassroom.View_Classroom;
//
//import android.content.Intent;
//import android.example.yourclassroom.R;
//import android.example.yourclassroom.controllers.CardAdapter;
//import android.example.yourclassroom.models.CardItem;
//import android.graphics.Color;
//import android.graphics.Typeface;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ScrollView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.cardview.widget.CardView;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.android.material.bottomsheet.BottomSheetDialog;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ListClassActivity extends AppCompatActivity {
//
//    //    private LinearLayout classContainer;
//    private List<CardItem> cardList;
//    private CardAdapter cardAdapter;
//    private RecyclerView recyclerView;
//
//    private final ActivityResultLauncher<Intent> joinClassLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            result -> {
//                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
//                    String classID = result.getData().getStringExtra("classID");
//                    if (classID != null && !classID.isEmpty()) {
////                        addClass(classID);
//                    } else {
//                        Log.e("MainActivity", "Lỗi: classID nhận được là null hoặc rỗng");
//                    }
//                }
//            }
//    );
//
//    private final ActivityResultLauncher<Intent> createClassLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            result -> {
//                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
//                    String className = result.getData().getStringExtra("className");
//                    if (className != null && !className.isEmpty()) {
////                        addClass(className);
//                    } else {
//                        Log.e("MainActivity", "Lỗi: className nhận được là null hoặc rỗng");
//                    }
//                }
//            }
//    );
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_shows_class);
//
//        ImageButton ImbCircleAdd = findViewById(R.id.imb_CircleAdd);
//        recyclerView = findViewById(R.id.listclass);
//
//        if (recyclerView == null) {
//            Log.e("MainActivity", "Lỗi: classContainer không tìm thấy trong layout");
//        }
//
//        ImbCircleAdd.setOnClickListener(v -> showChooseDialog());
//
//        recyclerView = findViewById(R.id.listclass);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Sử dụng layout dọc
//
//        cardList = new ArrayList<>(); // Khởi tạo danh sách bài đăng
//        cardAdapter = new CardAdapter(cardList, this); // Tạo adapter và gán danh sách bài đăng
//        recyclerView.setAdapter(cardAdapter); // Gán adapter cho RecyclerView
//
//        cardAdapter.loadData(); // Tải dữ liệu bài đăng lên danh sách
//
//    }
//
//    private void showChooseDialog() {
//        LayoutInflater inflater = LayoutInflater.from(this);
//        View dialogView = inflater.inflate(R.layout.dialog_choose, null);
//
//        TextView joinClass = dialogView.findViewById(R.id.joinClass);
//        TextView createClass = dialogView.findViewById(R.id.createClass);
//        ImageButton closeChoose = dialogView.findViewById(R.id.closeChoose);
//
//        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
//        bottomSheetDialog.setContentView(dialogView);
//
//        closeChoose.setOnClickListener(v -> bottomSheetDialog.dismiss());
//
//        joinClass.setOnClickListener(v -> {
//            bottomSheetDialog.dismiss();
//            Intent intent = new Intent(ListClassActivity.this, JoinActivity.class);
//            joinClassLauncher.launch(intent);
//        });
//        createClass.setOnClickListener(v -> {
//            bottomSheetDialog.dismiss();
//            Intent intent = new Intent(ListClassActivity.this, CreateActivity.class);
//            createClassLauncher.launch(intent);
//        });
//
//        bottomSheetDialog.show();
//    }
//}


package android.example.yourclassroom.View_Classroom;

import android.content.Intent;
import android.example.yourclassroom.R;
import android.example.yourclassroom.controllers.CardAdapter;
import android.example.yourclassroom.models.CardItem;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class ListClassActivity extends AppCompatActivity {

    private List<CardItem> cardList;  // Danh sách các lớp học
    private CardAdapter cardAdapter;  // Adapter để hiển thị danh sách lớp
    private RecyclerView recyclerView;  // RecyclerView để hiển thị danh sách lớp học

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

        // Khởi tạo các thành phần UI
        ImageButton ImbCircleAdd = findViewById(R.id.imb_CircleAdd);  // Nút thêm lớp học
        recyclerView = findViewById(R.id.listclass);  // RecyclerView để hiển thị danh sách lớp học

        // Kiểm tra xem RecyclerView có được tìm thấy không
        if (recyclerView == null) {
            Log.e("ListClassActivity", "Lỗi: recyclerView không tìm thấy trong layout");
        }

        // Sự kiện khi nhấn nút thêm lớp học
        ImbCircleAdd.setOnClickListener(v -> showChooseDialog());

        // Thiết lập RecyclerView với layout dọc
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo danh sách lớp học và adapter
        cardList = new ArrayList<>();
        cardAdapter = new CardAdapter(cardList, this);
        recyclerView.setAdapter(cardAdapter);  // Gán adapter cho RecyclerView

        // Tải dữ liệu lớp học từ Firebase
        cardAdapter.loadData();
    }

    // Phương thức để hiển thị BottomSheetDialog chọn tạo lớp hoặc tham gia lớp
    private void showChooseDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_choose, null);  // Inflate layout cho BottomSheetDialog

        TextView joinClass = dialogView.findViewById(R.id.joinClass);  // TextView để tham gia lớp
        TextView createClass = dialogView.findViewById(R.id.createClass);  // TextView để tạo lớp
        ImageButton closeChoose = dialogView.findViewById(R.id.closeChoose);  // Nút đóng BottomSheetDialog

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(dialogView);  // Đặt nội dung cho BottomSheetDialog

        // Sự kiện khi nhấn nút đóng
        closeChoose.setOnClickListener(v -> bottomSheetDialog.dismiss());

        // Sự kiện khi nhấn tham gia lớp
        joinClass.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();  // Đóng BottomSheetDialog
            Intent intent = new Intent(ListClassActivity.this, JoinActivity.class);  // Chuyển sang màn hình JoinActivity
            joinClassLauncher.launch(intent);  // Khởi chạy JoinActivity
        });

        // Sự kiện khi nhấn tạo lớp
        createClass.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();  // Đóng BottomSheetDialog
            Intent intent = new Intent(ListClassActivity.this, CreateActivity.class);  // Chuyển sang màn hình CreateActivity
            createClassLauncher.launch(intent);  // Khởi chạy CreateActivity
        });

        bottomSheetDialog.show();  // Hiển thị BottomSheetDialog
    }
}
