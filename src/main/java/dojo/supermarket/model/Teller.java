package dojo.supermarket.model;

import dojo.supermarket.model.bundle.Bundle;
import dojo.supermarket.model.coupon.Coupon;
import dojo.supermarket.model.discount.Discount;
import dojo.supermarket.model.loyalty.LoyaltyCard;
import dojo.supermarket.model.loyalty.LoyaltyRedemptionStrategy;
import dojo.supermarket.model.product.Product;
import dojo.supermarket.model.product.ProductQuantity;
import dojo.supermarket.model.receipt.Receipt;
import dojo.supermarket.model.specialOffer.Offer;
import dojo.supermarket.model.specialOffer.SpecialOfferType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Teller {

    private final SupermarketCatalog catalog;
    private final Map<Product, Offer> offers = new HashMap<>();
    private final List<Bundle> bundles = new ArrayList<>();
    private final List<Coupon> coupons = new ArrayList<>();

    public Teller(SupermarketCatalog catalog) {
        this.catalog = catalog;
    }

    public void addSpecialOffer(SpecialOfferType offerType, Product product, double argument) {
        offers.put(product, new Offer(offerType, product, argument));
    }

    public void addBundleOffer(Bundle bundle) {
        this.bundles.add(bundle);
    }

    public void addCoupon(Coupon coupon) {
        coupons.add(coupon);
    }

    public Receipt checksOutArticlesFrom(ShoppingCart theCart, LoyaltyCard customerCard, LocalDate checkoutDate) {
        Receipt receipt = new Receipt();

        List<ProductQuantity> productQuantities = theCart.getItems();
        for (ProductQuantity pq : productQuantities) {
            Product p = pq.getProduct();
            double quantity = pq.getQuantity();
            double unitPrice = catalog.getUnitPrice(p);
            double price = quantity * unitPrice;
            receipt.addProduct(p, quantity, unitPrice, price);
        }


        theCart.handleOffers(receipt, offers, bundles, coupons, catalog, checkoutDate);

        if (customerCard != null) {
            processLoyaltyProgram(receipt, customerCard, theCart);
        }

        return receipt;
    }


    public Receipt checksOutArticlesFrom(ShoppingCart theCart) {
        return checksOutArticlesFrom(theCart, null, LocalDate.now());
    }

    public Receipt checksOutArticlesFrom(ShoppingCart theCart, LocalDate checkoutDate) {
        return checksOutArticlesFrom(theCart, null, checkoutDate);
    }

    public Receipt checksOutArticlesFrom(ShoppingCart theCart, LoyaltyCard customerCard) {
        return checksOutArticlesFrom(theCart, customerCard, LocalDate.now());
    }

    private void processLoyaltyProgram(Receipt receipt, LoyaltyCard customerCard, ShoppingCart theCart) {


        if (customerCard.getPointsBalance() > 0 && !theCart.getItems().isEmpty()) {


            LoyaltyRedemptionStrategy redemptionStrategy = new LoyaltyRedemptionStrategy();

            Discount redemption = redemptionStrategy.calculateRedemption(
                    customerCard,
                    receipt.getTotalPrice()

            );
            receipt.addDiscount(redemption);
        }
        receipt.setPointsEarned(receipt.getTotalPrice());

        customerCard.addPoints(receipt.getPointsEarned());


    }
}
