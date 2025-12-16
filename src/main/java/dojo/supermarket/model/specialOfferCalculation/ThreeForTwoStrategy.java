package dojo.supermarket.model.specialOfferCalculation;

import dojo.supermarket.model.Discount;
import dojo.supermarket.model.Offer;
import dojo.supermarket.model.Product;

public class ThreeForTwoStrategy implements SpecialOfferCalculationStrategy {

    @Override
    public Discount calculateDiscount(Product product, double quantity, double unitPrice, double offerArgument) {
        int quantityAsInt = (int) quantity;
        if (quantityAsInt >= 3) {
            int numberOfXs = quantityAsInt / 3;
            double discountAmount = quantity * unitPrice - ((numberOfXs * 2 * unitPrice) + quantityAsInt % 3 * unitPrice);
            return new Discount(product, "3 for 2", -discountAmount);
        }
        return null;
    }


}