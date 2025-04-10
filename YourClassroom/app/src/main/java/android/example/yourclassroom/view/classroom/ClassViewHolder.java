package android.example.yourclassroom.view.classroom;

import android.content.Context;
import android.content.Intent;
import android.example.yourclassroom.R;
import android.example.yourclassroom.controller.ClasroomAdapter;
import android.example.yourclassroom.model.Classroom;
import android.example.yourclassroom.repository.UserRepository;
import android.example.yourclassroom.view.post.NewsFeedActivity;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ClassViewHolder extends RecyclerView.ViewHolder {

    public TextView tvCodeClass, tvclassName;
    public ImageView imvMore;

    public ClassViewHolder(@NonNull View itemView) {
        super(itemView);
        // Ánh xạ các thành phần UI từ itemView
        tvCodeClass = itemView.findViewById(R.id.tv_codeclass);
        tvclassName = itemView.findViewById(R.id.tv_classname);
        imvMore = itemView.findViewById(R.id.imv_more);
    }

    public void bind(Classroom classroomItem, ClasroomAdapter clasroomAdapter, Context context) {
        // Gán dữ liệu cho các thành phần trong ViewHolder
        tvCodeClass.setText(classroomItem.getCodeClass());
        tvclassName.setText(classroomItem.getClassName());

        String idUser = UserRepository.getCurrentUserId(context);  // Lấy ID người dùng hiện tại

        // Xử lý sự kiện khi bấm vào nút "thêm tùy chọn" (3 chấm)
        imvMore.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(v.getContext(), v);  // Tạo PopupMenu
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.delete, popup.getMenu());  // Inflate menu từ XML

            // Xử lý sự kiện khi chọn mục trong menu
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_logout) {  // Nếu chọn xóa bài đăng
                    clasroomAdapter.deleteClasroom( classroomItem);  // Gọi phương thức deleteCard từ Adapter
                    return true;
                }
                return false;
            });

            popup.show();  // Hiển thị menu
        });

        itemView.setOnClickListener(v -> {
            // Xử lý sự kiện khi bấm vào item
            Intent postIntent = new Intent(context, NewsFeedActivity.class);
            postIntent.putExtra("idClass", classroomItem.getId());
            postIntent.putExtra("idTeacher", classroomItem.getIdTeacher());
            postIntent.putExtra("idUser", idUser);
            context.startActivity(postIntent);  // Chuyển đến NewsFeedActivity
        });
    }
}
