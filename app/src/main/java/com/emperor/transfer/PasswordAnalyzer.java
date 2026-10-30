package com.emperor.transfer;

public class PasswordAnalyzer {

    public static class Result {
        public int score;
        public String level;
        public String advice;

        public Result(int score, String level, String advice) {
            this.score = score;
            this.level = level;
            this.advice = advice;
        }
    }

    public static Result analyze(String password) {

        if (password == null || password.isEmpty()) {
            return new Result(
                    0,
                    "EMPTY",
                    "Enter a password to analyze."
            );
        }

        int score = 0;

        if (password.length() >= 8) score += 20;
        if (password.length() >= 12) score += 20;
        if (password.length() >= 16) score += 10;

        if (password.matches(".*[a-z].*")) score += 10;
        if (password.matches(".*[A-Z].*")) score += 10;
        if (password.matches(".*[0-9].*")) score += 10;
        if (password.matches(".*[^a-zA-Z0-9].*")) score += 20;

        if (password.matches("(?i).*(123456|password|qwerty|admin).*")) {
            score = Math.min(score, 20);
        }

        String level;
        String advice;

        if (score < 30) {
            level = "WEAK";
            advice = "Use a longer password with mixed characters.";
        } else if (score < 60) {
            level = "MEDIUM";
            advice = "Add more character types and length.";
        } else if (score < 80) {
            level = "STRONG";
            advice = "Good password structure.";
        } else {
            level = "VERY STRONG";
            advice = "Good structure. Avoid reusing this password.";
        }

        return new Result(score, level, advice);
    }
}
