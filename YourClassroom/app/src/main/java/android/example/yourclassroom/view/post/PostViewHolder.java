package android.example.yourclassroom.view.post;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.example.yourclassroom.controller.PostAdapter;
import android.example.yourclassroom.model.Attachment;
import android.example.yourclassroom.model.Post;


import android.example.yourclassroom.R;
import android.example.yourclassroom.repository.AttachmentRepository;
import android.example.yourclassroom.repository.PostRepository;
import android.net.Uri;
import android.view.MenuInflater;
import android.view.View;
import android.webkit.ValueCallback;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PostViewHolder extends RecyclerView.ViewHolder {
    public TextView tvAuthor, tvDate, tvContent;
    public ImageView imvMore;
    public ListView lvAttachment;

    public PostViewHolder(@NonNull View itemView) {
        super(itemView);
        tvAuthor = itemView.findViewById(R.id.tv_name);
        tvDate = itemView.findViewById(R.id.tv_date);
        tvContent = itemView.findViewById(R.id.tv_content);
        imvMore = itemView.findViewById(R.id.imv_more);
        lvAttachment = itemView.findViewById(R.id.item_post_list_file);
    }

    public void bind(Post post, PostAdapter postAdapter) {
        tvAuthor.setText(post.getAuthor());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        tvDate.setText(sdf.format(post.getCreatedAt()));
        tvContent.setText(post.getContent());

        // Khoi tao danh sach dinh kem va adapter de hien thi
        List<Attachment> attachmentList = new ArrayList<>();
        List<String> fileNames = new ArrayList<>();
//        ArrayAdapter<String> topicAdapter = new ArrayAdapter<>(itemView.getContext(), android.R.layout.simple_list_item_1, fileNames);
        ArrayAdapter<String> topicAdapter = new ArrayAdapter<>(itemView.getContext(), R.layout.item_attachment, R.id.tv_filename, fileNames);
        lvAttachment.setAdapter(topicAdapter);

        AttachmentRepository.getAllAttachmentByIdExercise(post.getIdExercise(), new ValueCallback<Attachment>() {
            @Override
            public void onReceiveValue(Attachment value) {
                attachmentList.add(value);
                fileNames.add(value.getFilename());
                topicAdapter.notifyDataSetChanged();

                // Cập nhật chiều cao sau khi thêm item
                setListViewHeightBasedOnChildren(lvAttachment, 2);
            }
        });


        lvAttachment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Attachment file = attachmentList.get(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(file.getUri()), "application/pdf");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                try {
                    postAdapter.getContext().startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(postAdapter.getContext(), "No application to read file pdf", Toast.LENGTH_SHORT).show();
                }
            }
        });


        String idUser = postAdapter.getIdUser();
        String idTeacher = postAdapter.getIdTeacher();

        if (idUser.equals(idTeacher)) {
            imvMore.setVisibility(View.VISIBLE);
        } else {
            imvMore.setVisibility(View.GONE);
        }


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

    // Ham tu dong dieu chinh chieu cao cua ListView dua tren so item (gioi han toi da)
    public static void setListViewHeightBasedOnChildren(ListView listView, int maxVisibleItems) {
        android.widget.ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) return;

        int totalHeight = 0;
        int itemsToMeasure = Math.min(listAdapter.getCount(), maxVisibleItems);

        for (int i = 0; i < itemsToMeasure; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        totalHeight += listView.getDividerHeight() * (itemsToMeasure - 1);
        listView.getLayoutParams().height = totalHeight;
        listView.requestLayout();
    }

}
