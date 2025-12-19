package dojo.supermarket.model.coupon;

import dojo.supermarket.model.product.Product;

import java.time.LocalDate;

public class Coupon {
    private final Product product;
    private final LocalDate validFrom;
    private final LocalDate validUntil;
    private final double quantityNeeded;
    private final double quantityDiscounted;
    private final double discountFactor;
    private boolean wasUsed = false;

    public Coupon(Product product, LocalDate from, LocalDate until,
                  double needed, double discounted, double factor) {
        this.product = product;
        this.validFrom = from;
        this.validUntil = until;
        this.quantityNeeded = needed;
        this.quantityDiscounted = discounted;
        this.discountFactor = factor;
    }

    public boolean isValid(LocalDate date) {
        return !wasUsed && !date.isBefore(validFrom) && !date.isAfter(validUntil);
    }

    public void markAsUsed() {
        this.wasUsed = true;
    }

    // W klasie Coupon.java
    public double calculateDiscountValue(double cartQuantity, double unitPrice) {
        if (cartQuantity <= this.quantityNeeded) return 0;

        double eligibleItems = Math.min(
                cartQuantity - this.quantityNeeded,
                this.quantityDiscounted
        );
        return eligibleItems * unitPrice * (1.0 - this.discountFactor);
    }

    public Product getProduct() {
        return product;
    }

    public double getQuantityNeeded() {
        return quantityNeeded;
    }

    public double getQuantityDiscounted() {
        return quantityDiscounted;
    }

    public double getDiscountFactor() {
        return discountFactor;
    }
}