package dojo.supermarket.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void productCreationAndGetters() {
        Product apples = new Product("Apples", ProductUnit.KILO);
        assertEquals("Apples", apples.getName());
        assertEquals(ProductUnit.KILO, apples.getUnit());
    }

    @Test
    void productEquality() {
        Product apples1 = new Product("Apples", ProductUnit.KILO);
        Product apples2 = new Product("Apples", ProductUnit.KILO);
        Product bananas = new Product("Bananas", ProductUnit.EACH);
        Product applesEach = new Product("Apples", ProductUnit.EACH);

        // Same product
        assertEquals(apples1, apples2);


        // Different name
        assertNotEquals(apples1, bananas);

        // Different unit
        assertNotEquals(apples1, applesEach);

        // Null and different class
        assertNotEquals(null, apples1);
        assertNotEquals(apples1, new Object());
    }

    @Test
    void productHashCode() {
        Product apples1 = new Product("Apples", ProductUnit.KILO);
        Product apples2 = new Product("Apples", ProductUnit.KILO);
        Product bananas = new Product("Bananas", ProductUnit.EACH);

        assertEquals(apples1.hashCode(), apples2.hashCode());
        assertNotEquals(apples1.hashCode(), bananas.hashCode());
    }


    @Test
    void productWithSpecialCharacters() {
        Product specialProduct = new Product("Café-au-lait!", ProductUnit.EACH);
        assertEquals("Café-au-lait!", specialProduct.getName());
        assertEquals(ProductUnit.EACH, specialProduct.getUnit());
    }

    @Test
    void productWithEmptyName() {
        Product emptyNameProduct = new Product("", ProductUnit.KILO);
        assertEquals("", emptyNameProduct.getName());
        assertEquals(ProductUnit.KILO, emptyNameProduct.getUnit());
    }

    @Test
    void productWithNullName() {
        Product nullNameProduct = new Product(null, ProductUnit.EACH);
        assertNull(nullNameProduct.getName());
        assertEquals(ProductUnit.EACH, nullNameProduct.getUnit());
    }
}