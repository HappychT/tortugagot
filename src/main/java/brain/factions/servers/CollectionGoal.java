package brain.factions.servers;

public class CollectionGoal {
    private String name;
    private long currentAmount;
    private long targetAmount;

    public CollectionGoal(String name, long targetAmount, long currentAmount) {
        this.name = name;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
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

    public void addAmount(long amount) {
        this.currentAmount += amount;
    }

    public boolean isComplete() {
        return this.currentAmount >= this.targetAmount;
    }
}