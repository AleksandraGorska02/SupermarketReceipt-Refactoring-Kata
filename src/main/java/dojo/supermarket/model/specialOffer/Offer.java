package dojo.supermarket.model.specialOffer;

import dojo.supermarket.model.discount.Discount;
import dojo.supermarket.model.interfaces.SpecialOfferStrategies;
import dojo.supermarket.model.product.Product;

public class Offer {

    private final SpecialOfferType offerType;
    private final Product product;
    private final double argument;

    public Offer(SpecialOfferType offerType, Product product, double argument) {
        this.offerType = offerType;
        this.argument = argument;
        this.product = product;
    }


    public Product getProduct() {
        return product;
    }


    public Discount getDiscount(double quantity, double unitPrice) {
        SpecialOfferStrategies.SingleProductOfferStrategy strategy = offerType.getStrategy();

        if (strategy == null) {
            return null;
        }

        return strategy.calculateDiscount(this.product, quantity, unitPrice, this.argument);
    }
}