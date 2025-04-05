package android.example.yourclassroom.repository;

import android.content.Context;
import android.example.yourclassroom.model.Attachment;
import android.net.Uri;
import android.util.Log;
import android.webkit.ValueCallback;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class AttachmentRepository {
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance("https://yourclassroom-6d328-default-rtdb.asia-southeast1.firebasedatabase.app/");
    public static void pushDataToFirebase(String path, List<Attachment> fileList, Context context) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        for (Attachment file : fileList) if (file.getUri() != null) {
            StorageReference fileRef = storageReference.child(path + file.getFilename());
            Uri fileUri = Uri.parse(file.getUri());
            // Bắt đầu upload
            fileRef.putFile(fileUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();
                            Toast.makeText(context, "Tải lên thành công: " + downloadUrl, Toast.LENGTH_SHORT).show();
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Lỗi tải lên: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    })
                    .addOnProgressListener(snapshot -> {
                        double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        Toast.makeText(context, "Đang tải lên: " + (int) progress + "%", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    public static void getAllAttachmentByIdExercise(String idExercise, ValueCallback<Attachment> callBack) {
        DatabaseReference attachmentRef = database.getReference("attachments");
        attachmentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapShot : snapshot.getChildren()) {
                    Attachment attachment = snapShot.getValue(Attachment.class);
                    if (attachment != null && attachment.getIdExercise().equals(idExercise) && attachment.getIdSubmission() == null) {
                        callBack.onReceiveValue(attachment);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void getAllAttachmentByIdUser(String idUser, ValueCallback<Attachment> callBack) {
        DatabaseReference attachmentRef = database.getReference("attachments");
        attachmentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapShot : snapshot.getChildren()) {
                    Attachment attachment = snapShot.getValue(Attachment.class);
                    if (attachment != null && attachment.getIdSubmission() != null && attachment.getIdUser().equals(idUser)) {
                        callBack.onReceiveValue(attachment);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
