package com.bbs.teajava.utils;

import java.util.Random;

/**
 * @author kunhuang
 */
public class RandomUtil {
    private static Random random;

    public static Random getRandom() {
        if (random == null) {
            random = new Random();
        }
        return random;
    }
}
