package com.gratis.utils;

import java.time.Instant;
import java.util.Random;

/**
 * Generates unique-per-run test data so tests like TC_001 (registration) are
 * re-runnable without manual DB cleanup. Mirrors the placeholder conventions in
 * the source test case doc: "{random_timestamp}", "{random_7_digits}".
 */
public final class TestDataGenerator {

    private static final Random RANDOM = new Random();

    private TestDataGenerator() {
    }

    /** e.g. qa_gratis_test_1723027200000@mailinator.com (TC_001) */
    public static String uniqueEmail() {
        return "qa_gratis_test_" + Instant.now().toEpochMilli() + "@mailinator.com";
    }

    /** e.g. 555XXXXXXX - valid Turkish mobile prefix + 7 random digits (TC_001) */
    public static String turkishMobile(String prefix) {
        StringBuilder sb = new StringBuilder(prefix);
        for (int i = 0; i < 7; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }

    /** Meets: 1 upper, 1 lower, 1 digit, 1 special char, min 8 chars (TC_001 rule) */
    public static String validPassword() {
        return "GratisTest" + (2000 + RANDOM.nextInt(100)) + "!";
    }
}
