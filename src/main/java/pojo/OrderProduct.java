package pojo;

public class OrderProduct {
    private Integer id;
    private Product product; //OrderProduct：Product => n：1
    private Integer quantity;
    private Order belongOrder; //OrderProduct：Order => n：1

    public OrderProduct() {
    }

    public OrderProduct(Integer id) {
        this.id = id;
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

    public Order getBelongOrder() {
        return belongOrder;
    }

    public void setBelongOrder(Order belongOrder) {
        this.belongOrder = belongOrder;
    }
}
