package dojo.supermarket.model.specialOffer.types;

import dojo.supermarket.model.discount.Discount;
import dojo.supermarket.model.interfaces.SpecialOfferStrategies;
import dojo.supermarket.model.product.Product;

public class TwoForAmountStrategy implements SpecialOfferStrategies.SingleProductOfferStrategy {

    @Override
    public Discount calculateDiscount(Product product, double quantity, double unitPrice, double offerArgument) {

        if (quantity >= 2) {

            double total = offerArgument * Math.floor(quantity / 2.0) + quantity % 2 * unitPrice;

            double discountN = unitPrice * quantity - total;

            return new Discount(product, "2 for " + offerArgument, -discountN);


        }
        return null;
    }
}
