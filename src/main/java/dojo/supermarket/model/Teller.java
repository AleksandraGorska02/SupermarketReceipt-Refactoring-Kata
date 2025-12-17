package dojo.supermarket.model;

import dojo.supermarket.model.coupon.Coupon;
import dojo.supermarket.model.product.Product;
import dojo.supermarket.model.product.ProductQuantity;
import dojo.supermarket.model.receipt.Receipt;
import dojo.supermarket.model.specialOffer.Bundle;
import dojo.supermarket.model.specialOffer.Offer;
import dojo.supermarket.model.specialOffer.types.SpecialOfferCalculationStrategy;
import dojo.supermarket.model.specialOffer.SpecialOfferType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Teller {

    private final SupermarketCatalog catalog;
    private final Map<Product, Offer> offers = new HashMap<>();
    private final List<Offer> bundleOffers = new ArrayList<>();
    private final List<Coupon> coupons = new ArrayList<>();
    public Teller(SupermarketCatalog catalog) {
        this.catalog = catalog;
    }

    public void addSpecialOffer(SpecialOfferType offerType, Product product, double argument) {
        SpecialOfferCalculationStrategy strategy = offerType.getStrategy();
        offers.put(product, new Offer(offerType, product, argument, strategy));
    }

    public void addBundleOffer(Bundle bundle) {
            this.bundleOffers.add(new Offer(SpecialOfferType.BUNDLE_DISCOUNT, bundle, SpecialOfferType.BUNDLE_DISCOUNT.getStrategy()));
    }

    public void addCoupon(Coupon coupon) {
        coupons.add(coupon);
    }
    public Receipt checksOutArticlesFrom(ShoppingCart theCart, LocalDate checkoutDate) {
        Receipt receipt = new Receipt();
        List<ProductQuantity> productQuantities = theCart.getItems();
        for (ProductQuantity pq : productQuantities) {
            Product p = pq.getProduct();
            double quantity = pq.getQuantity();
            double unitPrice = catalog.getUnitPrice(p);
            double price = quantity * unitPrice;
            receipt.addProduct(p, quantity, unitPrice, price);
        }

        theCart.handleOffers(receipt, offers, bundleOffers, coupons, catalog, checkoutDate);

        return receipt;
    }


    public Receipt checksOutArticlesFrom(ShoppingCart theCart) {
        return checksOutArticlesFrom(theCart, LocalDate.now());
    }
}
