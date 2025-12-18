package dojo.supermarket.model.receipt;

import dojo.supermarket.model.SupermarketConfig;
import dojo.supermarket.model.discount.Discount;
import dojo.supermarket.model.product.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Receipt {

    private final List<ReceiptItem> items = new ArrayList<>();
    private final List<Discount> discounts = new ArrayList<>();
    private double pointsEarned;

    public double getTotalPrice() {
        double total = 0.0;
        for (ReceiptItem item : items) {
            total += item.getTotalPrice();
        }
        for (Discount discount : discounts) {
            total += discount.getDiscountAmount();
        }
        return total;
    }

    public void addProduct(Product p, double quantity, double price, double totalPrice) {
        items.add(new ReceiptItem(p, quantity, price, totalPrice));
    }

    public void setPointsEarned(double totalAmount) {
        double multiplier = SupermarketConfig.getProperty("loyalty.points.per.currency", 1.0);
        this.pointsEarned = Math.floor(totalAmount * multiplier);
    }

    public double getPointsEarned() {
        return pointsEarned;
    }

    public List<ReceiptItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void addDiscount(Discount discount) {
        discounts.add(discount);
    }

    public List<Discount> getDiscounts() {
        return discounts;
    }
}
