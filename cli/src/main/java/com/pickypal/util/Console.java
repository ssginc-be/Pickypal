package com.pickypal.util;

import java.io.IOException;

/**
 * @author Queue-ri
 */

public class Console {
    public static void clear() throws IOException {
        for (int i = 0; i < 40; ++i) {
            System.out.println();
        }
    }
}
