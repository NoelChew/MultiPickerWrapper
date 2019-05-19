package com.noelchew.multipickerwrapper.demo.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.webkit.MimeTypeMap;

import java.io.File;

public class FileIntentUtil {

    // source: https://stackoverflow.com/a/6381479/5652775
    public static void openFile(Context context, String filePath) {
        final Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".multipicker.fileprovider", new File(filePath));
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);

        MimeTypeMap myMime = MimeTypeMap.getSingleton();
        String mimeType = myMime.getMimeTypeFromExtension(fileExt(filePath));
        intent.setDataAndType(uri, mimeType);

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    private static String fileExt(String url) {
        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf(".") + 1);
            if (ext.indexOf("%") > -1) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.indexOf("/") > -1) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();

        }
    }

}
