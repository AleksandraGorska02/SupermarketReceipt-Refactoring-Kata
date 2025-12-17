package dojo.supermarket.model.bundle;

import dojo.supermarket.model.product.Product;
import dojo.supermarket.model.specialOffer.Discount;
import dojo.supermarket.model.specialOffer.types.SpecialOfferCalculationStrategy;

public class BundleDiscountStrategy implements SpecialOfferCalculationStrategy {
    @Override
    public Discount calculateDiscount(Product product, double quantity, double unitPrice, double offerArgument) {
        return null;
    }


    public static Discount createBundleDiscount(Product product, double totalBundlePrice, double discountPercentage, int numberOfBundles) {
        double discountAmount = totalBundlePrice * (discountPercentage / 100.0);
        String description = "Bundle Discount ("+product.getName()+")";

        return new Discount(product, description, -discountAmount);
    }

}