package dojo.supermarket.model.specialOffer.types;

import dojo.supermarket.model.specialOffer.Discount;
import dojo.supermarket.model.product.Product;

public interface SpecialOfferCalculationStrategy {
    // Calculates the discount for a given product based on the offer details
    Discount calculateDiscount(Product product, double quantity, double unitPrice, double offerArgument);
}
