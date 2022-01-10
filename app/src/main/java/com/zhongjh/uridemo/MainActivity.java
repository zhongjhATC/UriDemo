package com.zhongjh.uridemo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnFileSelector).setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");//无类型限制
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, 1);
        });

        findViewById(R.id.btnFileSelectorRename).setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");//无类型限制
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, 1);
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        if (getIntent() != null && getIntent().getData() != null) {
            Uri uri = getIntent().getData();
            File file = UriUtils.uriToFile(getApplicationContext(), uri);
        }

        // 初始化创建文件测试
        File file = new File(getFilesDir().getAbsolutePath() + "/" + "test.txt");
        // 判断文件是否存在，存在就删除
        if (!file.exists()) {
            // 创建文件
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 初始化创建文件测试
        File file2 = new File(getExternalFilesDir(null).getAbsolutePath() + "/" + "test.txt");
        // 判断文件是否存在，存在就删除
        if (!file2.exists()) {
            // 创建文件
            try {
                file2.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            File file = UriUtils.uriToFile(getApplicationContext(), uri);
            if (UriUtils.uriToFile(getApplicationContext(), uri) != null) {
                ((TextView) findViewById(R.id.tvContent)).setText(file.getPath());
            }

//            // 这是测试是否可操作文件，修改文件名字
//            FixFileName(file.getPath(),"new" + file.getName());
        }
    }


    /**
     * 通过文件路径直接修改文件名
     *
     * @param filePath    需要修改的文件的完整路径
     * @param newFileName 需要修改的文件的名称
     * @return
     */
    private String FixFileName(String filePath, String newFileName) {
        File f = new File(filePath);
        // 判断原文件是否存在（防止文件名冲突）
        if (!f.exists()) {
            return null;
        }
        newFileName = newFileName.trim();
        // 文件名不能为空
        if ("".equals(newFileName) || newFileName == null) {
            return null;
        }
        String newFilePath;
        // 判断是否为文件夹
        if (f.isDirectory()) {
            newFilePath = filePath.substring(0, filePath.lastIndexOf("/")) + "/" + newFileName;
        } else {
            newFilePath = filePath.substring(0, filePath.lastIndexOf("/")) + "/" + newFileName
                    + filePath.substring(filePath.lastIndexOf("."));
        }
        File nf = new File(newFilePath);
        try {
            // 修改文件名
            f.renameTo(nf);
        } catch (Exception err) {
            err.printStackTrace();
            return null;
        }
        return newFilePath;
    }
}