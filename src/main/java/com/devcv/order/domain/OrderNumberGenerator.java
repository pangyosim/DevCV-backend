package com.devcv.order.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class OrderNumberGenerator {
    private static final String DATETIME_FORMAT = "YYMMdd";
    private static final int RANDOM_NUMBER_LENGTH = 6;

    public static String generateOrderNumber() {
        String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATETIME_FORMAT));
        String randomNumber = generateRandomNumber();
        return datetime + "-" + randomNumber;
    }

    private static String generateRandomNumber() {
        StringBuilder randomNumber = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < RANDOM_NUMBER_LENGTH; i++) {
            int digit = random.nextInt(10);
            randomNumber.append(digit);
        }
        return randomNumber.toString();
    }
}