package dojo.supermarket.model.specialOffer;

import dojo.supermarket.model.product.Product;
import dojo.supermarket.model.specialOffer.types.SpecialOfferCalculationStrategy;

public class Offer {

    SpecialOfferType offerType;
    private final Product product;
    double  argument;
    private final SpecialOfferCalculationStrategy strategy;
    private final Bundle bundle;

    public Offer(SpecialOfferType offerType, Product product, double argument, SpecialOfferCalculationStrategy strategy) {
        this.offerType = offerType;
        this.argument = argument;
        this.product = product;
        this.strategy = strategy;
        this.bundle = null;
    }

    public Offer(SpecialOfferType offerType, Bundle bundle, SpecialOfferCalculationStrategy strategy) {
        this.offerType = offerType;
        this.argument = 0.0;
        this.product = null;
        this.strategy = strategy;
        this.bundle = bundle;
    }

    Product getProduct() {
        return product;
    }


    public Bundle getBundle() {
        return bundle;
    }
    public Discount getDiscount(double quantity, double unitPrice) {
          if (strategy == null || offerType == SpecialOfferType.BUNDLE_DISCOUNT) {
            return null;
        }
        return strategy.calculateDiscount(this.product, quantity, unitPrice, this.argument);
    }


}
