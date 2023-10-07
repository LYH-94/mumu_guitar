package bo.impl;

import bo.ProductService;
import bo.exception.ProductServiceImplException;
import controllers.impl.FavoriteControllerImpl;
import controllers.impl.TrolleyControllerImpl;
import dao.impl.ProductDAOImpl;
import pojo.OrderProduct;
import pojo.Product;
import pojo.ProductAddedFavoAndTrolInfo;
import pojo.User;
import util.ConnUtils;
import util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class ProductServiceImpl implements ProductService {

    private FavoriteControllerImpl favoriteController = null;
    private TrolleyControllerImpl trolleyController = null;
    private ProductDAOImpl productDAO = null;

    /**
     * 獲取所有正常販售的商品。
     *
     * @return
     * @throws ProductServiceImplException
     */
    @Override
    public List<Product> getAllProduct() throws ProductServiceImplException {
        try {
            return productDAO.getAllProduct(ConnUtils.getConn(), Product.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductServiceImplException("ProductServiceImpl 的 getAllProduct() 有問題。");
        }
    }

    /**
     * 通過訂單 id 獲取商品。
     *
     * @param orderId
     * @return
     * @throws ProductServiceImplException
     */
    @Override
    public List<OrderProduct> getProductByOrderId(int orderId) throws ProductServiceImplException {
        try {
            return productDAO.getProductByOrderId(ConnUtils.getConn(), OrderProduct.class, orderId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductServiceImplException("ProductServiceImpl 的 getProductByOrderNumber() 有問題。");
        }
    }

    /**
     * 通過 Id 獲取商品。
     *
     * @param id
     * @return
     * @throws ProductServiceImplException
     */
    @Override
    public Product getProductById(int id) throws ProductServiceImplException {
        try {
            return productDAO.getProductById(ConnUtils.getConn(), Product.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductServiceImplException("ProductServiceImpl 的 getProductById() 有問題。");
        }
    }

    /**
     * 獲取過濾後的商品。
     *
     * @param req
     * @return
     * @throws ProductServiceImplException
     */
    @Override
    public List<Product> getFilteredProduct(HttpServletRequest req) throws ProductServiceImplException {
        try {
            HttpSession session = req.getSession();

            // 1.獲取用戶選擇商品的分類。
            String classification = req.getParameter("classification");
            String session_classification = (String) session.getAttribute("session_classification");

            // 先以 req 請求為主進行判斷，若為空才根據 session 來判斷。
            if (StringUtils.isEmpty(classification)) {
                // 預設進入商品頁面時，是顯示所有商品。
                if (StringUtils.isEmpty(session_classification)) {
                    session.setAttribute("session_classification", "所有商品");

                    // 在數據庫 t_product 表中，商品有自己的類型，不會出現 "所有商品" 這個類，
                    // 因此使用空字串並寫成 SQL 中模糊匹配的寫法"%%"，表示所有商品。
                    classification = "";
                } else {
                    if ("所有商品".equals(session_classification)) {
                        classification = "";
                    } else {
                        classification = session_classification;
                    }
                }
            } else if ("所有商品".equals(classification)) {
                session.setAttribute("session_classification", classification);
                classification = "";
            } else {
                session.setAttribute("session_classification", classification);
            }

            // 2.獲取篩選表單中的數據。
            int lowest_price = 0;
            int highest_price = 999999;
            byte inventory = 2; // 後續利用 if 判斷要執行的 SQL。
            String searchProduct = "";

            // 最低價格 lowest_price
            String session_lowest_price = (String) session.getAttribute("session_lowest_price");
            if (StringUtils.isEmpty(req.getParameter("lowest_price"))) {
                if (StringUtils.isEmpty(session_lowest_price)) {
                    lowest_price = 0;
                } else {
                    lowest_price = Integer.parseInt(session_lowest_price);
                }
            } else if (Integer.parseInt(req.getParameter("lowest_price")) < 0) {
                lowest_price = 0;
            } else if (Integer.parseInt(req.getParameter("lowest_price")) > 999999) {
                lowest_price = 999999;
            } else {
                lowest_price = Integer.parseInt(req.getParameter("lowest_price"));
            }
            session.setAttribute("session_lowest_price", Integer.toString(lowest_price));

            // 最高價格 highest_price
            String session_highest_price = (String) session.getAttribute("session_highest_price");
            if (StringUtils.isEmpty(req.getParameter("highest_price"))) {
                if (StringUtils.isEmpty(session_highest_price)) {
                    highest_price = 999999;
                } else {
                    highest_price = Integer.parseInt(session_highest_price);
                }
            } else if (Integer.parseInt(req.getParameter("highest_price")) < 0 || Integer.parseInt(req.getParameter("highest_price")) > 999999) {
                highest_price = 999999;
            } else {
                highest_price = Integer.parseInt(req.getParameter("highest_price"));
            }
            session.setAttribute("session_highest_price", Integer.toString(highest_price));

            // 商品庫存 inventory
            String session_inventory = (String) session.getAttribute("session_inventory");
            if (StringUtils.isEmpty(req.getParameter("inventory"))) {
                if (StringUtils.isEmpty(session_inventory)) {
                    inventory = 2;
                } else {
                    inventory = Byte.parseByte(session_inventory);
                }
            } else {
                inventory = Byte.parseByte(req.getParameter("inventory"));
            }
            session.setAttribute("session_inventory", Byte.toString(inventory));

            // 商品搜尋 searchProduct
            String session_searchProduct = (String) session.getAttribute("session_searchProduct");
            if (StringUtils.isEmpty(req.getParameter("searchProduct"))) {
                if (StringUtils.isEmpty(session_searchProduct)) {
                    searchProduct = "";
                } else {
                    searchProduct = session_searchProduct;
                }
            } else if ("reset".equals(req.getParameter("searchProduct"))) {
                searchProduct = "";
            } else {
                searchProduct = req.getParameter("searchProduct");
            }
            session.setAttribute("session_searchProduct", searchProduct);

            // 3.首先獲取用戶選擇的排序方式。1-售價(H-L)、2-售價(L-H)。
            byte sortBy;
            String session_sortBy = (String) session.getAttribute("session_sortBy");
            if (StringUtils.isEmpty(req.getParameter("sortBy"))) { // 預設為 1。
                if (StringUtils.isEmpty(session_sortBy)) {
                    sortBy = 1;
                } else {
                    sortBy = Byte.parseByte(session_sortBy);
                }
            } else {
                sortBy = Byte.parseByte(req.getParameter("sortBy"));
            }
            session.setAttribute("session_sortBy", Byte.toString(sortBy));

            // 4.獲取用戶選擇的頁碼。
            int pageNumber;
            if (StringUtils.isEmpty(req.getParameter("pageNumber"))) {
                // 預設為第一頁。
                pageNumber = 1;
            } else {
                pageNumber = Integer.parseInt(req.getParameter("pageNumber"));
            }
            session.setAttribute("session_pageNumber", Integer.toString(pageNumber));

            // 調用 DAO。
            return productDAO.getFilteredProduct(ConnUtils.getConn(), Product.class, classification, lowest_price, highest_price, inventory, searchProduct, sortBy, pageNumber);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductServiceImplException("ProductServiceImpl 的 getFilteredProduct() 有問題。");
        }
    }

    /**
     * 獲取所有商品。
     *
     * @param req
     * @throws ProductServiceImplException
     */
    @Override
    public void getFilteredAllProduct(HttpServletRequest req) throws ProductServiceImplException {
        try {
            HttpSession session = req.getSession();

            // 商品編號搜尋 searchProductNumber
            String searchProductNumber = "";
            String session_searchProductNumber = (String) session.getAttribute("session_searchProductNumber");
            if (StringUtils.isEmpty(req.getParameter("searchProductNumber"))) {
                if (StringUtils.isEmpty(session_searchProductNumber)) {
                    searchProductNumber = "";
                } else {
                    searchProductNumber = session_searchProductNumber;
                }
            } else if ("reset".equals(req.getParameter("searchProductNumber"))) {
                searchProductNumber = "";
            } else {
                searchProductNumber = req.getParameter("searchProductNumber");
            }
            session.setAttribute("session_searchProductNumber", searchProductNumber);

            // 獲取用戶選擇的頁碼。
            int pageNumber;
            if (StringUtils.isEmpty(req.getParameter("pageNumber"))) {
                // 預設為第一頁。
                pageNumber = 1;
            } else {
                pageNumber = Integer.parseInt(req.getParameter("pageNumber"));
            }
            session.setAttribute("session_pageNumber", Integer.toString(pageNumber));

            // 調用 DAO。
            List<Product> allProductList = productDAO.getFilteredAllProduct(ConnUtils.getConn(), Product.class, searchProductNumber, pageNumber);
            session.setAttribute("allProductList", allProductList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductServiceImplException("ProductServiceImpl 的 getFilteredAllProduct() 有問題。");
        }
    }

    /**
     * 獲取所有商品的數量。
     *
     * @param req
     * @return
     * @throws ProductServiceImplException
     */
    @Override
    public int getFilterProductCount(HttpServletRequest req) throws ProductServiceImplException {
        try {
            HttpSession session = req.getSession();

            // 獲取用戶選擇商品的分類。
            String classification = req.getParameter("classification");
            String session_classification = (String) session.getAttribute("session_classification");

            // 先以 req 請求為主進行判斷，若為空才根據 session 來判斷。
            if (StringUtils.isEmpty(classification)) {
                // 預設進入商品頁面時，是顯示所有商品。
                if (StringUtils.isEmpty(session_classification)) {
                    session.setAttribute("session_classification", "所有商品");

                    // 在數據庫 t_product 表中，商品有自己的類型，不會出現 "所有商品" 這個類，
                    // 因此使用空字串並寫成 SQL 中模糊匹配的寫法"%%"，表示所有商品。
                    classification = "";
                } else {
                    if ("所有商品".equals(session_classification)) {
                        classification = "";
                    } else {
                        classification = session_classification;
                    }
                }
            } else if ("所有商品".equals(classification)) {
                session.setAttribute("session_classification", classification);
                classification = "";
            } else {
                session.setAttribute("session_classification", classification);
            }

            // 獲取篩選表單中的數據。
            int lowest_price = 0;
            int highest_price = 999999;
            byte inventory = 2; // 後續利用 if 判斷要執行的 SQL。
            String searchProduct = "";

            // 最低價格 lowest_price
            String session_lowest_price = (String) session.getAttribute("session_lowest_price");
            if (StringUtils.isEmpty(req.getParameter("lowest_price"))) {
                if (StringUtils.isEmpty(session_lowest_price)) {
                    lowest_price = 0;
                } else {
                    lowest_price = Integer.parseInt(session_lowest_price);
                }
            } else if (Integer.parseInt(req.getParameter("lowest_price")) < 0) {
                lowest_price = 0;
            } else if (Integer.parseInt(req.getParameter("lowest_price")) > 999999) {
                lowest_price = 999999;
            } else {
                lowest_price = Integer.parseInt(req.getParameter("lowest_price"));
            }
            session.setAttribute("session_lowest_price", Integer.toString(lowest_price));

            // 最高價格 highest_price
            String session_highest_price = (String) session.getAttribute("session_highest_price");
            if (StringUtils.isEmpty(req.getParameter("highest_price"))) {
                if (StringUtils.isEmpty(session_highest_price)) {
                    highest_price = 999999;
                } else {
                    highest_price = Integer.parseInt(session_highest_price);
                }
            } else if (Integer.parseInt(req.getParameter("highest_price")) < 0 || Integer.parseInt(req.getParameter("highest_price")) > 999999) {
                highest_price = 999999;
            } else {
                highest_price = Integer.parseInt(req.getParameter("highest_price"));
            }
            session.setAttribute("session_highest_price", Integer.toString(highest_price));

            // 商品庫存 inventory
            String session_inventory = (String) session.getAttribute("session_inventory");
            if (StringUtils.isEmpty(req.getParameter("inventory"))) {
                if (StringUtils.isEmpty(session_inventory)) {
                    inventory = 2;
                } else {
                    inventory = Byte.parseByte(session_inventory);
                }
            } else {
                inventory = Byte.parseByte(req.getParameter("inventory"));
            }
            session.setAttribute("session_inventory", Byte.toString(inventory));

            // 搜尋商品 searchProduct
            String session_searchProduct = (String) session.getAttribute("session_searchProduct");
            if (StringUtils.isEmpty(req.getParameter("searchProduct"))) {
                if (StringUtils.isEmpty(session_searchProduct)) {
                    searchProduct = "";
                } else {
                    searchProduct = session_searchProduct;
                }
            } else if ("reset".equals(req.getParameter("searchProduct"))) {
                searchProduct = "";
            } else {
                searchProduct = req.getParameter("searchProduct");
            }
            session.setAttribute("session_searchProduct", searchProduct);

            // 首先獲取用戶選擇的排序方式。1-售價(H-L)、2-售價(L-H)。
            byte sortBy;
            String session_sortBy = (String) session.getAttribute("session_sortBy");
            if (StringUtils.isEmpty(req.getParameter("sortBy"))) { // 預設為 1。
                if (StringUtils.isEmpty(session_sortBy)) {
                    sortBy = 1;
                } else {
                    sortBy = Byte.parseByte(session_sortBy);
                }
            } else {
                sortBy = Byte.parseByte(req.getParameter("sortBy"));
            }
            session.setAttribute("session_sortBy", Byte.toString(sortBy));

            // 調用 DAO。
            return productDAO.getFilterProductCount(ConnUtils.getConn(), classification, lowest_price, highest_price, inventory, searchProduct);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductServiceImplException("ProductServiceImpl 的 getFilteredProduct() 有問題。");
        }
    }

    /**
     * 獲取所有商品的數量。
     *
     * @param req
     * @throws ProductServiceImplException
     */
    @Override
    public void getFilterAllProductCount(HttpServletRequest req) throws ProductServiceImplException {
        try {
            HttpSession session = req.getSession();

            // 商品編號搜尋 searchProductNumber
            String searchProductNumber = "";
            String session_searchProductNumber = (String) session.getAttribute("session_searchProductNumber");
            if (StringUtils.isEmpty(req.getParameter("searchProductNumber"))) {
                if (StringUtils.isEmpty(session_searchProductNumber)) {
                    searchProductNumber = "";
                } else {
                    searchProductNumber = session_searchProductNumber;
                }
            } else if ("reset".equals(req.getParameter("searchProductNumber"))) {
                searchProductNumber = "";
            } else {
                searchProductNumber = req.getParameter("searchProductNumber");
            }
            session.setAttribute("session_searchProductNumber", searchProductNumber);

            // 調用 DAO 獲取商品總數。
            int productCount = productDAO.getFilterAllProductCount(ConnUtils.getConn(), searchProductNumber);

            // 根據商品總數計算的總頁數。
            int pages;
            if (productCount == 0) {
                pages = 1;
            } else if (productCount % 10 != 0) {
                pages = (productCount / 10) + 1;
            } else {
                pages = productCount / 10;
            }

            req.getSession().setAttribute("productCount", productCount);
            req.getSession().setAttribute("pages", pages);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductServiceImplException("ProductServiceImpl 的 getFilterAllProductCount() 有問題。");
        }
    }

    /**
     * 添加追蹤商品與購物車的訊息。
     *
     * @param productList 商品列表。
     * @param req
     * @return
     * @throws ProductServiceImplException
     */
    @Override
    public List<ProductAddedFavoAndTrolInfo> addFavoAndTrollInfo(List<Product> productList, HttpServletRequest req) throws ProductServiceImplException {
        try {
            // 輸入參數 productList 是根據需求獲取的商品數據 (所以商品或特定類型等等)，但還不包含是否有添加到用戶的追蹤清單或購物車，
            // 因此在該函數中將商品與追蹤與購物車訊息重新包裝成 ProductAddedFavoAndTrolInfo 物件，方便後續在前端時的顯示判斷。

            // 1.創建 List<ProductAddedFavoAndTrolInfo> 用於儲存包裝後的物件。
            List<ProductAddedFavoAndTrolInfo> productAddedFavoAndTrolInfoList = new ArrayList<>();

            // 2.判斷是否為訪客、會員或管理員。
            HttpSession session = req.getSession();

            User user = (User) session.getAttribute("user");
            // user == null 表示是訪客。
            if (user == null) {
                for (int i = 0; i < productList.size(); i++) {
                    ProductAddedFavoAndTrolInfo productAddedFavoAndTrolInfo = new ProductAddedFavoAndTrolInfo();
                    productAddedFavoAndTrolInfo.setProduct(productList.get(i));
                    productAddedFavoAndTrolInfo.setExist_favorite(false);
                    productAddedFavoAndTrolInfo.setExist_trolley(false);

                    productAddedFavoAndTrolInfoList.add(productAddedFavoAndTrolInfo);
                }
                return productAddedFavoAndTrolInfoList;
            } else {
                if ("general".equals(user.getIdentity())) {
                    for (int i = 0; i < productList.size(); i++) {
                        ProductAddedFavoAndTrolInfo productAddedFavoAndTrolInfo = new ProductAddedFavoAndTrolInfo();
                        productAddedFavoAndTrolInfo.setProduct(productList.get(i));

                        // 查詢數據庫中，該用戶是否有追蹤該商品。
                        boolean checkFavorite = favoriteController.checkFavorite(productList.get(i).getId(), user.getId());
                        productAddedFavoAndTrolInfo.setExist_favorite(checkFavorite);

                        // 查詢數據庫中，該用戶是否有將該商品添加到購物車。
                        boolean checkTrolley = trolleyController.checkTrolley(productList.get(i).getId(), user.getId());
                        productAddedFavoAndTrolInfo.setExist_trolley(checkTrolley);

                        productAddedFavoAndTrolInfoList.add(productAddedFavoAndTrolInfo);
                    }
                    return productAddedFavoAndTrolInfoList;
                } else if ("manager".equals(user.getIdentity())) {
                    for (int i = 0; i < productList.size(); i++) {
                        ProductAddedFavoAndTrolInfo productAddedFavoAndTrolInfo = new ProductAddedFavoAndTrolInfo();
                        productAddedFavoAndTrolInfo.setProduct(productList.get(i));
                        productAddedFavoAndTrolInfo.setExist_favorite(false);
                        productAddedFavoAndTrolInfo.setExist_trolley(false);

                        productAddedFavoAndTrolInfoList.add(productAddedFavoAndTrolInfo);
                    }
                    return productAddedFavoAndTrolInfoList;
                } else { // user 不等於 null 也不符合一般會員和管理員，所以也是訪客。
                    for (int i = 0; i < productList.size(); i++) {
                        ProductAddedFavoAndTrolInfo productAddedFavoAndTrolInfo = new ProductAddedFavoAndTrolInfo();
                        productAddedFavoAndTrolInfo.setProduct(productList.get(i));
                        productAddedFavoAndTrolInfo.setExist_favorite(false);
                        productAddedFavoAndTrolInfo.setExist_trolley(false);

                        productAddedFavoAndTrolInfoList.add(productAddedFavoAndTrolInfo);
                    }
                    return productAddedFavoAndTrolInfoList;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductServiceImplException("ProductServiceImpl 的 addFavoAndTrollInfo() 有問題。");
        }
    }

    /**
     * 修改指定 id 的商品。
     *
     * @param id        商品 id。
     * @param number    商品編號。
     * @param photo     商品照片。
     * @param name      商品名稱。
     * @param Introduce 商品介紹。
     * @param brand     商品品牌。
     * @param model     商品型號。
     * @param type      商品類型。
     * @param inventory 商品庫存。
     * @param sales     商品銷量。
     * @param price     商品價格。
     * @throws ProductServiceImplException
     */
    @Override
    public void editProductById(Integer id, String number, String photo, String name, String Introduce, String brand, String model, String type, Integer inventory, Integer sales, Integer price) throws ProductServiceImplException {
        try {
            productDAO.editProductById(ConnUtils.getConn(), id, number, photo, name, Introduce, brand, model, type, inventory, sales, price);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductServiceImplException("ProductServiceImpl 的 editProductById() 有問題。");
        }
    }

    /**
     * 停售指定 id 的商品。
     *
     * @param id     商品 id。
     * @param status 商品販售狀態。
     * @throws ProductServiceImplException
     */
    @Override
    public void stopSaleProductById(Integer id, String status) throws ProductServiceImplException {
        try {
            productDAO.stopSaleProductById(ConnUtils.getConn(), id, status);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductServiceImplException("ProductServiceImpl 的 editProductById() 有問題。");
        }
    }

    /**
     * 新增商品。
     *
     * @param req
     * @throws ProductServiceImplException
     */
    @Override
    public void addProduct(HttpServletRequest req) throws ProductServiceImplException {
        try {
            String number = req.getParameter("productNumber");
            String photo = req.getParameter("productPhoto");
            String name = req.getParameter("productName");
            String introduce = req.getParameter("productIntroduce");
            String brand = req.getParameter("productBrand");
            String model = req.getParameter("productModel");
            String type = req.getParameter("productType");
            Integer price = Integer.valueOf(req.getParameter("productPrice"));
            Integer inventory = Integer.valueOf(req.getParameter("productInventory"));
            Integer sales = Integer.valueOf(req.getParameter("productSales"));

            // 調用 DAO。
            productDAO.addProduct(ConnUtils.getConn(), number, photo, name, introduce, brand, model, type, price, inventory, sales);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProductServiceImplException("ProductServiceImpl 的 addProduct() 有問題。");
        }
    }
}