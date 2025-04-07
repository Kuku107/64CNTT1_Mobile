package android.example.yourclassroom.view.exercise;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.example.yourclassroom.R;
import android.example.yourclassroom.controller.AttachmentAdapter;
import android.example.yourclassroom.controller.ExerciseAdapter;
import android.example.yourclassroom.model.Attachment;
import android.example.yourclassroom.model.Exercise;
import android.example.yourclassroom.repository.AttachmentRepository;
import android.example.yourclassroom.repository.ExerciseRepository;
import android.example.yourclassroom.utils.EncodeDecodeFile;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddExerciseActivity extends AppCompatActivity implements AttachmentAdapter.OnItemClickListener{
    private static final int PICK_FILE_REQUEST = 1;
    private TextView tvDate;

    private RecyclerView rvListFile;
    private Button btnAssign;
    private final Calendar calendar = Calendar.getInstance();
    private AttachmentAdapter fileAdapter;

    private EditText edtTitle;
    private EditText edtInstruction;

    private LinearLayout uploadFromLocal;

    private ImageButton btnClose;

    private Intent intent;

    private String idClass;
    private String idAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_exercise);
        setSupportActionBar(findViewById(R.id.toolbar));

//        Binding
        uploadFromLocal = findViewById(R.id.upload_from_local);
        tvDate = findViewById(R.id.tv_date);
        btnAssign = findViewById(R.id.btn_assign_exercise);
        rvListFile = findViewById(R.id.rv_list_item_file);
        edtTitle = findViewById(R.id.add_exercise_title);
        edtInstruction = findViewById(R.id.add_exercise_instruction);
        btnClose = findViewById(R.id.btn_close_from_submit_assignment);

        intent = getIntent();
        idClass = intent.getStringExtra("idClass");
        idAuthor = intent.getStringExtra("idAuthor");

        LinearLayout datePickerContainer = findViewById(R.id.datePickerContainer);
        datePickerContainer.setOnClickListener(v -> showDatePickerDialog());

        uploadFromLocal.setOnClickListener(v -> openFilePicker());

//      Do du lieu vao recycler view
        fileAdapter = new AttachmentAdapter(this);
        fileAdapter.setOnItemClickListener(this);

        rvListFile.setLayoutManager(new LinearLayoutManager(this));
        rvListFile.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvListFile.setAdapter(fileAdapter);

        btnAssign.setOnClickListener(v -> {
            if (tvDate.getText().toString().isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn ngày hết hạn", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = edtTitle.getText().toString();
            String instruction = edtInstruction.getText().toString();
            Date expiredDate = new Date(tvDate.getText().toString());

            if (expiredDate.before(new Date())) {
                Toast.makeText(this, "Ngày hết hạn không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            Exercise exercise = new Exercise(null, idClass, title, instruction, new Date(), expiredDate, idAuthor);
            ExerciseAdapter.addExercise(exercise, fileAdapter.getAttachment());
            AttachmentRepository.pushDataToFirebase("exercises", fileAdapter.getAttachment(), this);
            finish();
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    calendar.set(selectedYear, selectedMonth, selectedDay);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    tvDate.setText(dateFormat.format(calendar.getTime()));
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData(); // Lấy URI của file

            final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            getContentResolver().takePersistableUriPermission(fileUri, takeFlags);

            String fileName = getFileName(fileUri);
            fileAdapter.addFile(new Attachment(fileUri.toString(), fileName));
        }
    }

    @Override
    public void onItemClick(Attachment file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(file.getUri()), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No PDF viewer app found. Please install one.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}