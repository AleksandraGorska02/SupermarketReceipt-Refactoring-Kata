package dojo.supermarket.model.interfaces;

import dojo.supermarket.model.SupermarketCatalog;
import dojo.supermarket.model.bundle.Bundle;
import dojo.supermarket.model.coupon.Coupon;
import dojo.supermarket.model.discount.Discount;
import dojo.supermarket.model.product.Product;

import java.time.LocalDate;
import java.util.Map;

public interface SpecialOfferStrategies {

    interface SingleProductOfferStrategy {
        Discount calculateDiscount(Product product, double quantity, double unitPrice, double offerArgument);
    }

    interface BundleOfferStrategy {
        Discount calculateBundleDiscount(Bundle bundle, Map<Product, Double> cartQuantities, SupermarketCatalog catalog);
    }

    interface CouponOfferStrategy {
        Discount calculateCouponDiscount(Coupon coupon, double cartQuantity, double unitPrice, LocalDate checkoutDate);
    }
}

