package dojo.supermarket.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SupermarketTest {

    private SupermarketCatalog catalog;
    private Product toothbrush;
    private Product apples;
    private Product cheese;
    private Product soap;

    // --- Mock Implementation for Testing ---
    static class FakeCatalog implements SupermarketCatalog {
        private final Map<String, Double> prices = new HashMap<>();

        @Override
        public void addProduct(Product product, double price) {
            prices.put(product.getName(), price);
        }

        @Override
        public double getUnitPrice(Product product) {
            return prices.getOrDefault(product.getName(), 0.0);
        }
    }
    // ----------------------------------------

    @BeforeEach
    void setUp() {
        catalog = new FakeCatalog();
        toothbrush = new Product("toothbrush", ProductUnit.EACH);
        apples = new Product("apples", ProductUnit.KILO);
        cheese = new Product("cheese", ProductUnit.EACH);
        soap = new Product("soap", ProductUnit.EACH);

        catalog.addProduct(toothbrush, 0.99); // 0.99 each
        catalog.addProduct(apples, 1.99);     // 1.99 per kilo
        catalog.addProduct(cheese, 2.50);     // 2.50 each
        catalog.addProduct(soap, 0.50);       // 0.50 each
    }

    @Test
    void tenPercentDiscountOnOneItem() {
        // ARRANGE
        Teller teller = new Teller(catalog);
        teller.addSpecialOffer(SpecialOfferType.TEN_PERCENT_DISCOUNT, toothbrush, 10.0);

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(toothbrush, 5.0); // 5 * 0.99 = 4.95 full price

        // ACT
        Receipt receipt = teller.checksOutArticlesFrom(cart);

        // ASSERT
        double fullPrice = 5.0 * 0.99; // 4.95
        double discountAmount = fullPrice * 0.10; // 0.495
        double expectedTotal = fullPrice - discountAmount; // 4.455

        assertEquals(expectedTotal, receipt.getTotalPrice(), 0.001);
        assertFalse(receipt.getDiscounts().isEmpty());

        Discount discount = receipt.getDiscounts().get(0);
        assertEquals(-discountAmount, discount.getDiscountAmount(), 0.001);
        assertEquals("10.0% off", discount.getDescription());
    }

    @Test
    void threeForTwoDiscount() {
        // ARRANGE
        Teller teller = new Teller(catalog);
        teller.addSpecialOffer(SpecialOfferType.THREE_FOR_TWO, soap, 0); // 3 for 2 on soap @ 0.50

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(soap, 4.0); // 4 units @ 0.50 = 2.00 full price

        // ACT
        Receipt receipt = teller.checksOutArticlesFrom(cart);

        // ASSERT
        // Should pay for 3 units: (1 group of 3 pays for 2) + 1 remainder
        // Expected price: 3 * 0.50 = 1.50
        double discountAmount = 1 * 0.50; // One item is free
        double expectedTotal = 2.00 - discountAmount; // 1.50

        assertEquals(expectedTotal, receipt.getTotalPrice(), 0.001);
        assertFalse(receipt.getDiscounts().isEmpty());

        Discount discount = receipt.getDiscounts().get(0);
        assertEquals(-discountAmount, discount.getDiscountAmount(), 0.001);
        assertEquals("3 for 2", discount.getDescription());
    }

    @Test
    void threeForTwoDiscountOnExactGroup() {
        // ARRANGE
        Teller teller = new Teller(catalog);
        teller.addSpecialOffer(SpecialOfferType.THREE_FOR_TWO, soap, 0);

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(soap, 3.0); // 3 units @ 0.50 = 1.50 full price

        // ACT
        Receipt receipt = teller.checksOutArticlesFrom(cart);

        // ASSERT
        // Expected price: 2 * 0.50 = 1.00
        double discountAmount = 0.50;
        assertEquals(1.00, receipt.getTotalPrice(), 0.001);
        assertEquals(-discountAmount, receipt.getDiscounts().get(0).getDiscountAmount(), 0.001);
    }

    @Test
    void twoForAmountDiscount() {
        // ARRANGE
        Teller teller = new Teller(catalog);
        teller.addSpecialOffer(SpecialOfferType.TWO_FOR_AMOUNT, cheese, 3.00); // 2 cheese for 3.00 (regular 2 * 2.50 = 5.00)

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(cheese, 3.0); // 3 units @ 2.50 = 7.50 full price

        // ACT
        Receipt receipt = teller.checksOutArticlesFrom(cart);

        // ASSERT
        // Price: (1 group of 2 pays 3.00) + (1 remainder pays 2.50) = 5.50
        double fullPrice = 3.0 * 2.50; // 7.50
        double discountAmount = fullPrice - 5.50; // 2.00
        double expectedTotal = 5.50;

        assertEquals(expectedTotal, receipt.getTotalPrice(), 0.001);
        assertFalse(receipt.getDiscounts().isEmpty());

        Discount discount = receipt.getDiscounts().get(0);
        assertEquals(-discountAmount, discount.getDiscountAmount(), 0.001);
        assertEquals("2 for 3.0", discount.getDescription());
    }

    @Test
    void fiveForAmountDiscount() {
        // ARRANGE
        Teller teller = new Teller(catalog);
        teller.addSpecialOffer(SpecialOfferType.FIVE_FOR_AMOUNT, toothbrush, 3.00); // 5 toothbrushes for 3.00 (regular 5 * 0.99 = 4.95)

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(toothbrush, 7.0); // 7 units @ 0.99 = 6.93 full price

        // ACT
        Receipt receipt = teller.checksOutArticlesFrom(cart);

        // ASSERT
        // Price: (1 group of 5 pays 3.00) + (2 remainder pay 2 * 0.99 = 1.98) = 4.98
        double fullPrice = 7.0 * 0.99; // 6.93
        double expectedTotal = 4.98;
        double discountAmount = fullPrice - expectedTotal; // 1.95

        assertEquals(expectedTotal, receipt.getTotalPrice(), 0.001);
        assertFalse(receipt.getDiscounts().isEmpty());

        Discount discount = receipt.getDiscounts().get(0);
        assertEquals(-discountAmount, discount.getDiscountAmount(), 0.001);
        assertEquals("5 for 3.0", discount.getDescription());
    }

    @Test
    void multipleOffersApplied() {
        // ARRANGE
        Teller teller = new Teller(catalog);
        teller.addSpecialOffer(SpecialOfferType.TEN_PERCENT_DISCOUNT, apples, 10.0); // 10% off apples
        teller.addSpecialOffer(SpecialOfferType.THREE_FOR_TWO, soap, 0);             // 3 for 2 on soap

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(apples, 2.0); // 2.0 kg @ 1.99 = 3.98 full price
        cart.addItemQuantity(soap, 6.0);   // 6 units @ 0.50 = 3.00 full price

        // ACT
        Receipt receipt = teller.checksOutArticlesFrom(cart);

        // ASSERT
        // Apples Discount: 3.98 * 0.10 = 0.398
        // Soap Discount: 2 items free (6/3=2 groups) = 2 * 0.50 = 1.00

        // Full Total: 3.98 + 3.00 = 6.98
        // Total Discount: 0.398 + 1.00 = 1.398
        double expectedTotal = 6.98 - 1.398; // 5.582

        assertEquals(expectedTotal, receipt.getTotalPrice(), 0.001);

        List<Discount> discounts = receipt.getDiscounts();
        assertEquals(2, discounts.size());

        // Assert discounts are present with correct amounts
        assertTrue(discounts.stream().anyMatch(d -> d.getProduct().equals(apples) && Math.abs(d.getDiscountAmount() + 0.398) < 0.001));
        assertTrue(discounts.stream().anyMatch(d -> d.getProduct().equals(soap) && Math.abs(d.getDiscountAmount() + 1.00) < 0.001));
    }
}