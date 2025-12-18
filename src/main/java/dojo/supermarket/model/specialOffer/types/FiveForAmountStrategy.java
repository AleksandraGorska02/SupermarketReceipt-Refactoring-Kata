package dojo.supermarket.model.specialOffer.types;

import dojo.supermarket.model.discount.Discount;
import dojo.supermarket.model.interfaces.SpecialOfferStrategies;
import dojo.supermarket.model.product.Product;

public class FiveForAmountStrategy implements SpecialOfferStrategies.SingleProductOfferStrategy {

    @Override
    public Discount calculateDiscount(Product product, double quantity, double unitPrice, double offerArgument) {
        int quantityAsInt = (int) quantity;
        if (quantityAsInt >= 5) {
            int numberOfXs = quantityAsInt / 5;
            double discountTotal = unitPrice * quantity - (offerArgument * numberOfXs + quantityAsInt % 5 * unitPrice);
            return new Discount(product, 5 + " for " + offerArgument, -discountTotal);
        }
        return null;
    }


}