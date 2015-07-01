package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by gili on 6/28/2015.
 */
public class ProductSorter {
    public void SortProductByName(ArrayList<Product> products){
        Collections.sort(products, new Comparator<Product>() {
            @Override
            public int compare(Product lhs, Product rhs) {
                return lhs.getProductName().compareTo(rhs.getProductName());
            }
        });
    }
}
