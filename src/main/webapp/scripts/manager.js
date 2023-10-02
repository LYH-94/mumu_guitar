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
    const searchProductNumber="reset";
    window.location.href="productController.do?operate=getAllProductList&searchProductNumber=" + searchProductNumber;
}

// 切換用戶狀態。
function switchStatus(id) {
    let element = document.getElementById(id);
    let status = element.textContent;

    // 彈出警告框。
    const isConfirmed = confirm("您確定要切換該用戶的狀態嗎 ?");

    // 若按下確定則執行切換。
    if(isConfirmed){
      window.location.href="index.do?operate=switchStatus&userId=" + id + "&status=" + status;
    }
}

// 修改商品。
function editProduct(id) {
    // 獲取商品各資訊的元素和按鈕。
    let productNumber = document.getElementById("number_id_"+id);
    let productPhotoAndName = document.getElementById("photoAndName_id_"+id);
    let productBrand = document.getElementById("brand_id_"+id);
    let productModel = document.getElementById("model_id_"+id);
    let productType = document.getElementById("type_id_"+id);
    let productPrice = document.getElementById("price_id_"+id);
    let productInventory = document.getElementById("inventory_id_"+id);
    let productSales = document.getElementById("sales_id_"+id);
    let productEditBtn = document.getElementById("editBtn_id_"+id);
    let productStopSaleBtn = document.getElementById("stopSaleBtn_id_"+id);

    let productIntroduce = document.getElementById("number_id_"+id).getAttribute("data-productIntroduce");


    if(productEditBtn.textContent == "修改"){
        // number
        let newInputNumber = document.createElement("input");
        newInputNumber.type = "number";
        newInputNumber.min= "0";
        newInputNumber.max= "999999";
        newInputNumber.name = "number";
        newInputNumber.style = "width: 100px";
        newInputNumber.value = productNumber.textContent;
        productNumber.textContent = ""; // 將原先的文字清除。
        productNumber.appendChild(newInputNumber); // 將子元素添加到父元素中。

        // PhotoAndName
        let childElements = productPhotoAndName.children; // 獲取 productPhotoAndName 元素中的所有子元素。

        // 利用迴圈遍歷
        for (let i = 0; i < childElements.length; ++i) {
            if(i == 0){ // photo
                // 分割字串。將圖片檔案名稱分割出來。
                let fileName = childElements[i].src.split('/').pop();

                let newInputPhoto = document.createElement("input");
                newInputPhoto.type = "text";
                newInputPhoto.name = "photo";
                newInputPhoto.style = "width: 100px";
                newInputPhoto.value = fileName;

                // 替換子元素。
                productPhotoAndName.replaceChild(newInputPhoto, childElements[i]);
            } else { // name
                // 創建商品名稱的輸入框。
                let newInputName = document.createElement("input");
                newInputName.type = "text";
                newInputName.name = "name";
                newInputName.style = "width: 200px";
                newInputName.value = childElements[i].textContent;


                // 創建商品說明的輸入框。
                let newInputIntroduce = document.createElement("input");
                newInputIntroduce.type = "textarea";
                newInputIntroduce.name = "introduce";
                newInputIntroduce.style = "width: 200px";
                newInputIntroduce.value = productIntroduce;

                // 添加到父元素中。
                childElements[i].textContent = ""; // 將原先的文字清除。
                childElements[i].appendChild(newInputName); // 將子元素添加到父元素中。
                childElements[i].appendChild(newInputIntroduce); // 將子元素添加到父元素中。
            }
        }

        // brand
        let newInputBrand = document.createElement("input");
        newInputBrand.type = "text";
        newInputBrand.name = "brand";
        newInputBrand.style = "width: 70px";
        newInputBrand.value = productBrand.textContent;
        productBrand.textContent = ""; // 將原先的文字清除。
        productBrand.appendChild(newInputBrand); // 將子元素添加到父元素中。

        // type
        let newInputType = document.createElement("input");
        newInputType.type = "text";
        newInputType.name = "type";
        newInputType.style = "width: 70px";
        newInputType.value = productType.textContent;
        productType.textContent = ""; // 將原先的文字清除。
        productType.appendChild(newInputType); // 將子元素添加到父元素中。

        // model
        let newInputModel = document.createElement("input");
        newInputModel.type = "text";
        newInputModel.name = "model";
        newInputModel.style = "width: 70px";
        newInputModel.value = productModel.textContent;
        productModel.textContent = ""; // 將原先的文字清除。
        productModel.appendChild(newInputModel); // 將子元素添加到父元素中。

        // price
        let newInputPrice = document.createElement("input");
        newInputPrice.type = "number";
        newInputPrice.min= "0";
        newInputPrice.max= "999999";
        newInputPrice.name = "price";
        newInputPrice.style = "width: 70px";
        newInputPrice.value = productPrice.textContent;
        productPrice.textContent = ""; // 將原先的文字清除。
        productPrice.appendChild(newInputPrice); // 將子元素添加到父元素中。

        // inventory
        let newInputInventory = document.createElement("input");
        newInputInventory.type = "number";
        newInputInventory.min= "0";
        newInputInventory.max= "999999";
        newInputInventory.name = "inventory";
        newInputInventory.style = "width: 30px";
        newInputInventory.value = productInventory.textContent;
        productInventory.textContent = ""; // 將原先的文字清除。
        productInventory.appendChild(newInputInventory); // 將子元素添加到父元素中。

        // sales
        let newInputSales = document.createElement("input");
        newInputSales.type = "number";
        newInputSales.min= "0";
        newInputSales.max= "999999";
        newInputSales.name = "sales";
        newInputSales.style = "width: 50px";
        newInputSales.value = productSales.textContent;
        productSales.textContent = ""; // 將原先的文字清除。
        productSales.appendChild(newInputSales); // 將子元素添加到父元素中。

        // editBtn
        productEditBtn.textContent = "確定"; // 修改 => 確定

        // stopSaleBtn
        productStopSaleBtn.textContent = "取消"; // 刪除 => 取消

    }else if(productEditBtn.textContent == "確定"){
        // 彈出警告框。
        const isConfirmed = confirm("您確定要修改嗎 ?");

        let pNumber = productNumber.children[0].value;
        let pPhoto = productPhotoAndName.children[0].value;
        let pName = productPhotoAndName.children[1].children[0].value;
        let pIntroduce = productPhotoAndName.children[1].children[1].value;
        let pBrand = productBrand.children[0].value;
        let pModel = productModel.children[0].value;
        let pType = productType.children[0].value;
        let pPrice = productPrice.children[0].value;
        let pInventory = productInventory.children[0].value;
        let pSales = productSales.children[0].value;
        // 若按下確定則執行修改。
        if(isConfirmed){
            window.location.href="productController.do?operate=editProductById&id=" + id + "&number=" + pNumber + "&photo=" + pPhoto + "&name=" + pName + "&Introduce=" + pIntroduce + "&brand=" + pBrand + "&type=" + pType + "&model=" + pModel + "&price=" + pPrice + "&inventory=" + pInventory + "&sales=" + pSales;
        }
    }
}

