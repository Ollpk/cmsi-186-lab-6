import java.util.*;

public abstract class CoinChanger {
    abstract public int minCoins(int amount, Set<Integer> denominations);

    static HashMap<Set<Integer>, Map<Integer, Integer>> memo = new HashMap<>();

    protected static void checkArguments(int amount, Set<Integer> denominations) {
        List<Integer> denominationList = new ArrayList<>(denominations);
        if (amount < 1) {
            throw new IllegalArgumentException("Amount must be at least 1");
        }
        if (denominationList.size() < 1) {
            throw new IllegalArgumentException("At least one denomination is required");

        }
        if (!denominationList.contains(1)) {
            throw new IllegalArgumentException("Denominations must have a 1-unit coin");
        }
        for (int i = 0; i < denominationList.size(); i++) {
            if (denominationList.get(i) < 1) {
                throw new IllegalArgumentException("Denominations must all be positive");
            }
        }

    }

    public static class TopDown extends CoinChanger {
        public int minCoins(int amount, Set<Integer> denominations) {
            checkArguments(amount, denominations);
            List<Integer> denominationlist = new ArrayList<>(denominations);
            Collections.sort(denominationlist);
            return helper(amount, denominationlist);

        }

        private static int helper(int amount, List<Integer> denominations) {

            // Gets the min # of coins needed to get amount for a denomination

            if (amount == 0) {
                return 0;
            }

            // Has this value been cached?
            Integer storageValue = memo
                    .getOrDefault(new HashSet<Integer>(denominations), new HashMap<Integer, Integer>()).get(amount);

            if (storageValue != null) {
                return storageValue;
            }

            // Gives us the answer for min coins.
            int result = -1;

            // This process gives us the min. amount of coins per denomination.
            for (int i = 0; i < denominations.size(); i++) {
                if (amount / denominations.get(i) < 1) {
                    continue;
                }

                int remainder = amount - denominations.get(i);
                int thisResult = 1 + helper(remainder, denominations);
                if ((thisResult < result) || result == -1) {
                    result = thisResult;
                }
            }

            // Stores the denominations and answers
            Map<Integer, Integer> memory = memo.getOrDefault(new HashSet<Integer>(denominations),
                    new HashMap<Integer, Integer>());
            memory.put(amount, result);
            memo.put(new HashSet<Integer>(denominations), memory);
            return result;
        }
    }

    public static class BottomUp extends CoinChanger {
        public int minCoins(int amount, Set<Integer> denominations) {
            checkArguments(amount, denominations);
            List<Integer> denominationList = new ArrayList<>(denominations);
            Collections.sort(denominationList, Collections.reverseOrder());
            return helper(amount, denominationList, denominationList.size() - 1);

        }

        private static int helper(int amount, List<Integer> denominations, int coinIndex) {
            // Gets the min # of coins needed to get amount for a denomination

            // Has this value been cached?
            Integer storageValue = memo
                    .getOrDefault(new HashSet<Integer>(denominations), new HashMap<Integer, Integer>()).get(amount);
            if (storageValue != null) {
                return storageValue;
            }

            // This process gives us the min. amount of coins per denomination.
            int coinNumber = amount / denominations.get(coinIndex);
            int remainder = amount % denominations.get(coinIndex);

            // Gives us the answer for min coins.
            int result = 0;

            // Checks if index that is checked in set is about to be out of bounds.
            if (coinIndex == 0) {
                result = coinNumber;
            } else {
                result = coinNumber + helper(remainder, denominations, coinIndex - 1);
            }

            // Stores the denominations and answers
            Map<Integer, Integer> memory = memo.getOrDefault(new HashSet<Integer>(denominations),
                    new HashMap<Integer, Integer>());
            memory.put(amount, result);
            memo.put(new HashSet<Integer>(denominations), memory);
            return result;
        }

    }
}
