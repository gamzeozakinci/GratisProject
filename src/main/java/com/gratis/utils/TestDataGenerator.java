package com.gratis.utils;

import java.util.Random;

public final class TestDataGenerator {

    private static final Random RANDOM = new Random();

    private TestDataGenerator() {
    }

    public static String uniquePhoneNumber() {
        int operatorPrefix = 30 + RANDOM.nextInt(30);
        StringBuilder subscriberNumber = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            subscriberNumber.append(RANDOM.nextInt(10));
        }
        return "5" + operatorPrefix + subscriberNumber;
    }
}
