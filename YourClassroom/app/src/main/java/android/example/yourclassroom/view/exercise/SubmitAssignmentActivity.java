package android.example.yourclassroom.view.exercise;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.example.yourclassroom.controller.AttachmentAdapter;
import android.example.yourclassroom.model.Attachment;
import android.example.yourclassroom.model.Exercise;
import android.example.yourclassroom.model.Submission;
import android.example.yourclassroom.repository.AttachmentRepository;
import android.example.yourclassroom.repository.SubmissionRepository;
import android.example.yourclassroom.repository.UserRepository;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.example.yourclassroom.R;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SubmitAssignmentActivity extends AppCompatActivity implements AttachmentAdapter.OnItemClickListener {

    private static final int PICK_FILE_REQUEST = 1;
    private Intent intent;
    private Exercise exercise;
    private String idUser;
    private String idTeacher;

    private TextView title, author, createdAt, expired, instruction, score;
    private ListView topicListView;
    private List<Attachment> topicList;

    private RecyclerView assignmentRecyclerView;
    private TextView addBtn, submitBtn;
    private AttachmentAdapter assignmentList;
    private ImageButton closeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_submit_assignment);


        intent = getIntent();
        topicList = new ArrayList<>();
        exercise = (Exercise) intent.getSerializableExtra("exercise");
        idUser = intent.getStringExtra("idUser");
        idTeacher = intent.getStringExtra("idTeacher");

//      Binding
        title = findViewById(R.id.submit_assignment_exercise_title);
        author = findViewById(R.id.submit_assignment_exercise_author);
        createdAt = findViewById(R.id.submit_assignment_exercise_createdAt);
        expired = findViewById(R.id.submit_assignment_exercise_expired);
        instruction = findViewById(R.id.submit_assignment_exercise_instruction);
        topicListView = findViewById(R.id.submit_assignment_exercise_list_file);
        assignmentRecyclerView = findViewById(R.id.submit_assignment_rv);
        addBtn = findViewById(R.id.submit_assignment_btn_add);
        submitBtn = findViewById(R.id.submit_assignment_btn_submit);
        closeBtn = findViewById(R.id.btn_close_from_submit_assignment);
        score = findViewById(R.id.submit_assignment_score);

//        Gan gia tri vao cac TextView va ListView
        title.setText(exercise.getTitle());
        UserRepository.getNameById(idTeacher, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                author.setText(value);
            }
        });
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        createdAt.setText(format.format(exercise.getCreateAt()));
        expired.setText("Đến hạn " + format.format(exercise.getExpired()));
        instruction.setText(exercise.getInstruction());
        List<String> topicNames = new ArrayList<>();
        ArrayAdapter<String> topicAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, topicNames);
        AttachmentRepository.getAllAttachmentByIdExercise(exercise.getId(), new ValueCallback<Attachment>() {
            @Override
            public void onReceiveValue(Attachment value) {
                    topicList.add(value);
                    topicNames.add(value.getFilename());
                    topicAdapter.notifyDataSetChanged();
            }
        });
        topicListView.setAdapter(topicAdapter);
        topicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Attachment file = topicList.get(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(file.getUri()), "application/pdf");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(SubmitAssignmentActivity.this, "No application found to open this file", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        Lay diem
        SubmissionRepository.getScoreByIdExerciseAndIdUser(exercise.getId(), idUser, new ValueCallback<Integer>() {
            @Override
            public void onReceiveValue(Integer value) {
                score.setText(String.valueOf(value) + "/100");
            }
        });

//        Do du lieu vao recycler view
        List<Attachment> assignmentSubmitted = new ArrayList<>();
        assignmentList = new AttachmentAdapter(this, assignmentSubmitted);
        AttachmentRepository.getAllAttachmentByIdUser(idUser, new ValueCallback<Attachment>() {
            @Override
            public void onReceiveValue(Attachment value) {
                if (value != null && value.getIdExercise().equals(exercise.getId())) {
                    assignmentSubmitted.add(value);
                    assignmentList.notifyDataSetChanged();
                }
            }
        });

        assignmentList.setOnItemClickListener(this);

        assignmentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        assignmentRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        assignmentRecyclerView.setAdapter(assignmentList);

        if (new Date().getTime() > exercise.getExpired().getTime()) {
            addBtn.setEnabled(false);
            addBtn.setVisibility(View.INVISIBLE);

            submitBtn.setEnabled(false);
            submitBtn.setVisibility(View.INVISIBLE);
        }

//        Xu ly su kien click nut add
        addBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("application/pdf");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, PICK_FILE_REQUEST);
        });

//        Xu ly su kien click nut submit
        submitBtn.setOnClickListener(v -> {
            Submission submission = new Submission();
            submission.setIdUser(idUser);
            submission.setIdExercise(exercise.getId());
            submission.setScore(0);
            SubmissionRepository.addSubmission(submission, assignmentList.getAttachment());
            AttachmentRepository.pushDataToFirebase("assignments/", assignmentList.getAttachment(), this);
            finish();
        });

//        Xu ly su kien click nut close
        closeBtn.setOnClickListener(v -> {
            finish();
        });
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
            Attachment attachment = new Attachment(fileUri.toString(), fileName);
            attachment.setIdUser(idUser);
            attachment.setIdExercise(exercise.getId());
            assignmentList.addFile(attachment);
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