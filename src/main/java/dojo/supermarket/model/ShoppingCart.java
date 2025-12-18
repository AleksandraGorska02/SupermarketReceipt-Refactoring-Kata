package dojo.supermarket.model;

import dojo.supermarket.model.bundle.Bundle;
import dojo.supermarket.model.bundle.BundleDiscountStrategy;
import dojo.supermarket.model.coupon.Coupon;
import dojo.supermarket.model.coupon.CouponDiscountStrategy;
import dojo.supermarket.model.discount.Discount;
import dojo.supermarket.model.interfaces.SpecialOfferStrategies;
import dojo.supermarket.model.product.Product;
import dojo.supermarket.model.product.ProductQuantity;
import dojo.supermarket.model.receipt.Receipt;
import dojo.supermarket.model.specialOffer.Offer;

import java.time.LocalDate;
import java.util.*;

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

    void handleOffers(Receipt receipt, Map<Product, Offer> offers, List<Bundle> bundles,
                      List<Coupon> coupons, SpecialOfferStrategies.SupermarketCatalog catalog, LocalDate checkoutDate) {


        applyStandardOffers(receipt, offers, catalog);
        applyBundleOffers(receipt, bundles, catalog);
        applyCouponOffers(receipt, coupons, catalog, checkoutDate);
    }

    private void applyStandardOffers(Receipt receipt, Map<Product, Offer> offers, SpecialOfferStrategies.SupermarketCatalog catalog) {
        for (Product p : productQuantities.keySet()) {
            if (offers.containsKey(p)) {
                double quantity = productQuantities.get(p);
                double unitPrice = catalog.getUnitPrice(p);

                Discount discount = offers.get(p).getDiscount(quantity, unitPrice);
                if (discount != null) {
                    receipt.addDiscount(discount);
                }
            }
        }
    }

    private void applyBundleOffers(Receipt receipt, List<Bundle> bundles, SpecialOfferStrategies.SupermarketCatalog catalog) {
        SpecialOfferStrategies.BundleOfferStrategy bundleStrategy = new BundleDiscountStrategy();
        for (Bundle bundle : bundles) {
            Discount d = bundleStrategy.calculateBundleDiscount(bundle, productQuantities, catalog);
            if (d != null) {
                receipt.addDiscount(d);
            }
        }
    }

    private void applyCouponOffers(Receipt receipt, List<Coupon> coupons, SpecialOfferStrategies.SupermarketCatalog catalog, LocalDate checkoutDate) {
        SpecialOfferStrategies.CouponOfferStrategy couponStrategy = new CouponDiscountStrategy();
        for (Coupon coupon : coupons) {
            Product product = coupon.getProduct();
            double quantityInCart = productQuantities.getOrDefault(product, 0.0);
            double unitPrice = catalog.getUnitPrice(product);

            Discount discount = couponStrategy.calculateCouponDiscount(coupon, quantityInCart, unitPrice, checkoutDate);

            if (discount != null) {
                receipt.addDiscount(discount);
                coupon.markAsUsed();
            }
        }
    }


}
