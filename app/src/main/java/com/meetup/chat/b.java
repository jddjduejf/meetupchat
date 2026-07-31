package com.meetup.chat;

import android.util.Base64;

public class b {
    private static final byte a = 0x5A;
    public static String a(String b) {
        try {
            byte[] c = Base64.decode(b, Base64.DEFAULT);
            for (int d = 0; d < c.length; d++) {
                c[d] ^= a;
            }
            return new String(c);
        } catch (Exception e) {
            return b;
        }
    }
}
