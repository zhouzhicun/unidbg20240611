package zz.app.bili;

import zz.app.bili.code.ContainerUtils;
import zz.app.bili.code.TextUtils;

import java.io.UnsupportedEncodingException;
import java.util.*;

public class SignedQuery {

    private static final char[] f14892c = "0123456789ABCDEF".toCharArray();

    public final String a;
    public final String b;

    public SignedQuery(String str, String str2) {
        this.a = str;
        this.b = str2;
    }

    public String toString() {
        String str = this.a;
        if (str == null) {
            return "";
        }
        if (this.b == null) {
            return str;
        }
        return this.a + "&sign=" + this.b;
    }



    static String r(Map<String, String> map) {

        if (!(map instanceof SortedMap)) {
            map = new TreeMap(map);
        }
        StringBuilder sb = new StringBuilder(256);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            if (!TextUtils.isEmpty(key)) {
                sb.append(b(key));
                sb.append(ContainerUtils.KEY_VALUE_DELIMITER);
                String value = entry.getValue();
                sb.append(value == null ? "" : b(value));
                sb.append(ContainerUtils.FIELD_DELIMITER);
            }
        }
        int length = sb.length();
        if (length > 0) {
            sb.deleteCharAt(length - 1);
        }
        if (length == 0) {
            return null;
        }
        return sb.toString();
    }

    private static boolean a(char c2, String str) {
        return (c2 >= 'A' && c2 <= 'Z') || (c2 >= 'a' && c2 <= 'z') || !((c2 < '0' || c2 > '9') && "-_.~".indexOf(c2) == -1 && (str == null || str.indexOf(c2) == -1));
    }

    static String b(String str) {
        return c(str, null);
    }

    static String c(String str, String str2) {
        StringBuilder sb = null;
        if (str == null) {
            return null;
        }
        int length = str.length();
        int i2 = 0;
        while (i2 < length) {
            int i3 = i2;
            while (i3 < length && a(str.charAt(i3), str2)) {
                i3++;
            }
            if (i3 == length) {
                if (i2 == 0) {
                    return str;
                }
                sb.append((CharSequence) str, i2, length);
                return sb.toString();
            }
            if (sb == null) {
                sb = new StringBuilder();
            }
            if (i3 > i2) {
                sb.append((CharSequence) str, i2, i3);
            }
            i2 = i3 + 1;
            while (i2 < length && !a(str.charAt(i2), str2)) {
                i2++;
            }
            try {
                byte[] bytes = str.substring(i3, i2).getBytes("UTF-8");
                int length2 = bytes.length;
                for (int i4 = 0; i4 < length2; i4++) {
                    sb.append('%');
                    sb.append(f14892c[(bytes[i4] & 240) >> 4]);
                    sb.append(f14892c[bytes[i4] & cv.m]);
                }
            } catch (UnsupportedEncodingException e) {
                throw new AssertionError(e);
            }
        }
        return sb == null ? str : sb.toString();
    }




    public final class cv {
        public static final byte a = 0;
        public static final byte b = 1;

        /* renamed from: c, reason: collision with root package name */
        public static final byte f23635c = 2;
        public static final byte d = 3;
        public static final byte e = 4;
        public static final byte f = 6;
        public static final byte g = 8;
        public static final byte h = 10;

        /* renamed from: i, reason: collision with root package name */
        public static final byte f23636i = 11;
        public static final byte j = 12;
        public static final byte k = 13;

        /* renamed from: l, reason: collision with root package name */
        public static final byte f23637l = 14;
        public static final byte m = 15;
        public static final byte n = 16;
    }



}
