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
            throw new ProductControllerImplException("ProductDAOImplException 的 getAllProduct() 有問題。");
        }
    }
}
