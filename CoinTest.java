import java.util.Set;
import java.util.List;
import java.util.stream.Stream;

public class CoinTest extends TestSuite {
    public static void main(String[] args) {
        TestSuite.run(new CoinTest());
    }

    // Tests are the same for bottom up and top down
    public List<Test> tests(String group, CoinChanger changer) {
        return List.of(
                new Test(group + "Zero or negative amount",
                        () -> expectThrows(() -> changer.minCoins(0, Set.of(8, 1)), IllegalArgumentException.class,
                                "Amount must be at least 1")),
                new Test(group + "No denominations",
                        () -> expectThrows(() -> changer.minCoins(100, Set.of()), IllegalArgumentException.class,
                                "At least one denomination is required")),
                new Test(group + "Zero or negative denominations",
                        () -> expectThrows(() -> changer.minCoins(100, Set.of(9, 2, -2, 1)),
                                IllegalArgumentException.class, "Denominations must all be positive")),
                new Test(group + "No one unit denomination",
                        () -> expectThrows(() -> changer.minCoins(100, Set.of(3, 5, 7)), IllegalArgumentException.class,
                                "Denominations must have a 1-unit coin")),
                new Test(group + "8 from 1 4 5 is 2", () -> {
                    expectEqual(changer.minCoins(8, Set.of(1, 4, 5)), 2);
                }), new Test(group + "159 from 30 8 1 31 is 6", () -> {
                    expectEqual(changer.minCoins(159, Set.of(30, 8, 1, 31)), 6);
                }), new Test(group + "50 from 1 should be 50", () -> {
                    expectEqual(changer.minCoins(50, Set.of(1)), 50);
                }), new Test(group + "3115 from 80 82 49 47 139 3 1 2 should be 25", () -> {
                    expectEqual(changer.minCoins(3115, Set.of(80, 82, 49, 47, 139, 3, 1, 2)), 25);
                }));
    }

    @Override
    public Test[] getTests() {
        var topDownTests = tests("Top down: ", new CoinChanger.TopDown()).stream();
        var bottomUpTests = tests("Bottom up: ", new CoinChanger.BottomUp()).stream();
        return Stream.concat(topDownTests, bottomUpTests).toArray(Test[]::new);
    }
}
