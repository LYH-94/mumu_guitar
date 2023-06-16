package pojo;

import java.util.List;

public class Favorite {
    private Integer id;
    private List<Product> productList; //Favorite：Product => 1：n
    private User owner; //Favorite：User => 1：1

    public Favorite() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
