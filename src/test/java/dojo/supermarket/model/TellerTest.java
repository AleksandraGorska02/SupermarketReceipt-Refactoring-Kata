package dojo.supermarket.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TellerTest {

    private SupermarketCatalog catalog;
    private Product toothbrush;
    private Product apples;

    // Mock implementation of SupermarketCatalog for testing
    static class FakeCatalog implements SupermarketCatalog {
        private final Map<String, Double> prices = new HashMap<>();

        @Override
        public void addProduct(Product product, double price) {
            prices.put(product.getName(), price);
        }

        @Override
        public double getUnitPrice(Product product) {
            return prices.get(product.getName());
        }
    }

    @BeforeEach
    void setUp() {
        catalog = new FakeCatalog();
        toothbrush = new Product("Toothbrush", ProductUnit.EACH);
        apples = new Product("Apples", ProductUnit.KILO);
        catalog.addProduct(toothbrush, 1.50); // $1.50 each
        catalog.addProduct(apples, 2.00);     // $2.00 per kilo
    }

    @Test
    void checkOutWithoutOffers() {
        Teller teller = new Teller(catalog);
        ShoppingCart cart = new ShoppingCart();

        cart.addItemQuantity(toothbrush, 2.0); // 2 * 1.50 = 3.00
        cart.addItemQuantity(apples, 0.5);     // 0.5 * 2.00 = 1.00

        Receipt receipt = teller.checksOutArticlesFrom(cart);

        // Check total price
        assertEquals(4.00, receipt.getTotalPrice(), 0.001);

        // Check receipt items
        assertEquals(2, receipt.getItems().size());
        assertEquals(3.00, receipt.getItems().stream().filter(i -> i.getProduct().equals(toothbrush)).findFirst().get().getTotalPrice(), 0.001);
        assertTrue(receipt.getDiscounts().isEmpty());
    }

    @Test
    void checkOutWithTenPercentDiscountOffer() {
        Teller teller = new Teller(catalog);
        ShoppingCart cart = new ShoppingCart();

        // Add special offer: 10% off apples
        teller.addSpecialOffer(SpecialOfferType.TEN_PERCENT_DISCOUNT, apples, 10.0);

        cart.addItemQuantity(apples, 2.0); // 2.0 kg @ 2.00 = 4.00 full price

        Receipt receipt = teller.checksOutArticlesFrom(cart);

        // Full Price: 4.00
        // Discount: 10% of 4.00 = 0.40
        // Total: 4.00 - 0.40 = 3.60
        assertEquals(3.60, receipt.getTotalPrice(), 0.001);

        // Check discount
        assertEquals(1, receipt.getDiscounts().size());
        assertEquals(-0.40, receipt.getDiscounts().get(0).getDiscountAmount(), 0.001);
    }

    @Test
    void checkOutWithThreeForTwoOffer() {
        Teller teller = new Teller(catalog);
        ShoppingCart cart = new ShoppingCart();

        // Add special offer: 3 for 2 on toothbrushes
        teller.addSpecialOffer(SpecialOfferType.THREE_FOR_TWO, toothbrush, 0);

        cart.addItemQuantity(toothbrush, 3.0); // 3 units @ 1.50 = 4.50 full price

        Receipt receipt = teller.checksOutArticlesFrom(cart);

        // Full Price: 4.50
        // Discount: 1 unit free = 1.50
        // Total: 4.50 - 1.50 = 3.00
        assertEquals(3.00, receipt.getTotalPrice(), 0.001);

        // Check discount
        assertEquals(1, receipt.getDiscounts().size());
        assertEquals(-1.50, receipt.getDiscounts().get(0).getDiscountAmount(), 0.001);
    }

    @Test
    void checkOutWithTwoForAmountOffer() {
        Teller teller = new Teller(catalog);
        ShoppingCart cart = new ShoppingCart();

        // Add special offer: 2 for 2.50 on toothbrushes
        teller.addSpecialOffer(SpecialOfferType.TWO_FOR_AMOUNT, toothbrush, 2.50);

        cart.addItemQuantity(toothbrush, 4.0); // 4 units @ 1.50 = 6.00 full price

        Receipt receipt = teller.checksOutArticlesFrom(cart);

        // Full Price: 6.00
        // Discount: (2 groups of 2 for 2.50) = 5.00, so discount = 6.00 - 5.00 = 1.00
        // Total: 6.00 - 1.00 = 5.00
        assertEquals(5.00, receipt.getTotalPrice(), 0.001);

        // Check discount
        assertEquals(1, receipt.getDiscounts().size());
        assertEquals(-1.00, receipt.getDiscounts().get(0).getDiscountAmount(), 0.001);
    }

    @Test
    void checkOutWithNoItems() {
        Teller teller = new Teller(catalog);
        ShoppingCart cart = new ShoppingCart();

        Receipt receipt = teller.checksOutArticlesFrom(cart);

        // Check total price
        assertEquals(0.00, receipt.getTotalPrice(), 0.001);

        // Check receipt items
        assertTrue(receipt.getItems().isEmpty());
        assertTrue(receipt.getDiscounts().isEmpty());
    }

    @Test
    void checkOutWithMultipleOffers() {
        Teller teller = new Teller(catalog);
        ShoppingCart cart = new ShoppingCart();

        // Add special offers
        teller.addSpecialOffer(SpecialOfferType.TEN_PERCENT_DISCOUNT, apples, 10.0); // 10% off apples
        teller.addSpecialOffer(SpecialOfferType.THREE_FOR_TWO, toothbrush, 0);       // 3 for 2 on toothbrushes

        cart.addItemQuantity(apples, 1.0);      // 1.0 kg @ 2.00 = 2.00 full price
        cart.addItemQuantity(toothbrush, 3.0);   // 3 units @ 1.50 = 4.50 full price

        Receipt receipt = teller.checksOutArticlesFrom(cart);

        // Apples Discount: 10% of 2.00 = 0.20
        // Toothbrush Discount: 1 unit free = 1.50
        // Full Total: 2.00 + 4.50 = 6.50
        // Total Discounts: 0.20 + 1.50 = 1.70
        // Final Total: 6.50 - 1.70 = 4.80
        assertEquals(4.80, receipt.getTotalPrice(), 0.001);

        // Check discounts
        assertEquals(2, receipt.getDiscounts().size());
    }
}