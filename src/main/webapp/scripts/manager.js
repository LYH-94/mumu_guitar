// 根據用戶狀態改變顯示的顏色。
const js_status = document.querySelectorAll('.js_status');
js_status.forEach(status => {
    if(status.textContent == "正常"){
        status.style.color = "blue";
    }else{
        status.style.color = "red";
    }
});

// 跳轉至管理員訂單頁 manager_order.html。
function manager_orderPage() {
    const searchOrder="reset";
    window.location.href="orderController.do?operate=getAllOrderList&searchOrder=" + searchOrder;
}

// 跳轉至管理員會員頁 manager_member.html。
function manager_memberPage() {
    const searchMember="reset";
    window.location.href="index.do?operate=getAllMemberList&searchMember=" + searchMember;
}

// 跳轉至管理員商品頁 manager_product.html。
function manager_productPage() {
    const searchProduct="reset";
    window.location.href="productController.do?operate=getAllProductList&searchProduct=" + searchProduct;
}

// 切換用戶狀態。
function switchStatus(id) {
    var element = document.getElementById(id);
    var status = element.textContent;

    // 彈出警告框。
    const isConfirmed = confirm("您確定要切換該用戶的狀態嗎 ?");

    // 若按下確定則執行切換。
    if(isConfirmed){
      window.location.href="index.do?operate=switchStatus&userId=" + id + "&status=" + status;
    }
}
