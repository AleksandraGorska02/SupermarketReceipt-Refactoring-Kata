package dojo.supermarket.model;

import dojo.supermarket.model.product.Product;
import dojo.supermarket.model.product.ProductQuantity;
import dojo.supermarket.model.product.ProductUnit;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductQuantityTest {

    @Test
    void gettersReturnProvidedValues() {
        Product p = new Product("Oranges", ProductUnit.KILO);
        ProductQuantity pq = new ProductQuantity(p, 2.5);

        assertEquals(p, pq.getProduct());
        assertEquals(2.5, pq.getQuantity(), 0.0001);
    }

    @Test
    void quantityCanBeZeroOrNegative() {
        Product p = new Product("Bananas", ProductUnit.KILO);

        ProductQuantity pqZero = new ProductQuantity(p, 0.0);
        assertEquals(0.0, pqZero.getQuantity(), 0.0001);

        ProductQuantity pqNegative = new ProductQuantity(p, -1.5);
        assertEquals(-1.5, pqNegative.getQuantity(), 0.0001);
    }
}

