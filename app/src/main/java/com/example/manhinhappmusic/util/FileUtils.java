package com.example.manhinhappmusic.util;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {
    public static File getFileFromUri(Context context, Uri uri) {
        File file = null;
        try (InputStream inputStream = context.getContentResolver().openInputStream(uri)) {
            String fileName = "temp_" + System.currentTimeMillis() + ".jpg";
            File tempFile = new File(context.getCacheDir(), fileName);
            try (OutputStream outputStream = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[4096];
                int read;
                while ((read = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, read);
                }
            }
            file = tempFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}