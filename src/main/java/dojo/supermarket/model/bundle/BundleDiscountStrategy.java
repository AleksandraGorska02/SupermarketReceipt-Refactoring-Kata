package dojo.supermarket.model.bundle;

import dojo.supermarket.model.SupermarketCatalog;
import dojo.supermarket.model.product.Product;
import dojo.supermarket.model.specialOffer.Discount;
import dojo.supermarket.model.specialOffer.types.SpecialOfferStrategies;
import java.util.Map;

public class BundleDiscountStrategy implements SpecialOfferStrategies.BundleOfferStrategy {

    @Override
    public Discount calculateBundleDiscount(Bundle bundle, Map<Product, Double> cartQuantities, SupermarketCatalog catalog) {

        int numBundles = calculateMaxBundles(bundle, cartQuantities);

        if (numBundles <= 0) {
            return null;
        }

        double totalBundleValue = 0;
        Product representative = null;

        for (Map.Entry<Product, Double> entry : bundle.getProductsInBundle().entrySet()) {
            Product p = entry.getKey();
            if (representative == null) representative = p;

            double unitPrice = catalog.getUnitPrice(p);
            totalBundleValue += entry.getValue() * numBundles * unitPrice;
        }

        double discountAmount = totalBundleValue * (bundle.getDiscountPercentage() / 100.0);

        return new Discount(representative, "Bundle Discount", -discountAmount);
    }

    private int calculateMaxBundles(Bundle bundle, Map<Product, Double> cartQuantities) {
        double max = Double.MAX_VALUE;
        for (Map.Entry<Product, Double> entry : bundle.getProductsInBundle().entrySet()) {
            double inCart = cartQuantities.getOrDefault(entry.getKey(), 0.0);
            if (entry.getValue() > 0) {
                max = Math.min(max, Math.floor(inCart / entry.getValue()));
            }
        }
        return (max == Double.MAX_VALUE) ? 0 : (int) max;
    }
}