// 停售或取消修改商品。
function stopSaleProduct(id) {
    //獲取按鈕。
    let productStopSaleBtn = document.getElementById("stopSaleBtn_id_"+id);

    if(productStopSaleBtn.textContent == "取消"){
        manager_productPage();
    }else if(productStopSaleBtn.textContent == "停售"){
        // 彈出警告框。
        const isConfirmed = confirm("您確定要停售此商品嗎 ?");

        // 若按下確定則停售。
        if(isConfirmed){
            window.location.href="productController.do?operate=stopSaleProductById&id=" + id + "&status=正常" ;
        }
    }else if(productStopSaleBtn.textContent == "正常"){
        // 彈出警告框。
        const isConfirmed = confirm("您確定要販售此商品嗎 ?");

        // 若按下確定則停售。
        if(isConfirmed){
            window.location.href="productController.do?operate=stopSaleProductById&id=" + id + "&status=停售" ;
        }
    }
}

// 顯示新增商品的表單。
let addProductDivElement = document.getElementById("addProductDiv");
let addProductBtnElement = document.getElementById("addProductBtn");

function showAddProductForm() {
    if (addProductDivElement.hidden == true) {
        addProductDivElement.hidden = false;
        addProductBtnElement.textContent = "取消新增"
    } else {
        addProductDivElement.hidden = true;
        addProductBtnElement.textContent = "新增商品"
    }
}

// 再次確認是否提交表單 (新增商品時用的)
const addProductForm = document.getElementById("addProductForm");

// 添加監聽器來處理表單提交。
addProductForm.addEventListener("submit", function(event) {
  // 阻止默認的表單提交行為。
  event.preventDefault();

  // 彈出警告框。
  const isConfirmed = confirm("您確定要新增嗎 ?");

  // 若按下確定則執行提交。
  if(isConfirmed){
    addProductForm.submit();
  }
});
