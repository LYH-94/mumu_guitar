
// 將購物車中的商品數 +1。
function plus(id, currentQuantity) {
    window.location.href="trolley.do?operate=plusQuantity&productId=" + id + "&currentQuantity=" + currentQuantity;
}

// 將購物車中的商品數 -1。
function reduce(id, currentQuantity) {
    window.location.href="trolley.do?operate=reduceQuantity&productId=" + id + "&currentQuantity=" + currentQuantity;
}

// 移除購物車中的指定商品。
function deleteTrolleyProduct(id) {
    window.location.href="trolley.do?operate=deleteTrolleyProduct&productId=" + id;
}

// 移除購物車中的所有商品。
function clearTrolley() {
    window.location.href="trolley.do?operate=clearTrolley";
}

// 結帳。
function checkout() {
    window.location.href="trolley.do?operate=checkout";
}