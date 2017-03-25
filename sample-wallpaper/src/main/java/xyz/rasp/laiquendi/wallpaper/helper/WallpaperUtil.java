package xyz.rasp.laiquendi.wallpaper.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.ByteArrayOutputStream;

/**
 * Created by twiceYuan on 2017/3/25.
 * <p>
 * 壁纸设置工具
 */
public class WallpaperUtil {

    public static void set(Activity context, String url) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("下载中");
        progressDialog.show();
        Glide.with(context).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                progressDialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setDataAndType(getImageUri(context, resource, Uri.parse(url).getLastPathSegment()), "image/jpeg");
                intent.putExtra("mimeType", "image/jpeg");
                context.startActivity(Intent.createChooser(intent, "设为壁纸"));
            }
        });
    }

    private static Uri getImageUri(Context inContext, Bitmap bitmap, String name) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), bitmap, name, null);
        return Uri.parse(path);
    }
}
