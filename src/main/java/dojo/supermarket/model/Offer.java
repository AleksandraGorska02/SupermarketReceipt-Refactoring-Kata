package dojo.supermarket.model;

import dojo.supermarket.model.specialOfferCalculation.SpecialOfferCalculationStrategy;

public class Offer {

    SpecialOfferType offerType;
    private final Product product;
    public double  argument;
    private final SpecialOfferCalculationStrategy strategy;

    public Offer(SpecialOfferType offerType, Product product, double argument,SpecialOfferCalculationStrategy strategy) {
        this.offerType = offerType;
        this.argument = argument;
        this.product = product;
        this.strategy = strategy;
    }

    Product getProduct() {
        return product;
    }
    public Discount getDiscount(double quantity, double unitPrice) {
        if (strategy == null) {
            return null;
        }
        return strategy.calculateDiscount(this.product, quantity, unitPrice, this);
    }


}
