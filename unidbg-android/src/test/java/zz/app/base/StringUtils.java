package zz.app.base;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

public class StringUtils {

    // bytes 转 base64
    public static String bytesToBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    // base64 转 bytes
    public static byte[] base64ToBytes(String base64) {
        return Base64.getDecoder().decode(base64);
    }


    // bytes 转字符串
    public static String bytesToString(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    // 字符串转 bytes
    public static byte[] stringToBytes(String str) {
        return str.getBytes(StandardCharsets.UTF_8);
    }


    // hex 字符串转字符串
    public static String hexToString(String hexStr) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hexStr.length(); i+=2) {
            String str = hexStr.substring(i, i+2);
            output.append((char)Integer.parseInt(str, 16));
        }
        return output.toString();
    }

    // 字符串转 hex
    public static String stringToHex(String str) {
        StringBuilder hex = new StringBuilder();
        for (char ch : str.toCharArray()) {
            int intValue = (int) ch;
            hex.append(String.format("%02x", intValue));
        }
        return hex.toString();
    }

    // hex 字符串转 bytes
    public static byte[] hexToBytes(String hexStr) {
        byte[] val = new byte[hexStr.length() / 2];
        for (int i = 0; i < val.length; i++) {
            int index = i * 2;
            int j = Integer.parseInt(hexStr.substring(index, index + 2), 16);
            val[i] = (byte) j;
        }
        return val;
    }

    // bytes 转 hex 字符串
    public static String bytesToHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }



    public static void main(String[] args) {
        String text = "hello world";

        String hexStr = StringUtils.stringToHex(text);
        byte[] bytes = StringUtils.stringToBytes(text);
        String base64 = bytesToBase64(bytes);
        System.out.println(base64);
        System.out.println(hexStr);
        System.out.println(Arrays.toString(bytes));

        System.out.println("----------------------------");

        String cc = StringUtils.bytesToString(bytes);
        System.out.println(cc);

        String cc1 = StringUtils.hexToString(hexStr);
        System.out.println(cc1);

        byte[] bb = base64ToBytes(base64);
        System.out.println(Arrays.toString(bytes));

    }
}