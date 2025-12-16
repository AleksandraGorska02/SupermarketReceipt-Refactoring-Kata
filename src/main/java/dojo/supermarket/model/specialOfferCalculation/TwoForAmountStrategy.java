package dojo.supermarket.model.specialOfferCalculation;

import dojo.supermarket.model.Discount;
import dojo.supermarket.model.Offer;
import dojo.supermarket.model.Product;

public class TwoForAmountStrategy implements SpecialOfferCalculationStrategy {

    @Override
    public Discount calculateDiscount(Product product, double quantity, double unitPrice, double offerArgument) {
        int quantityAsInt = (int) quantity;
        if (quantityAsInt >= 2) {

                double total = offerArgument * (quantityAsInt / 2) + quantityAsInt % 2 * unitPrice;

                double discountN = unitPrice * quantity - total;

                return new Discount(product, "2 for " + offerArgument, -discountN);


        }
        return null;
    }
}
