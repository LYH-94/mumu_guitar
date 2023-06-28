package bo.impl;

import bo.ProductService;
import bo.exception.ProductServiceImplException;
import controllers.exception.ProductControllerImplException;
import dao.impl.ProductDAOImpl;
import pojo.Product;
import util.ConnUtils;

import java.util.List;

public class ProductServiceImpl implements ProductService {

    private ProductDAOImpl productDAO = null;

    @Override
    public List<Product> getAllProduct() throws ProductServiceImplException {
        try {
            return productDAO.getAllProduct(ConnUtils.getConn(), Product.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductControllerImplException("ProductServiceImpl 的 getAllProduct() 有問題。");
        }
    }

    @Override
    public Product getProductById(int id) throws ProductServiceImplException {
        try {
            return productDAO.getProductById(ConnUtils.getConn(), Product.class , id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductControllerImplException("ProductServiceImpl 的 getProductById() 有問題。");
        }
    }
}
