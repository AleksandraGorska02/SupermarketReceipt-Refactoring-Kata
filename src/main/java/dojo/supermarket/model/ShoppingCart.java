package dojo.supermarket.model;

import dojo.supermarket.model.product.Product;
import dojo.supermarket.model.product.ProductQuantity;
import dojo.supermarket.model.receipt.Receipt;
import dojo.supermarket.model.specialOffer.Bundle;
import dojo.supermarket.model.specialOffer.Discount;
import dojo.supermarket.model.specialOffer.Offer;
import dojo.supermarket.model.specialOffer.types.BundleDiscountStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart {

    private final List<ProductQuantity> items = new ArrayList<>();
    private final Map<Product, Double> productQuantities = new HashMap<>();

    List<ProductQuantity> getItems() {
        return Collections.unmodifiableList(items);
    }

    void addItem(Product product) {
        addItemQuantity(product, 1.0);
    }

    Map<Product, Double> productQuantities() {
        return Collections.unmodifiableMap(productQuantities);
    }

    public void addItemQuantity(Product product, double quantity) {
        items.add(new ProductQuantity(product, quantity));
        if (productQuantities.containsKey(product)) {
            productQuantities.put(product, productQuantities.get(product) + quantity);
        } else {
            productQuantities.put(product, quantity);
        }
    }

    void handleOffers(Receipt receipt, Map<Product, Offer> offers, List<Offer> bundleOffers, SupermarketCatalog catalog) {

        for (Product p : productQuantities().keySet()) {
            double quantity = productQuantities.get(p);
            if (offers.containsKey(p)) {
                Offer offer = offers.get(p);
                double unitPrice = catalog.getUnitPrice(p);
                Discount discount = offer.getDiscount(quantity, unitPrice);
                if (discount != null) {
                    receipt.addDiscount(discount);
                }
            }
        }


        for (Offer bundleOffer : bundleOffers) {
            Bundle bundle = bundleOffer.getBundle();
            int numBundles = calculateMaxBundles(bundle);

            if (numBundles > 0) {
                double totalBundlePrice = 0;
                Product representativeProduct = null;

                for (Map.Entry<Product, Double> entry : bundle.getProductsInBundle().entrySet()) {
                    Product p = entry.getKey();
                     if (representativeProduct == null) representativeProduct = p;
                    totalBundlePrice += entry.getValue() * numBundles * catalog.getUnitPrice(p);
                }


                Discount discount = BundleDiscountStrategy.createBundleDiscount(
                        representativeProduct,
                        totalBundlePrice,
                        bundle.getDiscountPercentage(),
                        numBundles
                );

                receipt.addDiscount(discount);
            }
        }
    }

    private int calculateMaxBundles(Bundle bundle) {
        double max = Double.MAX_VALUE;
        for (Map.Entry<Product, Double> entry : bundle.getProductsInBundle().entrySet()) {
            double inCart = productQuantities.getOrDefault(entry.getKey(), 0.0);
            if (entry.getValue() > 0) {
                max = Math.min(max, Math.floor(inCart / entry.getValue()));
            }
        }
        return (max == Double.MAX_VALUE) ? 0 : (int) max;
    }
}
