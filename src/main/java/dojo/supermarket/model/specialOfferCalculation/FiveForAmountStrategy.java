package dojo.supermarket.model.specialOfferCalculation;

import dojo.supermarket.model.Discount;
import dojo.supermarket.model.Offer;
import dojo.supermarket.model.Product;

public class FiveForAmountStrategy implements SpecialOfferCalculationStrategy {

    @Override
    public Discount calculateDiscount(Product product, double quantity, double unitPrice, Offer offer) {
        int quantityAsInt = (int) quantity;
        if (quantityAsInt >= 5) {
            int numberOfXs = quantityAsInt / 5;
            double discountTotal = unitPrice * quantity - (offer.argument * numberOfXs + quantityAsInt % 5 * unitPrice);
            return new Discount(product, 5 + " for " + offer.argument, -discountTotal);
        }
        return null;
    }


}