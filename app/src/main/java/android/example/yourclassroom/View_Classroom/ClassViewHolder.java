//package android.example.yourclassroom.View_Classroom;
//
//import android.example.yourclassroom.R;
//import android.example.yourclassroom.controllers.CardAdapter;
//import android.example.yourclassroom.models.CardItem;
//import android.view.MenuInflater;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.PopupMenu;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.cardview.widget.CardView;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.text.SimpleDateFormat;
//import java.util.Locale;
//
//public class ClassViewHolder extends RecyclerView.ViewHolder {
//
//    public TextView tvCodeClass, tvclassName;
//    public ImageView imvMore;
//
//    public ClassViewHolder(@NonNull View itemView) {
//        super(itemView);
//        // Ánh xạ các thành phần UI từ itemView
//        tvCodeClass = itemView.findViewById(R.id.tv_codeclass);
//        tvclassName = itemView.findViewById(R.id.tv_classname);
//        imvMore = itemView.findViewById(R.id.imv_more);
//
//    }
//
//    public void bind(CardItem cardItem, CardAdapter cardAdapter) {
//
//        tvCodeClass.setText(cardItem.getCodeClass());
//        tvclassName.setText(cardItem.getClassName());
//
//        // Xử lý sự kiện khi bấm vào nút "thêm tùy chọn" (3 chấm)
//        imvMore.setOnClickListener(v -> {
//            PopupMenu popup = new PopupMenu(v.getContext(), v);
//            MenuInflater inflater = popup.getMenuInflater();
//            inflater.inflate(R.menu.delete, popup.getMenu());
//
//            popup.setOnMenuItemClickListener(item -> {
//                if (item.getItemId() == R.id.action_delete) { // Nếu chọn xóa bài đăng
//                    cardAdapter.deleteCard(v.getContext(), cardItem); // Gọi phương thức xóa bài đăng
//                    return true;
//                }
//                return false;
//            });
//            popup.show(); // Hiển thị menu
//        });
//
//
//
//    }
//
//
//
//}

package android.example.yourclassroom.View_Classroom;

import android.example.yourclassroom.R;
import android.example.yourclassroom.controllers.CardAdapter;
import android.example.yourclassroom.models.CardItem;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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

    public void bind(CardItem cardItem, CardAdapter cardAdapter) {
        // Gán dữ liệu cho các thành phần trong ViewHolder
        tvCodeClass.setText(cardItem.getCodeClass());
        tvclassName.setText(cardItem.getClassName());

        // Xử lý sự kiện khi bấm vào nút "thêm tùy chọn" (3 chấm)
        imvMore.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(v.getContext(), v);  // Tạo PopupMenu
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.delete, popup.getMenu());  // Inflate menu từ XML

            // Xử lý sự kiện khi chọn mục trong menu
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_delete) {  // Nếu chọn xóa bài đăng
                    cardAdapter.deleteCard( cardItem);  // Gọi phương thức deleteCard từ Adapter
                    return true;
                }
                return false;
            });

            popup.show();  // Hiển thị menu
        });
    }
}
