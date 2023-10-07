package bo.impl;

import bo.FavoriteService;
import bo.exception.FavoriteServiceImplException;
import dao.impl.FavoriteDAOImpl;
import pojo.Favorite;
import pojo.Product;
import util.ConnUtils;
import util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public class FavoriteServiceImpl implements FavoriteService {

    FavoriteDAOImpl favoriteDAO = null;

    /**
     * 獲取指定會員追蹤的商品。
     *
     * @param req
     * @param userId 使用者 id。
     * @return
     * @throws FavoriteServiceImplException
     */
    @Override
    public List<Product> getFilteredFavoriteProductByUserId(HttpServletRequest req, int userId) throws FavoriteServiceImplException {
        try {
            HttpSession session = req.getSession();

            // 1.獲取用戶選擇商品的分類。
            String classification = req.getParameter("classification");
            String session_classification = (String) session.getAttribute("session_classification");

            // 先以 req 請求為主進行判斷，若為空才根據 session 來判斷。
            if (StringUtils.isEmpty(classification)) {
                if (StringUtils.isEmpty(session_classification)) {

                    // 預設進入商品頁面時，是顯示所有商品。
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

            // 庫存狀態 inventory
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

            // 3.首先獲取用戶選擇的排序方式。1-售價(H-L)、2-售價(L-H)。
            byte sortBy;
            String session_sortBy = (String) session.getAttribute("session_sortBy");
            if (StringUtils.isEmpty(req.getParameter("sortBy"))) {
                if (StringUtils.isEmpty(session_sortBy)) {
                    sortBy = 1; // 預設為 1。
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
                pageNumber = 1; // 預設為第一頁。
            } else {
                pageNumber = Integer.parseInt(req.getParameter("pageNumber"));
            }
            session.setAttribute("session_pageNumber", Integer.toString(pageNumber));

            // 調用 DAO。
            return favoriteDAO.getFilteredFavoriteProduct(ConnUtils.getConn(), Product.class, classification, lowest_price, highest_price, inventory, searchProduct, sortBy, pageNumber, userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FavoriteServiceImplException("FavoriteServiceImpl 的 getFilteredFavoriteProductByUserId() 有問題。");
        }
    }

    /**
     * 獲取指定會員追蹤商品的數量。
     *
     * @param req
     * @param userId 使用者 id。
     * @return
     * @throws FavoriteServiceImplException
     */
    @Override
    public int getFilteredFavoriteProductCountByUserId(HttpServletRequest req, int userId) throws FavoriteServiceImplException {
        try {
            HttpSession session = req.getSession();

            // 1.獲取用戶選擇商品的分類。
            String classification = req.getParameter("classification");
            String session_classification = (String) session.getAttribute("session_classification");

            // 先以 req 請求為主進行判斷，若為空才根據 session 來判斷。
            if (StringUtils.isEmpty(classification)) {
                if (StringUtils.isEmpty(session_classification)) {

                    // 預設進入商品頁面時，是顯示所有商品。
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

            // 庫存狀態 inventory
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

            // 3.首先獲取用戶選擇的排序方式。1-售價(H-L)、2-售價(L-H)。
            byte sortBy;
            String session_sortBy = (String) session.getAttribute("session_sortBy");
            if (StringUtils.isEmpty(req.getParameter("sortBy"))) {
                if (StringUtils.isEmpty(session_sortBy)) {
                    sortBy = 1; // 預設為 1。
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
                pageNumber = 1; // 預設為第一頁。
            } else {
                pageNumber = Integer.parseInt(req.getParameter("pageNumber"));
            }
            session.setAttribute("session_pageNumber", Integer.toString(pageNumber));

            // 調用 DAO。
            return favoriteDAO.getFilterFavoriteProductCount(ConnUtils.getConn(), classification, lowest_price, highest_price, inventory, searchProduct, userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FavoriteServiceImplException("FavoriteServiceImpl 的 getFilteredFavoriteProductCountByUserId() 有問題。");
        }
    }

    /**
     * 添加商品到指定會員的商品追蹤表中。
     *
     * @param productId 商品 id
     * @param userId    使用者 id
     * @return
     * @throws FavoriteServiceImplException
     */
    @Override
    public boolean addFavorite(Integer productId, Integer userId) throws FavoriteServiceImplException {
        try {
            // 先判斷該用戶是否已經有追蹤同樣的商品。
            boolean b = favoriteDAO.checkForDuplicateUsers(ConnUtils.getConn(), Favorite.class, productId, userId);

            // 若為不重複為 true，重複則為 false。
            if (b) {
                // 不重複則進行 "添加" 操作。
                return favoriteDAO.addFavorite(ConnUtils.getConn(), productId, userId);
            } else {
                // 若重複則進行 "刪除" 操作。
                favoriteDAO.deleteFavorite(ConnUtils.getConn(), productId, userId);
                return b;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new FavoriteServiceImplException("FavoriteServiceImpl 的 addFavorite() 有問題。");
        }
    }

    /**
     * 用於查詢數據庫中，該用戶是否已經追蹤該商品。
     *
     * @param productId 商品 id。
     * @param userId    使用者 id。
     * @return
     * @throws FavoriteServiceImplException
     */
    @Override
    public boolean checkFavorite(Integer productId, Integer userId) throws FavoriteServiceImplException {
        try {
            return favoriteDAO.checkFavorite(ConnUtils.getConn(), Favorite.class, productId, userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FavoriteServiceImplException("FavoriteServiceImpl 的 checkFavorite() 有問題。");
        }
    }
}