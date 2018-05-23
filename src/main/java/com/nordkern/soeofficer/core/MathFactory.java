package com.nordkern.soeofficer.core;

import java.util.Arrays;

/**
 * Created by mortenfrank on 18/12/2017.
 */
public class MathFactory {

    public static class Levenshtein {

        public static String getMatchWithShortestDistance(String word, String[] set) throws NoMatchException {
            String match = word;
            int smallest = Integer.MAX_VALUE;
            for (int i = 0; i < set.length; i++) {
                int distance;
                if ((distance = calculate(word, set[i])) < smallest) {
                    smallest = distance;
                    match = set[i];
                }
            }

            if (match.length() < Math.floorDiv(smallest,2)) {
                throw new NoMatchException();
            }

            return match;
        }

        private static int calculate(String x, String y) {
            int[][] dp = new int[x.length() + 1][y.length() + 1];

            for (int i = 0; i <= x.length(); i++) {
                for (int j = 0; j <= y.length(); j++) {
                    if (i == 0) {
                        dp[i][j] = j;
                    }
                    else if (j == 0) {
                        dp[i][j] = i;
                    }
                    else {
                        dp[i][j] = min(dp[i - 1][j - 1]
                                        + costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)),
                                dp[i - 1][j] + 1,
                                dp[i][j - 1] + 1);
                    }
                }
            }

            return dp[x.length()][y.length()];
        }

        private static int costOfSubstitution(char a, char b) {
            return a == b ? 0 : 1;
        }

        private static int min(int... numbers) {
            return Arrays.stream(numbers)
                    .min().orElse(Integer.MAX_VALUE);
        }
    }
}
