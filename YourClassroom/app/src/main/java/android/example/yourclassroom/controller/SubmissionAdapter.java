package android.example.yourclassroom.controller;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.example.yourclassroom.R;
import android.example.yourclassroom.model.Attachment;
import android.example.yourclassroom.model.User;
import android.example.yourclassroom.repository.SubmissionRepository;
import android.example.yourclassroom.repository.UserRepository;
import android.example.yourclassroom.view.exercise.GradeActivity;
import android.example.yourclassroom.view.exercise.SubmitAssignmentActivity;
import android.net.Uri;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SubmissionAdapter extends RecyclerView.Adapter<SubmissionAdapter.SubmissionHolder>{

    private Context context;
    private List<User> students;
    private Map<String, List<Attachment>> studentAttachments;
    private String idExercise;


    public SubmissionAdapter(Context context, List<User> students, Map<String, List<Attachment>> studentAttachments, String idExercise) {
        this.context = context;
        this.students = students;
        this.studentAttachments = studentAttachments;
        this.idExercise = idExercise;
    }

    @NonNull
    @Override
    public SubmissionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grade_item, parent, false);
        return new SubmissionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubmissionHolder holder, int position) {
        User student = students.get(position);
        List<Attachment> attachments = studentAttachments.get(student.getId());

        UserRepository.getNameById(student.getId(), new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                holder.name.setText(value);
            }
        });
        SubmissionRepository.getScoreByIdExerciseAndIdUser(idExercise, student.getId(), new ValueCallback<Integer>() {
            @Override
            public void onReceiveValue(Integer value) {
                holder.score.setText(String.valueOf(value));
            }
        });
        List<String> attachmentNames = new ArrayList<>();
        if (attachments != null) {
            for (Attachment attachment : attachments) {
                attachmentNames.add(attachment.getFilename());
            }
        }
        ArrayAdapter<String> attachmentNamesAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, attachmentNames);
        holder.assignmentListView.setAdapter(attachmentNamesAdapter);

        holder.assignmentListView.setOnItemClickListener((parent, view, pos, id) -> {
            Attachment file = attachments.get(pos);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(file.getUri()), "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "Không có ứng dụng thích hợp để đọc tệp tin", Toast.LENGTH_SHORT).show();
            }
        });

        holder.score.addTextChangedListener(new TextWatcher() {
            private String previousText = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                previousText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                holder.score.removeTextChangedListener(this);

                String input = s.toString();

                if (input.isEmpty()) {
                    holder.score.addTextChangedListener(this);
                    return;
                }

                // Xóa số 0 ở đầu nếu có (trừ khi chỉ nhập "0")
                if (input.length() > 1 && input.startsWith("0")) {
                    input = input.replaceFirst("^0+", ""); // loại bỏ các số 0 đứng đầu
                    if (input.isEmpty()) input = "0";
                }

                try {
                    int value = Integer.parseInt(input);
                    if (value > 100) {
                        input = input.substring(0, input.length() - 1);
                    }

                    holder.score.setText(input);
                    holder.score.setSelection(input.length());
                } catch (NumberFormatException e) {
                    holder.score.setText(previousText);
                    holder.score.setSelection(previousText.length());
                }

                holder.score.addTextChangedListener(this);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (students != null) {
            return students.size();
        }
        return 0;
    }

    class SubmissionHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private EditText score;
        private ListView assignmentListView;

        public SubmissionHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.grade_item_student_name);
            score = itemView.findViewById(R.id.grade_item_student_score);
            assignmentListView = itemView.findViewById(R.id.grade_item_list_view);
        }
    }

}
