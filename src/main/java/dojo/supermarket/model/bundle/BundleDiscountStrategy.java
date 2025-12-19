package dojo.supermarket.model.bundle;

import dojo.supermarket.model.interfaces.SupermarketCatalog;
import dojo.supermarket.model.discount.Discount;
import dojo.supermarket.model.interfaces.SpecialOfferStrategies;
import dojo.supermarket.model.product.Product;

import java.util.Map;

public class BundleDiscountStrategy implements SpecialOfferStrategies.BundleOfferStrategy {

    @Override
    public Discount calculateBundleDiscount(Bundle bundle, Map<Product, Double> cartQuantities, SupermarketCatalog catalog){

        double discountAmount = bundle.calculateDiscountValue(cartQuantities, catalog);

        if (discountAmount <= 0) {
            return null;
        }

        return new Discount(null, "Bundle Discount", -discountAmount);
    }


}