package android.example.yourclassroom.views.post;

import android.example.yourclassroom.controllers.PostAdapter;
import android.example.yourclassroom.model.Attachment;
import android.example.yourclassroom.model.Post;


import android.example.yourclassroom.R;
import android.example.yourclassroom.repository.AttachmentRepository;
import android.example.yourclassroom.repository.PostRepository;
import android.view.MenuInflater;
import android.view.View;
import android.webkit.ValueCallback;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PostViewHolder extends RecyclerView.ViewHolder {
    public TextView tvAuthor, tvDate, tvContent, tvAttachment;
    public ImageView imvMore;
    public CardView cvAttachment;
    public ListView lvAttachment;

    public PostViewHolder(@NonNull View itemView) {
        super(itemView);
        tvAuthor = itemView.findViewById(R.id.tv_name);
        tvDate = itemView.findViewById(R.id.tv_date);
        tvContent = itemView.findViewById(R.id.tv_content);
//        tvAttachment = itemView.findViewById(R.id.tv_attachment);
//        cvAttachment = itemView.findViewById(R.id.cv_attachment);
        imvMore = itemView.findViewById(R.id.imv_more);
        lvAttachment = itemView.findViewById(R.id.item_post_list_file);
    }

    public void bind(Post post, PostAdapter postAdapter) {
        tvAuthor.setText(post.getAuthor());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        tvDate.setText(sdf.format(post.getCreateAt()));

        tvContent.setText(post.getContent());

//        if (post.getIdExercise() != null && !post.getIdExercise().isEmpty()) {
//            cvAttachment.setVisibility(View.VISIBLE);
//            tvAttachment.setText("BaiTapTuan1.pdf");
//        } else {
//            cvAttachment.setVisibility(View.GONE);
//        }
        List<Attachment> attachmentList = new ArrayList<>();
        List<String> fileNames = new ArrayList<>();
        ArrayAdapter<String> topicAdapter = new ArrayAdapter<>(itemView.getContext(), android.R.layout.simple_list_item_1, fileNames);
        lvAttachment.setAdapter(topicAdapter);
        AttachmentRepository.getAllAttachmentByIdExercise(post.getIdExercise(), new ValueCallback<Attachment>() {
            @Override
            public void onReceiveValue(Attachment value) {
                attachmentList.add(value);
                fileNames.add(value.getFilename());
                topicAdapter.notifyDataSetChanged();
            }
        });



        imvMore.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(v.getContext(), v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.post_options_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_delete) {
                    PostRepository.deletePost(v.getContext(), post);
                    return true;
                }
                return false;
            });

            popup.show();
        });

    }

}
