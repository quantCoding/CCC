import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class Snack_Vendor {

    public static void main(String[] args) {

        if (args.length == 0)  {
            System.out.println("The program requires the following arguments { level(1-6) level input }");
            System.exit(-1);
        }
        int level = Integer.parseInt(args[0]);

        print(cut(args, 1));

        switch (level) {
            case 1 -> System.out.println("\n" + getLevel1(cut(args, 1)));
            case 2 -> System.out.println("\n" + getLevel2(cut(args, 1)));
            case 3 -> System.out.println("\n" + getLevel3(cut(args, 1)));
            case 4 -> System.out.println("\n" + getLevel4(cut(args, 1)));
            case 5 -> System.out.println("\n" + getLevel5(cut(args, 1)));
            case 6 -> System.out.println("\n" + getLevel6(cut(args, 1)));
        }
    }

    private static String[] cut(String[] args, int n) {
        String[] ret = new String[args.length - n];
        for (int i = n; i < args.length; i++) ret[i - n] = args[i];
        return ret;
    }

    private static void print(String[] args) {
        for (String arg : args) System.out.print(arg + " ");
    }

    private static String getLevel1(String[] args) {
        int remainingPrice = Integer.parseInt(args[0]);
        for (int i = 2; i < args.length; i++) remainingPrice -= Integer.parseInt(args[i]);
        return remainingPrice <= 0 ? "CHANGE " + remainingPrice * -1 : "MISSING " + remainingPrice;
    }

    private static String getLevel2(String[] args) {
        int remainingPrice = Integer.parseInt(args[0]);
        for (int i = 2; i < args.length; i++) remainingPrice -= Integer.parseInt(args[i]);

        return formatRemainingPrice(remainingPrice);
    }

    private static String getLevel3(String[] args) {
        Integer steps = null;
        Integer money = null;
        for (String arg : args) {
            if (arg.equals(args[0])) continue;
            if (arg.matches("[A-Z][0-9]+")) {
                steps = (arg.charAt(0) - 'A') * Integer.parseInt(args[0].substring(1)) + Integer.parseInt(arg.substring(1));
                continue;
            }
            if (steps == null) continue;
            if (money == null) {
                money = 0;
                continue;
            }

            money += Integer.parseInt(arg);
        }

        int price = Integer.parseInt(args[steps]);
        return formatRemainingPrice(price - money);
    }

    private static String formatRemainingPrice(int remainingPrice) {
        if (remainingPrice > 0) return "MISSING " + remainingPrice;
        remainingPrice *= -1;

        int[] coins = new int[]{200, 100, 50, 20, 10, 5, 2, 1};
        String ret = "";
        for (int coin : coins) {
            int counterCoin = 0;
            while (remainingPrice >= coin) {
                remainingPrice -= coin;
                counterCoin++;
            }
            ret = counterCoin + " " + ret;
        }
        return "CHANGE " + ret;
    }

    private static String getLevel4(String[] args) {

        int size = (args[0].charAt(0) - 'A' + 1) * Integer.parseInt(args[0].substring(1));
        int[] prices = new int[size];
        int[] amount = new int[size];
        for (int i = 1; i < size + 1; i++) {
            prices[i - 1] = Integer.parseInt(args[i]);
            amount[i - 1] = Integer.parseInt(args[i + size]);
        }

        int price = 0;
        for (int i = size * 2 + 2; i < args.length; i++) {
            String currentOrder = args[i];

            int steps = (int) (currentOrder.charAt(0) - 'A') * Integer.parseInt(args[0].substring(1)) + Integer.parseInt(currentOrder.substring(1));
            steps = steps - 1;                  // The array starts at index 0 and so we must correct the steps which are always a step ahead

            if (amount[steps] <= 0)
                continue;   // If the storage for this specific item is to be empty continue. Because in that case
            // you cannot take something out of a empty storage.
            amount[steps]--;
            price += prices[steps];
        }

        return price + "";
    }

    private static String getLevel5(String[] args) {

        HashSet<Integer> visited = new HashSet<>();

        Position currentPos = new Position(args[1].charAt(0) - 'A', Integer.parseInt(args[1].substring(1)), 0);
        Position desiredPos = new Position(args[2].charAt(0) - 'A', Integer.parseInt(args[2].substring(1)), 0);

        Queue<Position> queue = new LinkedList<>();
        queue.add(currentPos);

        Position current = null;
        while (!queue.isEmpty() && !(current = queue.poll()).equals(desiredPos)) {

            if (current.getCol() < 0 || current.getCol() >= Integer.parseInt(args[0].substring(1))) continue;
            if (current.getRow() < 0 || current.getRow() >= Integer.parseInt(args[0].charAt(0) - 'A' + "") + 1)
                continue;

            if (visited.contains(current.getRow() * 1000 + current.getCol())) continue;
            visited.add(current.getRow() * 1000 + current.getCol());

            queue.add(new Position(current.getRow(), current.getCol() + 1, current.getTime() + 1));
            queue.add(new Position(current.getRow(), current.getCol() - 1, current.getTime() + 1));

            queue.add(new Position(current.getRow() + 1, current.getCol(), current.getTime() + 1));
            queue.add(new Position(current.getRow() - 1, current.getCol(), current.getTime() + 1));

            queue.add(new Position(current.getRow() + 1, current.getCol() + 1, current.getTime() + 1));
            queue.add(new Position(current.getRow() - 1, current.getCol() - 1, current.getTime() + 1));

            queue.add(new Position(current.getRow() + 1, current.getCol() - 1, current.getTime() + 1));
            queue.add(new Position(current.getRow() - 1, current.getCol() + 1, current.getTime() + 1));
        }

        return current.getTime() + "";
    }

    private static String getLevel6(String[] args) {

        HashSet<Integer> visited = new HashSet<>();

        Position currentPos = new Position(args[1].charAt(0) - 'A', Integer.parseInt(args[1].substring(1)), 0);
        Position desiredPos = new Position(args[2].charAt(0) - 'A', Integer.parseInt(args[2].substring(1)), 0);
        int directionRestriction = Integer.parseInt(args[3]);

        Queue<Position> queue = new LinkedList<>();
        queue.add(currentPos);

        Position current = null;
        while (!queue.isEmpty() && !(current = queue.poll()).equals(desiredPos)) {

            if (current.getCol() < 0 || current.getCol() >= Integer.parseInt(args[0].substring(1))) continue;
            if (current.getRow() < 0 || current.getRow() >= Integer.parseInt(args[0].charAt(0) - 'A' + "") + 1)
                continue;

            if (visited.contains(current.getRow() * 1000 + current.getCol())) continue;
            visited.add(current.getRow() * 1000 + current.getCol());

            if (directionRestriction != 0)
                queue.add(new Position(current.getRow(), current.getCol() + 1, current.getTime() + 1));
            if (directionRestriction != 4)
                queue.add(new Position(current.getRow(), current.getCol() - 1, current.getTime() + 1));

            if (directionRestriction != 6)
                queue.add(new Position(current.getRow() + 1, current.getCol(), current.getTime() + 1));
            if (directionRestriction != 2)
                queue.add(new Position(current.getRow() - 1, current.getCol(), current.getTime() + 1));

            if (directionRestriction != 7)
                queue.add(new Position(current.getRow() + 1, current.getCol() + 1, current.getTime() + 1));
            if (directionRestriction != 3)
                queue.add(new Position(current.getRow() - 1, current.getCol() - 1, current.getTime() + 1));

            if (directionRestriction != 5)
                queue.add(new Position(current.getRow() + 1, current.getCol() - 1, current.getTime() + 1));
            if (directionRestriction != 1)
                queue.add(new Position(current.getRow() - 1, current.getCol() + 1, current.getTime() + 1));
        }

        return current.getTime() + "";
    }

    static class Position {
        private int row;
        private int col;
        private int time;

        public Position(int row, int col, int time) {
            this.row = row;
            this.col = col;
            this.time = time;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        public int getTime() {
            return time;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return row == position.row && col == position.col;
        }
    }
}
