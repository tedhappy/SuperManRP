package why.superman.redpacket.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public final class BitmapUtils {

    private BitmapUtils() {}

    public static boolean saveBitmap(Context context, File output, Bitmap bitmap) {
        if(output.exists()) {
            return false;
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(output);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            insertMedia(context, output, "image/jpeg");
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } finally {
            if(fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {}

            }
        }


    }

    /** 加入到系统的图库中*/
    private static void insertMedia(Context context, File output, String mime) {
        try {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Video.Media.DATA, output.getAbsolutePath());
            values.put(MediaStore.Video.Media.MIME_TYPE, mime);
            //记录到系统媒体数据库，通过系统的gallery可以即时查看
            context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
            //通知系统去扫描
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(output)));
        } catch (Exception e){}
    }

    public static File saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "superman");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
//        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
        return file;
    }
}
