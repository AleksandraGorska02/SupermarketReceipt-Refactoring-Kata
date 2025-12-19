package dojo.supermarket.model;

import dojo.supermarket.ReceiptPrinter;
import dojo.supermarket.model.coupon.Coupon;
import dojo.supermarket.model.interfaces.SupermarketCatalog;
import dojo.supermarket.model.loyalty.LoyaltyCard;
import dojo.supermarket.model.product.Product;
import dojo.supermarket.model.product.ProductUnit;
import dojo.supermarket.model.receipt.Receipt;
import dojo.supermarket.model.bundle.Bundle;
import dojo.supermarket.model.specialOffer.SpecialOfferType;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class SupermarketTest {
    private SupermarketCatalog catalog;
    private Teller teller;
    private ShoppingCart theCart;
    private Product toothbrush;
    private Product rice;
    private Product apples;
    private Product cherryTomatoes;

    @BeforeEach
    public void setUp() {
        catalog = new FakeCatalog();
        teller = new Teller(catalog);
        theCart = new ShoppingCart();

        toothbrush = new Product("toothbrush", ProductUnit.EACH);
        catalog.addProduct(toothbrush, 0.99);
        rice = new Product("rice", ProductUnit.EACH);
        catalog.addProduct(rice, 2.99);
        apples = new Product("apples", ProductUnit.KILO);
        catalog.addProduct(apples, 1.99);
        cherryTomatoes = new Product("cherry tomato box", ProductUnit.EACH);
        catalog.addProduct(cherryTomatoes, 0.69);

    }

    @Test
    public void an_empty_shopping_cart_should_cost_nothing() {
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void one_normal_item() {
        theCart.addItem(toothbrush);
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void two_normal_items() {
        theCart.addItem(toothbrush);
        theCart.addItem(rice);
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void buy_two_get_one_free() {
        theCart.addItem(toothbrush);
        theCart.addItem(toothbrush);
        theCart.addItem(toothbrush);
        teller.addSpecialOffer(SpecialOfferType.THREE_FOR_TWO, toothbrush, catalog.getUnitPrice(toothbrush));
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void buy_two_get_one_free_but_insufficient_in_basket() {
        theCart.addItem(toothbrush);
        teller.addSpecialOffer(SpecialOfferType.THREE_FOR_TWO, toothbrush, catalog.getUnitPrice(toothbrush));
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }
    @Test
    public void buy_five_get_one_free() {
        theCart.addItem(toothbrush);
        theCart.addItem(toothbrush);
        theCart.addItem(toothbrush);
        theCart.addItem(toothbrush);
        theCart.addItem(toothbrush);
        teller.addSpecialOffer(SpecialOfferType.THREE_FOR_TWO, toothbrush, catalog.getUnitPrice(toothbrush));
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void loose_weight_product() {
        theCart.addItemQuantity(apples, .5);
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void percent_discount() {
        theCart.addItem(rice);
        teller.addSpecialOffer(SpecialOfferType.TEN_PERCENT_DISCOUNT, rice, 10.0);
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void xForY_discount() {
        theCart.addItem(cherryTomatoes);
        theCart.addItem(cherryTomatoes);
        teller.addSpecialOffer(SpecialOfferType.TWO_FOR_AMOUNT, cherryTomatoes,.99);
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void xForY_discount_with_insufficient_in_basket() {
        theCart.addItem(cherryTomatoes);
        teller.addSpecialOffer(SpecialOfferType.TWO_FOR_AMOUNT, cherryTomatoes,.99);
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void FiveForY_discount() {
        theCart.addItemQuantity(apples, 5);
        teller.addSpecialOffer(SpecialOfferType.FIVE_FOR_AMOUNT, apples,6.99);
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void FiveForY_discount_withSix() {
        theCart.addItemQuantity(apples, 6);
        teller.addSpecialOffer(SpecialOfferType.FIVE_FOR_AMOUNT, apples,5.99);
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void FiveForY_discount_withSixteen() {
        theCart.addItemQuantity(apples, 16);
        teller.addSpecialOffer(SpecialOfferType.FIVE_FOR_AMOUNT, apples,7.99);
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void FiveForY_discount_withFour() {
        theCart.addItemQuantity(apples, 4);
        teller.addSpecialOffer(SpecialOfferType.FIVE_FOR_AMOUNT, apples,8.99);
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void bundle_of_toothbrush_and_toothpaste() {
        // Setup a new product for the bundle
        Product toothpaste = new Product("toothpaste", ProductUnit.EACH);
        catalog.addProduct(toothpaste, 1.79);

        // Define the bundle requirements (1 toothbrush + 1 toothpaste)
        java.util.Map<Product, Double> bundleItems = new java.util.HashMap<>();
        bundleItems.put(toothbrush, 1.0);
        bundleItems.put(toothpaste, 1.0);

        Bundle bundle = new Bundle(bundleItems, 10.0);
        teller.addBundleOffer(bundle);

        // Add items to the cart
        theCart.addItem(toothbrush);
        theCart.addItem(toothpaste);

        // Expected discount: 10% of (0.99 + 1.79) = 0.278
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void two_complete_bundles_discounted() {
        Product toothpaste = new Product("toothpaste", ProductUnit.EACH);
        catalog.addProduct(toothpaste, 1.79);

        java.util.Map<Product, Double> bundleItems = new java.util.HashMap<>();
        bundleItems.put(toothbrush, 1.0);
        bundleItems.put(toothpaste, 1.0);

        teller.addBundleOffer(new Bundle(bundleItems, 10.0));

        // Add enough for two complete bundles
        theCart.addItem(toothbrush);
        theCart.addItem(toothbrush);
        theCart.addItem(toothpaste);
        theCart.addItem(toothpaste);

        // Expected discount: 10% of (2 * (0.99 + 1.79))
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void incomplete_bundle_receives_no_discount() {
        Product toothpaste = new Product("toothpaste", ProductUnit.EACH);
        catalog.addProduct(toothpaste, 1.79);

        java.util.Map<Product, Double> bundleItems = new java.util.HashMap<>();
        bundleItems.put(toothbrush, 1.0);
        bundleItems.put(toothpaste, 1.0);

        teller.addBundleOffer(new Bundle(bundleItems, 10.0));

        // Only add one of the two required items
        theCart.addItem(toothbrush);

        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void bundle_with_excess_items_only_discounts_complete_sets() {
        Product toothpaste = new Product("toothpaste", ProductUnit.EACH);
        catalog.addProduct(toothpaste, 1.79);

        java.util.Map<Product, Double> bundleItems = new java.util.HashMap<>();
        bundleItems.put(toothbrush, 1.0);
        bundleItems.put(toothpaste, 1.0);

        teller.addBundleOffer(new Bundle(bundleItems, 10.0));

        // Buy two toothbrushes and only one toothpaste
        theCart.addItem(toothbrush);
        theCart.addItem(toothbrush);
        theCart.addItem(toothpaste);

        // Only one complete bundle exists; the second toothbrush should be regular price.
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void coupon_discount_applied_when_valid_and_quantity_met() {
        Product orangeJuice = new Product("orange juice", ProductUnit.EACH);
        catalog.addProduct(orangeJuice, 2.00);

        // Coupon: Valid 13/11 to 15/11. Buy 6, get up to 6 more at 50% off (factor 0.5)
        Coupon coupon = new Coupon(
                orangeJuice,
                java.time.LocalDate.of(2025, 11, 13),
                java.time.LocalDate.of(2025, 11, 15),
                6.0, 6.0, 0.5
        );
        teller.addCoupon(coupon);

        // Buy 12 items on Nov 14th (Valid date)
        for (int i = 0; i < 12; i++) {
            theCart.addItem(orangeJuice);
        }

        // 6 items at $2.00 + 6 items at $1.00 (50% off) = $18.00 total
        java.time.LocalDate checkoutDate = java.time.LocalDate.of(2025, 11, 14);
        Receipt receipt = teller.checksOutArticlesFrom(theCart, checkoutDate);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }
    @Test
    public void coupon_discount_for_one_product() {
        Product orangeJuice = new Product("orange juice", ProductUnit.EACH);
        catalog.addProduct(orangeJuice, 2.00);
        theCart.addItem(orangeJuice);

        Coupon coupon = new Coupon(
                orangeJuice,
                java.time.LocalDate.of(2025, 11, 13),
                java.time.LocalDate.of(2025, 11, 15), 0, 1, 0.5
        );
        teller.addCoupon(coupon);



        java.time.LocalDate checkoutDate = java.time.LocalDate.of(2025, 11, 14);
        Receipt receipt = teller.checksOutArticlesFrom(theCart, checkoutDate);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }
    @Test
    public void coupon_discount_ignored_when_expired() {
        Product orangeJuice = new Product("orange juice", ProductUnit.EACH);
        catalog.addProduct(orangeJuice, 2.00);

        Coupon coupon = new Coupon(
                orangeJuice,
                java.time.LocalDate.of(2025, 11, 13),
                java.time.LocalDate.of(2025, 11, 15),
                6.0, 6.0, 0.5
        );
        teller.addCoupon(coupon);

        for (int i = 0; i < 12; i++) {
            theCart.addItem(orangeJuice);
        }

        // Checkout on Nov 16th (Expired)
        java.time.LocalDate checkoutDate = java.time.LocalDate.of(2025, 11, 16);
        Receipt receipt = teller.checksOutArticlesFrom(theCart, checkoutDate);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void coupon_discount_limited_to_max_quantity() {
        Product orangeJuice = new Product("orange juice", ProductUnit.EACH);
        catalog.addProduct(orangeJuice, 2.00);

        // Coupon: buy 6, get MAX 6 more at half price
        Coupon coupon = new Coupon(
                orangeJuice,
                java.time.LocalDate.of(2025, 11, 13),
                java.time.LocalDate.of(2025, 11, 15),
                6.0, 6.0, 0.5
        );
        teller.addCoupon(coupon);

        // Buy 20 items (Excessive quantity)
        for (int i = 0; i < 20; i++) {
            theCart.addItem(orangeJuice);
        }

        // Only 6 items should be discounted, the rest (14) at full price
        java.time.LocalDate checkoutDate = java.time.LocalDate.of(2025, 11, 14);
        Receipt receipt = teller.checksOutArticlesFrom(theCart, checkoutDate);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void coupon_can_only_be_used_once() {
        Product orangeJuice = new Product("orange juice", ProductUnit.EACH);
        catalog.addProduct(orangeJuice, 2.00);

        Coupon coupon = new Coupon(
                orangeJuice,
                java.time.LocalDate.of(2025, 11, 13),
                java.time.LocalDate.of(2025, 11, 15),
                6.0, 6.0, 0.5
        );
        teller.addCoupon(coupon);

        for (int i = 0; i < 12; i++) {
            theCart.addItem(orangeJuice);
        }

        java.time.LocalDate checkoutDate = java.time.LocalDate.of(2025, 11, 14);

        // First checkout: Coupon should apply
        Receipt receipt1 = teller.checksOutArticlesFrom(theCart, checkoutDate);
        assertFalse(receipt1.getDiscounts().isEmpty());

        // Second checkout with same Cart/Teller: Coupon should NOT apply again
        Receipt receipt2 = teller.checksOutArticlesFrom(theCart, checkoutDate);
        // We expect 0 discounts in the second receipt because the coupon is "spent"
        assertTrue(receipt2.getDiscounts().stream().noneMatch(d -> d.getDescription().equals("Coupon Discount")));
    }

    @Test
    public void coupon_only_applies_to_its_specific_product() {
        Product orangeJuice = new Product("orange juice", ProductUnit.EACH);
        catalog.addProduct(orangeJuice, 2.00);

        // Coupon specifically for orange juice
        Coupon coupon = new Coupon(
                orangeJuice,
                java.time.LocalDate.of(2025, 11, 13),
                java.time.LocalDate.of(2025, 11, 15),
                1.0, 1.0, 0.5
        );
        teller.addCoupon(coupon);

        // Add rice to the cart (which is NOT orange juice)
        theCart.addItem(rice);

        // Checkout on a valid date
        java.time.LocalDate checkoutDate = java.time.LocalDate.of(2025, 11, 14);
        Receipt receipt = teller.checksOutArticlesFrom(theCart, checkoutDate);

        // The receipt should have no discounts because the product doesn't match
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void coupon_cannot_be_redeemed_twice_first() {
        Product orangeJuice = new Product("orange juice", ProductUnit.EACH);
        catalog.addProduct(orangeJuice, 2.00);

        Coupon coupon = new Coupon(
                orangeJuice,
                java.time.LocalDate.of(2025, 11, 13),
                java.time.LocalDate.of(2025, 11, 15),
                1.0, 1.0, 0.5
        );
        teller.addCoupon(coupon);

        theCart.addItemQuantity(orangeJuice, 2.0);

        java.time.LocalDate checkoutDate = java.time.LocalDate.of(2025, 11, 14);

        // First checkout applies the coupon
        Receipt firstReceipt = teller.checksOutArticlesFrom(theCart, checkoutDate);

        // Second checkout with the SAME coupon object
        // Since it's "valid only once", the second receipt should have no discount
        Receipt secondReceipt = teller.checksOutArticlesFrom(theCart, checkoutDate);

        Approvals.verify(new ReceiptPrinter(40).printReceipt(firstReceipt));


    }
    @Test
    public void coupon_cannot_be_redeemed_twice_second() {
        Product orangeJuice = new Product("orange juice", ProductUnit.EACH);
        catalog.addProduct(orangeJuice, 2.00);

        Coupon coupon = new Coupon(
                orangeJuice,
                java.time.LocalDate.of(2025, 11, 13),
                java.time.LocalDate.of(2025, 11, 15),
                1.0, 1.0, 0.5
        );
        teller.addCoupon(coupon);

        theCart.addItemQuantity(orangeJuice, 2.0);

        java.time.LocalDate checkoutDate = java.time.LocalDate.of(2025, 11, 14);

        // First checkout applies the coupon
        Receipt firstReceipt = teller.checksOutArticlesFrom(theCart, checkoutDate);

        // Second checkout with the SAME coupon object
        // Since it's "valid only once", the second receipt should have no discount
        Receipt secondReceipt = teller.checksOutArticlesFrom(theCart, checkoutDate);


        Approvals.verify(new ReceiptPrinter(40).printReceipt(secondReceipt));

    }

    @Test
    public void loyalty_points_earned_and_redeemed() {
        LoyaltyCard card = new LoyaltyCard("customer-123");
        card.addPoints(100.0); // Starts with $10.00 credit (at 10:1 ratio)

        theCart.addItemQuantity(apples, 10.0); // $19.90 total

        // Checkout with loyalty card
        Receipt receipt = teller.checksOutArticlesFrom(theCart, card);

        // Should see a $10.00 redemption discount
        // Final total should be $9.90
        // Customer should earn 9 points on the remaining balance
        assertEquals(9.0, receipt.getPointsEarned());
        assertEquals(9.0, card.getPointsBalance()); // 100 - 100
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void earn_points_from_zero_balance() {
        // Create a new loyalty card with 0 points
        LoyaltyCard card = new LoyaltyCard("LC-001");

        theCart.addItemQuantity(rice, 2.0); // 2 * 2.99 = 5.98

        // Checkout: 1 point per $1 spent
        Receipt receipt = teller.checksOutArticlesFrom(theCart, card);

        // Final total 5.98 should earn 5 points
        assertEquals(5.0, receipt.getPointsEarned());
        assertEquals(5.0, card.getPointsBalance());
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void redemption_with_insufficient_points_for_full_payment() {
        // Card has 50 points = $5.00 credit (using 10:1 ratio)
        LoyaltyCard card = new LoyaltyCard("LC-002");
        card.addPoints(50.0);

        theCart.addItemQuantity(toothbrush, 10.0); // Total $9.90

        // Use a representative product (toothbrush) to avoid NullPointerException
        Receipt receipt = teller.checksOutArticlesFrom(theCart, card);

        // Total should be 9.90 - 5.00 = 4.90
        // New points earned on 4.90 = 4 points
        assertEquals(4.0, receipt.getPointsEarned());
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void redemption_limited_by_total_price() {
        // Card has 500 points = $50.00 credit
        LoyaltyCard card = new LoyaltyCard("LC-003");
        card.addPoints(500.0);

        theCart.addItem(toothbrush); // Total $0.99

        Receipt receipt = teller.checksOutArticlesFrom(theCart, card);

        // Redemption should be capped at 0.99, not the full 50.00 credit
        // Total should be 0.00
        assertEquals(0.0, receipt.getTotalPrice());
        assertEquals(490.0, card.getPointsBalance());
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void loyalty_points_with_three_for_two_offer() {
        LoyaltyCard card = new LoyaltyCard("LC-401");

        theCart.addItem(toothbrush);
        theCart.addItem(toothbrush);
        theCart.addItem(toothbrush);


        teller.addSpecialOffer(SpecialOfferType.THREE_FOR_TWO, toothbrush, catalog.getUnitPrice(toothbrush));

        // before: 2.97. after: 1.98
        Receipt receipt = teller.checksOutArticlesFrom(theCart, card);

        //  Total pointsld: floor(1.98) = 1 punkt
        assertEquals(1.0, receipt.getPointsEarned());
        assertEquals(1.0, card.getPointsBalance());
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void loyalty_points_with_bundle_discount() {
        LoyaltyCard card = new LoyaltyCard("LC-402");
        Product toothpaste = new Product("toothpaste", ProductUnit.EACH);
        catalog.addProduct(toothpaste, 1.79);

        java.util.Map<Product, Double> bundleItems = new java.util.HashMap<>();
        bundleItems.put(toothbrush, 1.0);
        bundleItems.put(toothpaste, 1.0);
        teller.addBundleOffer(new Bundle(bundleItems, 10.0));

        // toothbrush (0.99) + toothpaste (1.79) = 2.78
        theCart.addItem(toothbrush);
        theCart.addItem(toothpaste);

        // 10%: -0.28. Sum after: 2.50
        Receipt receipt = teller.checksOutArticlesFrom(theCart, card);

        //  Total pointsld: floor(2.50) = 2 punkty
        assertEquals(2.0, receipt.getPointsEarned());
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void loyalty_points_with_coupon_and_redemption() {

        LoyaltyCard card = new LoyaltyCard("LC-403");
        card.addPoints(50.0);

        Product orangeJuice = new Product("orange juice", ProductUnit.EACH);
        catalog.addProduct(orangeJuice, 2.00);

        Coupon coupon = new Coupon(orangeJuice, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), 6.0, 6.0, 0.5);
        teller.addCoupon(coupon);

        theCart.addItemQuantity(orangeJuice, 12.0);

        Receipt receipt = teller.checksOutArticlesFrom(theCart, card, LocalDate.now());

        assertEquals(13.0, receipt.getPointsEarned());
        assertEquals(13.0, card.getPointsBalance()); // 50 - 50 + 13
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }
}
