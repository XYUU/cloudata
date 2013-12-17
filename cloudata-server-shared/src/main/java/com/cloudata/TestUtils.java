package com.cloudata;

import java.io.File;

public class TestUtils {
    public static void rmdir(File dir) {
        for (File f : dir.listFiles()) {
            if (f.isFile()) {
                f.delete();
            } else {
                rmdir(f);
            }
        }

        dir.delete();
    }

    public static byte[] buildBytes(int length) {
        byte[] data = new byte[length];
        for (int j = 0; j < length; j++) {
            data[j] = (byte) (j % 0xff);
        }
        return data;
    }

}