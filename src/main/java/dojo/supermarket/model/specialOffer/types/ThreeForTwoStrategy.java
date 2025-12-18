package dojo.supermarket.model.specialOffer.types;

import dojo.supermarket.model.discount.Discount;
import dojo.supermarket.model.interfaces.SpecialOfferStrategies;
import dojo.supermarket.model.product.Product;

public class ThreeForTwoStrategy implements SpecialOfferStrategies.SingleProductOfferStrategy {

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