package dojo.supermarket.model.loyalty;

import dojo.supermarket.model.SupermarketConfig;
import dojo.supermarket.model.product.Product;
import dojo.supermarket.model.specialOffer.Discount;
import dojo.supermarket.model.specialOffer.types.SpecialOfferCalculationStrategy;

public class LoyaltyRedemptionStrategy  {

    public Discount calculateRedemption(LoyaltyCard card, double amountToPay, Product representative) {
        double ratio = SupermarketConfig.getProperty("loyalty.redemption.ratio", 10.0);
        double pointsAvailable = card.getPointsBalance();
        double maxCreditValue = pointsAvailable / ratio;


        double appliedCredit = Math.min(maxCreditValue, amountToPay);

       double pointsToDeduct = Math.ceil(appliedCredit * ratio);

        card.redeemPoints(pointsToDeduct);

        return new Discount(representative, "Loyalty Points Redemption", -appliedCredit);
    }

}
