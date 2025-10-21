package brain.factions.servers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CollectionGoal {
    private String name;
    private long currentAmount;
    private long targetAmount;
    private List<Transaction> history;

    public CollectionGoal(String name, long targetAmount, long currentAmount) {
        this.name = name;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.history = new ArrayList<>();
    }

    public CollectionGoal(String name, long targetAmount) {
        this(name, targetAmount, 0);
    }

    public String getName() {
        return name;
    }

    public long getCurrentAmount() {
        return currentAmount;
    }

    public long getTargetAmount() {
        return targetAmount;
    }

    public List<Transaction> getHistory() {
        return history;
    }

    public void addAmount(long amount) {
        this.currentAmount += amount;
    }

    public void logTransaction(String playerName, long amount) {
        if (this.history == null) {
            this.history = new ArrayList<>();
        }
        this.history.add(new Transaction(playerName, amount, System.currentTimeMillis()));
    }


    public boolean isComplete() {
        return this.currentAmount >= this.targetAmount;
    }

    public static class Transaction {
        private final String playerName;
        private final long amount;
        private final long timestamp;

        public Transaction(String playerName, long amount, long timestamp) {
            this.playerName = playerName;
            this.amount = amount;
            this.timestamp = timestamp;
        }

        public String getPlayerName() {
            return playerName;
        }

        public long getAmount() {
            return amount;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public String getFormattedDate() {
            return new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date(timestamp));
        }
    }
}