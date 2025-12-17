package dojo.supermarket.model.specialOffer;

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

    public Map<Product, Double> getProductsInBundle() {
        return productsInBundle;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }
}
