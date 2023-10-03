// 若商品為停售狀態則將追蹤與購物車按鈕停用。
const productStatus = document.getElementById('js_productStatus');
const productBtnElements = document.querySelectorAll('.js_productBtn');
productBtnElements.forEach(productBtnElement => {
    //confirm(productStatus.textContent);
    //confirm(productBtnElement.disabled);

    if(productStatus.textContent == "販售狀態：正常"){
        productBtnElement.disabled = false;
    }else{
        productBtnElement.disabled = true;
        productBtnElement.style.backgroundColor = "gray";
    }
});