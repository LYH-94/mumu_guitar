package pojo;

public class Favorite {
    private Integer id;
    private Product product; //Favorite：Product => 1：1
    private User owner; //Favorite：User => 1：1

    public Favorite() {
    }

    public Favorite(Integer id) {
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
