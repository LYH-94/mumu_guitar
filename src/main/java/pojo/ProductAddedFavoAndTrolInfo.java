package pojo;

public class ProductAddedFavoAndTrolInfo {
    private Product product;
    private boolean exist_favorite = false; // 預設訪客訪問時，為 false。
    private boolean exist_trolley = false; // 預設訪客訪問時，為 false。

    public ProductAddedFavoAndTrolInfo() {
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public boolean isExist_favorite() {
        return exist_favorite;
    }

    public void setExist_favorite(boolean exist_favorite) {
        this.exist_favorite = exist_favorite;
    }

    public boolean isExist_trolley() {
        return exist_trolley;
    }

    public void setExist_trolley(boolean exist_trolley) {
        this.exist_trolley = exist_trolley;
    }
}
