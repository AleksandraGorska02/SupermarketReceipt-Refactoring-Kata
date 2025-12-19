package dojo.supermarket.model.bundle;

import dojo.supermarket.model.interfaces.SupermarketCatalog;
import dojo.supermarket.model.product.Product;

import java.util.Collections;
import java.util.Map;

public class Bundle {

    private final Map<Product, Double> productsInBundle;
    private final double discountPercentage;

    public Bundle(Map<Product, Double> productsInBundle, double discountPercentage) {
        this.productsInBundle = Collections.unmodifiableMap(productsInBundle);
        this.discountPercentage = discountPercentage;
    }


    public double calculateDiscountValue(Map<Product, Double> cartQuantities, SupermarketCatalog catalog) {
        int numBundles = calculateMaxBundles(cartQuantities);
        if (numBundles <= 0) return 0;

        double totalValue = 0;
        for (Map.Entry<Product, Double> entry : productsInBundle.entrySet()) {
            totalValue += entry.getValue() * numBundles * catalog.getUnitPrice(entry.getKey());
        }
        return totalValue * (this.discountPercentage / 100.0);
    }


    private int calculateMaxBundles( Map<Product, Double> cartQuantities) {
        double max = Double.MAX_VALUE;
        for (Map.Entry<Product, Double> entry : productsInBundle.entrySet()) {
            double inCart = cartQuantities.getOrDefault(entry.getKey(), 0.0);
            if (entry.getValue() > 0) {

                    max = Math.min(max, Math.floor(inCart / entry.getValue()));

            }
        }
        return (max == Double.MAX_VALUE) ? 0 : (int) max;
    }

    public Map<Product, Double> getProductsInBundle() {
        return productsInBundle;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

}
