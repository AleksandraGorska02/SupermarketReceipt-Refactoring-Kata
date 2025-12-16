package dojo.supermarket.model;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReceiptTest {

    private final Product toothbrush = new Product("Toothbrush", ProductUnit.EACH);
    private final Product cheese = new Product("Cheese", ProductUnit.KILO);

    @Test
    void emptyReceiptTotalPriceIsZero() {
        Receipt receipt = new Receipt();
        assertEquals(0.0, receipt.getTotalPrice(), 0.001);
    }

    @Test
    void totalPriceCalculatedFromItems() {
        Receipt receipt = new Receipt();
        // Toothbrush: 1 unit @ 1.50 = 1.50
        receipt.addProduct(toothbrush, 1.0, 1.50, 1.50);
        // Cheese: 0.5 Kilo @ 10.00 = 5.00
        receipt.addProduct(cheese, 0.5, 10.00, 5.00);

        assertEquals(6.50, receipt.getTotalPrice(), 0.001);
    }

    @Test
    void totalPriceCalculatedWithDiscounts() {
        Receipt receipt = new Receipt();
        // Item: 1.0 * 2.00 = 2.00
        receipt.addProduct(toothbrush, 1.0, 2.00, 2.00);
        // Item: 2.0 * 3.00 = 6.00
        receipt.addProduct(cheese, 2.0, 3.00, 6.00);

        receipt.addDiscount(new Discount(toothbrush, "1 dollar off", -1.00));
        // Discount 2: -0.50
        receipt.addDiscount(new Discount(cheese, "50 cents off", -0.50));

        // Expected Total: (2.00 + 6.00) + (-1.00 + -0.50) = 8.00 - 1.50 = 6.50
        assertEquals(6.50, receipt.getTotalPrice(), 0.001);
    }

    @Test
    void getItemsReturnsCorrectList() {
        Receipt receipt = new Receipt();
        receipt.addProduct(toothbrush, 1.0, 1.50, 1.50);

        List<ReceiptItem> items = receipt.getItems();
        assertEquals(1, items.size());
        assertEquals(toothbrush, items.get(0).getProduct());
        assertEquals(1.50, items.get(0).getTotalPrice(), 0.001);
    }

    @Test
    void getDiscountsReturnsCorrectList() {
        Receipt receipt = new Receipt();
        Discount discount = new Discount(toothbrush, "Test", -1.0);
        receipt.addDiscount(discount);

        List<Discount> discounts = receipt.getDiscounts();
        assertEquals(1, discounts.size());
        assertEquals(discount, discounts.get(0));
    }

    @Test
    void addingMultipleItemsAndDiscounts() {
        Receipt receipt = new Receipt();
        receipt.addProduct(toothbrush, 2.0, 1.50, 3.00);
        receipt.addProduct(cheese, 1.0, 5.00, 5.00);
        receipt.addDiscount(new Discount(toothbrush, "Buy one get one free", -1.50));
        receipt.addDiscount(new Discount(cheese, "10% off", -0.50));

        // Total: (3.00 + 5.00) + (-1.50 - 0.50) = 8.00 - 2.00 = 6.00
        assertEquals(6.00, receipt.getTotalPrice(), 0.001);
    }

    @Test
    void receiptWithNoItemsButWithDiscounts() {
        Receipt receipt = new Receipt();
        receipt.addDiscount(new Discount(toothbrush, "Special discount", -2.00));

        assertEquals(-2.00, receipt.getTotalPrice(), 0.001);
    }

    @Test
    void receiptWithNoDiscountsButWithItems() {
        Receipt receipt = new Receipt();
        receipt.addProduct(cheese, 3.0, 4.00, 12.00);

        assertEquals(12.00, receipt.getTotalPrice(), 0.001);
    }
}