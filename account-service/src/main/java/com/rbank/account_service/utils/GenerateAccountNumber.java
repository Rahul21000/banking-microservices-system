package com.rbank.account_service.utils;

import java.util.Random;


public class GenerateAccountNumber {
    public static Long handleGenerateUniqueAccountNumber() {
        Random random = new Random();
        int firstDigit = random.nextInt(9) + 1;
        StringBuilder accountNumber = new StringBuilder();
        accountNumber.append(firstDigit);
        for (int i = 1; i < 10; i++) {
            accountNumber.append(random.nextInt(10));
        }
        return Long.parseLong(accountNumber.toString());
    }
}
