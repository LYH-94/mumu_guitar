// 首頁，展示所有商品並將所有設置重置為初始值。
function favorite_index() {
    const searchProduct="reset";
    window.location.href="favorite.do?operate=getFavoriteByUserId&classification=所有商品&lowest_price=0&highest_price=999999&inventory=2&searchProduct=" + searchProduct + "&sortBy=1";
}

// 首頁，展示木吉他。
function favorite_index_woodenGuitar() {
    window.location.href="favorite.do?operate=getFavoriteByUserId&classification=木吉他";
}

// 首頁，展示電吉他。
function favorite_index_electricGuitar() {
    window.location.href="favorite.do?operate=getFavoriteByUserId&classification=電吉他";
}

// 首頁，展示...。
// ...