package dao.impl;

import controllers.exception.ProductControllerImplException;
import dao.BaseDAO;
import dao.ProductDAO;
import dao.exception.ProductDAOImplException;
import pojo.Product;

import java.sql.Connection;
import java.util.List;

public class ProductDAOImpl extends BaseDAO implements ProductDAO {
    @Override
    public List<Product> getAllProduct(Connection conn, Class<Product> clazz) throws ProductDAOImplException {
        try {
            String sql = "SELECT * FROM t_product";
            return super.getForList(conn, clazz, sql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductControllerImplException("ProductDAOImpl 的 getAllProduct() 有問題。");
        }
    }

    @Override
    public List<Product> getProductByType(Connection conn, Class<Product> clazz, String type) throws ProductDAOImplException {
        try {
            String sql = "SELECT * FROM t_product WHERE type = ?";
            return super.getForList(conn, clazz, sql, type);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductControllerImplException("ProductDAOImpl 的 getProductByType() 有問題。");
        }
    }

    @Override
    public Product getProductById(Connection conn, Class<Product> clazz, int id) throws ProductDAOImplException {
        try {
            String sql = "SELECT * FROM t_product WHERE id = ?";
            return super.getInstance(conn, clazz, sql, id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductControllerImplException("ProductDAOImpl 的 getProductById() 有問題。");
        }
    }
}
