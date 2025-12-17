package dojo.supermarket.model.specialOffer.types;

import dojo.supermarket.model.loyalty.LoyaltyCard;
import dojo.supermarket.model.product.Product;
import dojo.supermarket.model.product.ProductUnit;
import dojo.supermarket.model.specialOffer.Discount;

public class LoyaltyRedemptionStrategy implements SpecialOfferCalculationStrategy {

    public Discount calculateRedemption(LoyaltyCard card, double amountToPay) {
        double pointsAvailable = card.getPointsBalance();

        double creditValue = pointsAvailable / 10.0;

        double appliedCredit = Math.min(creditValue, amountToPay);
        double pointsToDeduct = Math.ceil(appliedCredit * 10.0);
        Product loyaltyPlaceholder = new Product("Loyalty Points", ProductUnit.EACH);
        card.redeemPoints(pointsToDeduct);
        return new Discount(loyaltyPlaceholder, "Redemption", -appliedCredit);
    }
    @Override
    public Discount calculateDiscount(Product product, double quantity, double unitPrice, double offerArgument) {
        return null;
    }
}
