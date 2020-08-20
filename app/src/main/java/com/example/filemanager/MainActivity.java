package com.example.filemanager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "Main";
    public static final String[] PERMISSION = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean permission() {
        int p = 0;
        while (p < 2) {
            if (checkSelfPermission(PERMISSION[p]) != PackageManager.PERMISSION_GRANTED)
                return true;
            p++;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        if (permission())
            ActivityCompat.requestPermissions(this, PERMISSION, 3);
        final File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath());
        File[] files = file.listFiles();
        showFiles(files);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void showFiles(File[] file) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.container);
        FileAdapter fileAdapter = new FileAdapter(this);
        recyclerView.setAdapter(fileAdapter);
        recyclerView.setHasFixedSize(true);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<String> fileList = new ArrayList<>();
        ArrayList<Boolean> updown = new ArrayList<>();
        ArrayList<Integer> num = new ArrayList<>();
        ArrayList<Integer> margin = new ArrayList<>();
        for (int i = 0; i < file.length; i++) {
            fileList.add(String.valueOf(file[i].getAbsolutePath()));
            updown.add(false);
            num.add(i, calnum(file[i].getAbsolutePath()));
            margin.add(i, 0);
        }
        fileAdapter.swap(fileList, updown, num, margin);
    }

    public int calnum(String root) {
        final File file = new File(root);
        File[] files = file.listFiles();
        return files.length;
    }
}
