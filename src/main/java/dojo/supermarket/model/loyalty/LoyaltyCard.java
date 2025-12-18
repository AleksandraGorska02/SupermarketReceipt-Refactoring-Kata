package dojo.supermarket.model.loyalty;

public class LoyaltyCard {
    private final String accountId;
    private double pointsBalance;

    public LoyaltyCard(String accountId) {
        this.accountId = accountId;
        this.pointsBalance = 0.0;
    }

    public String getAccountId() {
        return accountId;
    }

    public void addPoints(double points) {
        this.pointsBalance += points;
    }

    public void redeemPoints(double points) {
        if (points <= pointsBalance) {
            this.pointsBalance -= points;
        }
    }

    public double getPointsBalance() {
        return pointsBalance;
    }
}
