package dojo.supermarket.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {

    private SupermarketCatalog catalog;
    private Product toothbrush;
    private Product apples;

    @BeforeEach
    void setUp() {
        // Setup a simple catalog
        catalog = new FakeCatalog();
        toothbrush = new Product("Toothbrush", ProductUnit.EACH);
        apples = new Product("Apples", ProductUnit.KILO);
        catalog.addProduct(toothbrush, 1.50);
        catalog.addProduct(apples, 2.00);
    }

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

    @Test
    void addItemIncrementsQuantity() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(toothbrush); // +1
        cart.addItemQuantity(toothbrush, 2.0); // +2.0

        Map<Product, Double> quantities = cart.productQuantities();
        assertEquals(1, quantities.size());
        assertEquals(3.0, quantities.get(toothbrush), 0.001);
    }

    @Test
    void handleNoOffersCreatesNoDiscounts() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(toothbrush);
        Receipt receipt = new Receipt();

        // Empty offers map
        Map<Product, Offer> emptyOffers = new HashMap<>();
        cart.handleOffers(receipt, emptyOffers, catalog);

        assertTrue(receipt.getDiscounts().isEmpty());
    }

    // --- Offer Tests ---

    @Test
    void handleThreeForTwoOffer() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(toothbrush, 4.0); // 4 units @ 1.50 each
        Receipt receipt = new Receipt();
        Map<Product, Offer> offers = new HashMap<>();
        offers.put(toothbrush, new Offer(SpecialOfferType.THREE_FOR_TWO, toothbrush, 0)); // Argument ignored

        cart.handleOffers(receipt, offers, catalog);

        // Total units: 4.0. Groups of 3: 1. Remainder: 1.
        // Should pay for 2 items in the group + 1 remainder = 3 items total.
        // Discount should be 1 item's price: 1.50
        List<Discount> discounts = receipt.getDiscounts();
        assertEquals(1, discounts.size());
        assertEquals(-1.50, discounts.get(0).getDiscountAmount(), 0.001);
        assertEquals("3 for 2", discounts.get(0).getDescription());
    }

    @Test
    void handleThreeForTwoOfferWithLessThanThree() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(toothbrush, 2.0); // 2 units @ 1.50 each
        Receipt receipt = new Receipt();
        Map<Product, Offer> offers = new HashMap<>();
        offers.put(toothbrush, new Offer(SpecialOfferType.THREE_FOR_TWO, toothbrush, 0));

        cart.handleOffers(receipt, offers, catalog);
        assertTrue(receipt.getDiscounts().isEmpty());
    }

    @Test
    void handleTenPercentDiscountOffer() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(apples, 2.5); // 2.5 kg @ 2.00 per kg = 5.00 full price
        Receipt receipt = new Receipt();
        Map<Product, Offer> offers = new HashMap<>();
        offers.put(apples, new Offer(SpecialOfferType.TEN_PERCENT_DISCOUNT, apples, 10.0)); // 10%

        cart.handleOffers(receipt, offers, catalog);

        // Discount: 10% of 5.00 = 0.50
        List<Discount> discounts = receipt.getDiscounts();
        assertEquals(1, discounts.size());
        assertEquals(-0.50, discounts.get(0).getDiscountAmount(), 0.001);
        assertEquals("10.0% off", discounts.get(0).getDescription());
    }

    @Test
    void handleTwoForAmountOffer() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(toothbrush, 3.0); // 3 units @ 1.50 each = 4.50 full price
        Receipt receipt = new Receipt();
        Map<Product, Offer> offers = new HashMap<>();
        offers.put(toothbrush, new Offer(SpecialOfferType.TWO_FOR_AMOUNT, toothbrush, 2.00)); // 2 for 2.00

        cart.handleOffers(receipt, offers, catalog);

        // Total units: 3. Groups of 2: 1. Remainder: 1.
        // Price: (1 group * 2.00) + (1 remainder * 1.50) = 3.50
        // Discount: 4.50 - 3.50 = 1.00
        List<Discount> discounts = receipt.getDiscounts();
        assertEquals(1, discounts.size());
        assertEquals(-1.00, discounts.get(0).getDiscountAmount(), 0.001);
        assertEquals("2 for 2.0", discounts.get(0).getDescription());
    }

    @Test
    void handleTwoForAmountOfferWithLessThanTwo() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(toothbrush, 1.0); // 1 unit @ 1.50
        Receipt receipt = new Receipt();
        Map<Product, Offer> offers = new HashMap<>();
        offers.put(toothbrush, new Offer(SpecialOfferType.TWO_FOR_AMOUNT, toothbrush, 2.00));

        cart.handleOffers(receipt, offers, catalog);
        assertTrue(receipt.getDiscounts().isEmpty());
    }

    @Test
    void handleFiveForAmountOffer() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(toothbrush, 7.0); // 7 units @ 1.50 each = 10.50 full price
        Receipt receipt = new Receipt();
        Map<Product, Offer> offers = new HashMap<>();
        offers.put(toothbrush, new Offer(SpecialOfferType.FIVE_FOR_AMOUNT, toothbrush, 5.00)); // 5 for 5.00

        cart.handleOffers(receipt, offers, catalog);

        // Total units: 7. Groups of 5: 1. Remainder: 2.
        // Price: (1 group * 5.00) + (2 remainder * 1.50) = 5.00 + 3.00 = 8.00
        // Discount: 10.50 - 8.00 = 2.50
        List<Discount> discounts = receipt.getDiscounts();
        assertEquals(1, discounts.size());
        assertEquals(-2.50, discounts.get(0).getDiscountAmount(), 0.001);
        assertEquals("5 for 5.0", discounts.get(0).getDescription());
    }

    @Test
    void handleFiveForAmountOfferWithLessThanFive() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(toothbrush, 4.0);
        Receipt receipt = new Receipt();
        Map<Product, Offer> offers = new HashMap<>();
        offers.put(toothbrush, new Offer(SpecialOfferType.FIVE_FOR_AMOUNT, toothbrush, 5.00));

        cart.handleOffers(receipt, offers, catalog);
        assertTrue(receipt.getDiscounts().isEmpty());
    }

    @Test
    void handleOffersWithNoItemsInCart() {
        ShoppingCart cart = new ShoppingCart();
        Receipt receipt = new Receipt();
        Map<Product, Offer> offers = new HashMap<>();
        offers.put(toothbrush, new Offer(SpecialOfferType.THREE_FOR_TWO, toothbrush, 0));
        offers.put(apples, new Offer(SpecialOfferType.TEN_PERCENT_DISCOUNT, apples, 10.0));

        cart.handleOffers(receipt, offers, catalog);
        assertTrue(receipt.getDiscounts().isEmpty());
    }

    @Test
    void handleOffersWithMultipleProductsAndOffers() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(toothbrush, 3.0); // 3 units @ 1.50 each
        cart.addItemQuantity(apples, 5.0);      // 5 kg @ 2.00 per kg
        Receipt receipt = new Receipt();
        Map<Product, Offer> offers = new HashMap<>();
        offers.put(toothbrush, new Offer(SpecialOfferType.THREE_FOR_TWO, toothbrush, 0));
        offers.put(apples, new Offer(SpecialOfferType.TEN_PERCENT_DISCOUNT, apples, 10.0));

        cart.handleOffers(receipt, offers, catalog);

        List<Discount> discounts = receipt.getDiscounts();
        assertEquals(2, discounts.size());

        // Toothbrush discount
        Discount toothbrushDiscount = discounts.stream()
                .filter(d -> d.getDescription().equals("3 for 2"))
                .findFirst()
                .orElse(null);
        assertNotNull(toothbrushDiscount);
        assertEquals(-1.50, toothbrushDiscount.getDiscountAmount(), 0.001);

        // Apples discount
        Discount applesDiscount = discounts.stream()
                .filter(d -> d.getDescription().equals("10.0% off"))
                .findFirst()
                .orElse(null);
        assertNotNull(applesDiscount);
        // Total for apples: 5 kg * 2.00 = 10.00; 10% discount = 1.00
        assertEquals(-1.00, applesDiscount.getDiscountAmount(), 0.001);
    }
}