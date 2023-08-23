package util;

import pojo.Product;

import java.util.Comparator;

public class compareUtils implements Comparator<Product> {
    @Override
    public int compare(Product p1, Product p2){
        if(p1.getPrice() < p2.getPrice())
            return -1;
        else if(p1.getPrice() == p2.getPrice())
            return 0;
        else if(p1.getPrice() > p2.getPrice())
            return 1;
        return 0;
    }
}
