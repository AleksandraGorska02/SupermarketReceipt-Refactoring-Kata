package dojo.supermarket.model;

import dojo.supermarket.model.interfaces.SpecialOfferStrategies;
import dojo.supermarket.model.product.Product;
import dojo.supermarket.model.product.ProductUnit;
import dojo.supermarket.model.receipt.Receipt;
import dojo.supermarket.model.discount.Discount;
import dojo.supermarket.model.specialOffer.SpecialOfferType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {

    private SpecialOfferStrategies.SupermarketCatalog catalog;
    private Product toothbrush;
    private Product apples;
    private Teller teller; // Dodajemy Teller

    @BeforeEach
    void setUp() {
        // Setup a simple catalog
        catalog = new FakeCatalog();
        toothbrush = new Product("Toothbrush", ProductUnit.EACH);
        apples = new Product("Apples", ProductUnit.KILO);
        catalog.addProduct(toothbrush, 1.50);
        catalog.addProduct(apples, 2.00);

        teller = new Teller(catalog); // Inicjalizacja Teller
    }

    // Mock implementation of SupermarketCatalog for testing
    static class FakeCatalog implements SpecialOfferStrategies.SupermarketCatalog {
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

    // Ten test jest teraz redundantny, ponieważ Teller pośrednio go testuje, ale zostawiam
    @Test
    void handleNoOffersCreatesNoDiscounts() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(toothbrush);

        // Używamy Teller do przeprowadzenia transakcji
        Receipt receipt = teller.checksOutArticlesFrom(cart);

        assertTrue(receipt.getDiscounts().isEmpty());
    }

    // --- Offer Tests: Testujemy system przez klasę Teller ---

    @Test
    void handleThreeForTwoOffer() {
        // Konfiguracja oferty w Teller
        teller.addSpecialOffer(SpecialOfferType.THREE_FOR_TWO, toothbrush, 0);

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(toothbrush, 4.0); // 4 units @ 1.50 each

        Receipt receipt = teller.checksOutArticlesFrom(cart);

        // Oczekiwana logika: Discount should be 1 item's price: 1.50
        List<Discount> discounts = receipt.getDiscounts();
        assertEquals(1, discounts.size());
        assertEquals(-1.50, discounts.get(0).getDiscountAmount(), 0.001);
        assertEquals("3 for 2", discounts.get(0).getDescription());
    }

    @Test
    void handleThreeForTwoOfferWithLessThanThree() {
        teller.addSpecialOffer(SpecialOfferType.THREE_FOR_TWO, toothbrush, 0);

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(toothbrush, 2.0); // 2 units @ 1.50 each

        Receipt receipt = teller.checksOutArticlesFrom(cart);

        assertTrue(receipt.getDiscounts().isEmpty());
    }

    @Test
    void handleTenPercentDiscountOffer() {
        // Konfiguracja oferty w Teller
        teller.addSpecialOffer(SpecialOfferType.TEN_PERCENT_DISCOUNT, apples, 10.0);

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(apples, 2.5); // 2.5 kg @ 2.00 per kg = 5.00 full price

        Receipt receipt = teller.checksOutArticlesFrom(cart);

        // Oczekiwana logika: Discount: 10% of 5.00 = 0.50
        List<Discount> discounts = receipt.getDiscounts();
        assertEquals(1, discounts.size());
        assertEquals(-0.50, discounts.get(0).getDiscountAmount(), 0.001);
        assertEquals("10.0% off", discounts.get(0).getDescription());
    }

    @Test
    void handleTwoForAmountOffer() {
        // Konfiguracja oferty w Teller
        teller.addSpecialOffer(SpecialOfferType.TWO_FOR_AMOUNT, toothbrush, 2.00);

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(toothbrush, 3.0); // 3 units @ 1.50 each = 4.50 full price

        Receipt receipt = teller.checksOutArticlesFrom(cart);

        // Oczekiwana logika: Discount: 4.50 - 3.50 = 1.00
        List<Discount> discounts = receipt.getDiscounts();
        assertEquals(1, discounts.size());
        assertEquals(-1.00, discounts.get(0).getDiscountAmount(), 0.001);
        assertEquals("2 for 2.0", discounts.get(0).getDescription());
    }

    @Test
    void handleTwoForAmountOfferWithLessThanTwo() {
        teller.addSpecialOffer(SpecialOfferType.TWO_FOR_AMOUNT, toothbrush, 2.00);

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(toothbrush, 1.0); // 1 unit @ 1.50

        Receipt receipt = teller.checksOutArticlesFrom(cart);

        assertTrue(receipt.getDiscounts().isEmpty());
    }

    @Test
    void handleFiveForAmountOffer() {
        // Konfiguracja oferty w Teller
        teller.addSpecialOffer(SpecialOfferType.FIVE_FOR_AMOUNT, toothbrush, 5.00);

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(toothbrush, 7.0); // 7 units @ 1.50 each = 10.50 full price

        Receipt receipt = teller.checksOutArticlesFrom(cart);

        // Oczekiwana logika: Discount: 10.50 - 8.00 = 2.50
        List<Discount> discounts = receipt.getDiscounts();
        assertEquals(1, discounts.size());
        assertEquals(-2.50, discounts.get(0).getDiscountAmount(), 0.001);
        assertEquals("5 for 5.0", discounts.get(0).getDescription());
    }

    @Test
    void handleFiveForAmountOfferWithLessThanFive() {
        teller.addSpecialOffer(SpecialOfferType.FIVE_FOR_AMOUNT, toothbrush, 5.00);

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(toothbrush, 4.0);

        Receipt receipt = teller.checksOutArticlesFrom(cart);

        assertTrue(receipt.getDiscounts().isEmpty());
    }

    @Test
    void handleOffersWithNoItemsInCart() {
        teller.addSpecialOffer(SpecialOfferType.THREE_FOR_TWO, toothbrush, 0);
        teller.addSpecialOffer(SpecialOfferType.TEN_PERCENT_DISCOUNT, apples, 10.0);

        ShoppingCart cart = new ShoppingCart();

        Receipt receipt = teller.checksOutArticlesFrom(cart);

        assertTrue(receipt.getDiscounts().isEmpty());
    }

    @Test
    void handleOffersWithMultipleProductsAndOffers() {
        // Konfiguracja obu ofert w Teller
        teller.addSpecialOffer(SpecialOfferType.THREE_FOR_TWO, toothbrush, 0);
        teller.addSpecialOffer(SpecialOfferType.TEN_PERCENT_DISCOUNT, apples, 10.0);

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(toothbrush, 3.0); // 3 units @ 1.50 each
        cart.addItemQuantity(apples, 5.0);      // 5 kg @ 2.00 per kg

        Receipt receipt = teller.checksOutArticlesFrom(cart);

        List<Discount> discounts = receipt.getDiscounts();
        assertEquals(2, discounts.size());

        // Toothbrush discount: 1.50
        Discount toothbrushDiscount = discounts.stream()
                .filter(d -> d.getDescription().equals("3 for 2"))
                .findFirst()
                .orElse(null);
        assertNotNull(toothbrushDiscount);
        assertEquals(-1.50, toothbrushDiscount.getDiscountAmount(), 0.001);

        // Apples discount: 1.00
        Discount applesDiscount = discounts.stream()
                .filter(d -> d.getDescription().equals("10.0% off"))
                .findFirst()
                .orElse(null);
        assertNotNull(applesDiscount);
        assertEquals(-1.00, applesDiscount.getDiscountAmount(), 0.001);
    }
}