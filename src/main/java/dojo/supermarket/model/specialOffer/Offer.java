package dojo.supermarket.model.specialOffer;

import dojo.supermarket.model.product.Product;
import dojo.supermarket.model.specialOffer.types.SpecialOfferStrategies;

public class Offer {

    private final SpecialOfferType offerType;
    private final Product product;
    private final double argument;

      public Offer(SpecialOfferType offerType, Product product, double argument) {
        this.offerType = offerType;
        this.argument = argument;
        this.product = product;
    }

    public SpecialOfferType getType() {
        return offerType;
    }

    public double getArgument() {
        return argument;
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