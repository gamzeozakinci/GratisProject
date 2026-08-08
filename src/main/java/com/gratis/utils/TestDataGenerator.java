package com.gratis.utils;

import java.util.Random;

/**
 * Generates unique-per-run test data so tests like TC_001 (registration) are
 * re-runnable without manual DB cleanup.
 */
public final class TestDataGenerator {

    private static final Random RANDOM = new Random();

    private TestDataGenerator() {
    }

    /**
     * A syntactically valid, freshly-random Turkish mobile number (5XX XXX XX XX,
     * no leading 0/country code - matches the "0(5 )" phone field on gratis.com).
     * Not guaranteed to be deliverable/receive a real OTP; swap for a provisioned
     * test-SIM number if the target environment requires a real SMS round-trip.
     */
    public static String uniquePhoneNumber() {
        int operatorPrefix = 30 + RANDOM.nextInt(30); // 5(30-59)... covers most TR mobile ranges
        StringBuilder subscriberNumber = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            subscriberNumber.append(RANDOM.nextInt(10));
        }
        return "5" + operatorPrefix + subscriberNumber; // 10 digits total: 5XX XXX XX XX
    }
}
