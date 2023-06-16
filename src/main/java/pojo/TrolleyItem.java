package pojo;

public class TrolleyItem {
    private Integer id;
    private Product product; //TrolleyItem：product => n：1
    private Integer quantity;
    private Order owner; //TrolleyItem：User => n：1

    public TrolleyItem() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Order getOwner() {
        return owner;
    }

    public void setOwner(Order owner) {
        this.owner = owner;
    }
}
