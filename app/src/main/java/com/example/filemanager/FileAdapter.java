package com.example.filemanager;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {
    File[] files;
    Context context;
    ArrayList<String> fileList, flist;
    ArrayList<Boolean> updown, ud;
    ArrayList<Integer> num, n, margin, m;


    public FileAdapter(Context context) {
        this.context = context;
        fileList = new ArrayList<>();
        updown = new ArrayList<>();
        num = new ArrayList<>();
    }

    @NonNull
    @Override
    public FileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_layout, parent, false);
        return new FileAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileAdapter.ViewHolder holder, int position) {
        String root = fileList.get(position);
        String[] name = fileList.get(position).split("/");
        holder.filename.setText(name[name.length - 1]);
        String uri;
        if (updown.get(position))
            uri = "@drawable/down";
        else
            uri = "@drawable/right";
        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
        Drawable res = context.getResources().getDrawable(imageResource);
        holder.arrow.setImageDrawable(res);
        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) holder.cardView.getLayoutParams();
        layoutParams.setMargins(100 * margin.get(position), 0, 0, 0);
        holder.cardView.requestLayout();
        if (isFile(root)) {
            uri = "@drawable/fileicon";
            holder.arrow.setVisibility(View.GONE);
        } else
            uri = "@drawable/folder";
        imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
        res = context.getResources().getDrawable(imageResource);
        holder.img.setImageDrawable(res);


    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public Boolean isFile(String root) {
        return new File(root).isFile();
    }

    public int calnum(String root) {
        final File file = new File(root);
        File[] files = file.listFiles();
        if (files != null)
            return files.length;
        return 0;
    }

    public void swap(ArrayList<String> filesL, ArrayList<Boolean> updown, ArrayList<Integer> n, ArrayList<Integer> marg) {
        this.updown = updown;
        num = n;
        fileList = filesL;
        margin = marg;
        this.notifyDataSetChanged();
    }

    public void getFiles(String root, int pos) {
        ArrayList<String> flist = new ArrayList<>(fileList);
        ArrayList<Boolean> ud = new ArrayList<>(updown);
        ArrayList<Integer> n = new ArrayList<>(num);
        ArrayList<Integer> m = new ArrayList<>(margin);
        final File file = new File(root);
        File[] files = file.listFiles();
        for (int i = pos + 1, j = 0; j < files.length; i++, j++) {
            int ma = m.get(pos);
            flist.add(i, files[j].getAbsolutePath());
            ud.add(i, false);
            n.add(i, calnum(files[j].getAbsolutePath()));
            m.add(i, ma + 1);
        }
        swap(flist, ud, n, m);
    }

    public void close(int pos) {
        for (int i = 0; i < n.get(pos); i++) {
            if (ud.get(pos + 1)) {
                close(pos + 1);
            }
            fileList.get(0);
            flist.remove(pos + 1);
            ud.remove(pos + 1);
            n.remove(pos + 1);
            m.remove(pos + 1);
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView filename;
        ImageView img, arrow;
        CardView cardView;
        ConstraintLayout cons;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            filename = (TextView) itemView.findViewById(R.id.filename);
            img = (ImageView) itemView.findViewById(R.id.imageView);
            arrow = (ImageView) itemView.findViewById(R.id.arrow);
            cardView = (CardView) itemView.findViewById(R.id.card);
            cons = (ConstraintLayout) itemView.findViewById(R.id.layout);
            cons.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    String root = fileList.get(pos);
                    if (updown.get(pos)) {
                        updown.set(pos, false);
                        flist = new ArrayList<>(fileList);
                        ud = new ArrayList<>(updown);
                        n = new ArrayList<>(num);
                        m = new ArrayList<>(margin);
                        close(pos);
                        swap(flist, ud, n, m);
                    } else {
                        updown.set(pos, true);
                        getFiles(root, pos);
                    }
                }
            });

        }
    }
}
