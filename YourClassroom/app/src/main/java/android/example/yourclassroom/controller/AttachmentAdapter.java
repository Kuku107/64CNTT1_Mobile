package android.example.yourclassroom.controller;

import android.content.Context;
import android.example.yourclassroom.R;
import android.example.yourclassroom.model.Attachment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.FileHolder>{

    private List<Attachment> fileList;
    private Context context;

    private OnItemClickListener listener;

    public AttachmentAdapter(Context context) {
        this.fileList = new ArrayList<>();
        this.context = context;
    }

    public AttachmentAdapter(Context context, List<Attachment> fileList) {
        this.context = context;
        this.fileList = fileList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public FileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file, parent, false);
        return new FileHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileHolder holder, int position) {
        Attachment file = fileList.get(position);

        holder.tvFileName.setText(file.getFilename());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(file);
                }
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileList.remove(file);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (fileList != null) {
            return fileList.size();
        }
        return 0;
    }

    public void addFile(Attachment file) {
        fileList.add(file);
        notifyDataSetChanged();
    }



    public List<Attachment> getAttachment() {
        return fileList;
    }

    class FileHolder extends RecyclerView.ViewHolder {
        private TextView tvFileName;
        private ImageButton btnDelete;

        public FileHolder(@NonNull View itemView) {
            super(itemView);
            this.tvFileName = itemView.findViewById(R.id.tv_item_file_name);
            this.btnDelete = itemView.findViewById(R.id.ib_item_file_delete);;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Attachment file);
    }
}
