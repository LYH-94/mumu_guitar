// 返回至 "我的訂單" 頁面。
function goBack() {
    window.location.href="orderController.do?operate=getUserOrderList&searchOrder=reset";
}