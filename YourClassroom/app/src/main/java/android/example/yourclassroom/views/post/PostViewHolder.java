package android.example.yourclassroom.views.post;

import android.example.yourclassroom.controllers.PostAdapter;
import android.example.yourclassroom.models.Post;


import android.example.yourclassroom.R;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class PostViewHolder extends RecyclerView.ViewHolder {
    public TextView tvAuthor, tvDate, tvContent, tvAttachment;
    public ImageView imvMore;
    public CardView cvAttachment;

    public PostViewHolder(@NonNull View itemView) {
        super(itemView);
        tvAuthor = itemView.findViewById(R.id.tv_name);
        tvDate = itemView.findViewById(R.id.tv_date);
        tvContent = itemView.findViewById(R.id.tv_content);
        tvAttachment = itemView.findViewById(R.id.tv_attachment);
        cvAttachment = itemView.findViewById(R.id.cv_attachment);
        imvMore = itemView.findViewById(R.id.imv_more);
    }

    public void bind(Post post, PostAdapter postAdapter) {
        tvAuthor.setText(post.getAuthor());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        tvDate.setText(sdf.format(post.getCreateAt()));

        tvContent.setText(post.getContent());

//        if (attachment.getFilename() != null && !attachment.getFilename().isEmpty()) {
//            cvAttachment.setVisibility(View.VISIBLE);
//            tvAttachment.setText(attachment.getFilename());
//        } else {
//            cvAttachment.setVisibility(View.GONE);
//        }

        imvMore.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(v.getContext(), v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.post_options_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_delete) {
                    postAdapter.deletePost(v.getContext(), post);
                    return true;
                }
                return false;
            });

            popup.show();
        });
    }

}
