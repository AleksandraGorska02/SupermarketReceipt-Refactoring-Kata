package dojo.supermarket.model.loyalty;

import dojo.supermarket.model.SupermarketConfig;
import dojo.supermarket.model.discount.Discount;

public class LoyaltyRedemptionStrategy {

    public Discount calculateRedemption(LoyaltyCard card, double amountToPay) {
        double ratio = SupermarketConfig.getProperty("loyalty.redemption.ratio", 10.0);
        double pointsAvailable = card.getPointsBalance();
        double maxCreditValue = pointsAvailable / ratio;


        double appliedCredit = Math.min(maxCreditValue, amountToPay);

        double pointsToDeduct = Math.ceil(appliedCredit * ratio);

        card.redeemPoints(pointsToDeduct);

        return new Discount(null, "Redemption(Loyalty Points)", -appliedCredit);
    }

}
