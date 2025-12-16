package dojo.supermarket.model.specialOfferCalculation;

import dojo.supermarket.model.Discount;
import dojo.supermarket.model.Offer;
import dojo.supermarket.model.Product;

public interface SpecialOfferCalculationStrategy {
    // Calculates the discount for a given product based on the offer details
    Discount calculateDiscount(Product product, double quantity, double unitPrice, Offer offer);
}
