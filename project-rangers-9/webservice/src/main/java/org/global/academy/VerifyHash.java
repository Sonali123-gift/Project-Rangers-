package org.global.academy;

import java.security.MessageDigest;
import java.util.Scanner;

public class VerifyHash {
    public static void main(String[] args) {
        final String storedHash = "2bb80d537b1da3e38bd30361aa855686bde0eacd7162fef6a25fe97bf527a25b";
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your input: ");
        String input = scanner.nextLine();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(input.getBytes());

            StringBuilder sb = new StringBuilder(); //hex
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            String computedHash = sb.toString();
            if (computedHash.equalsIgnoreCase(storedHash)) {
                System.out.println("✅ Match! The hash is correct.");
            } else {
                System.out.println("❌ No match. The hash is incorrect.");
            }

        } catch (Exception e) {
            System.out.println("Error computing hash: " + e.getMessage());
        }

        scanner.close();
    }
}

