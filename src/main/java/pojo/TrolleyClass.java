package pojo;

import java.util.ArrayList;
import java.util.List;

public class TrolleyClass {
    private User owner;
    private List<Product> product;
    private List<Integer> quantity;
    private List<Integer> subTotal;
    private Integer totalQuantity;
    private Integer totalAmount;

    public TrolleyClass() {
        setProduct(new ArrayList<Product>());
        setQuantity(new ArrayList<Integer>());
        setSubTotal(new ArrayList<Integer>());
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }

    public List<Integer> getQuantity() {
        return quantity;
    }

    public void setQuantity(List<Integer> quantity) {
        this.quantity = quantity;
    }

    public List<Integer> getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(List<Integer> subTotal) {
        this.subTotal = subTotal;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
}
