// window.location.href="......"
// window 表示當前視窗 (網頁)。
// location 表示地址欄 (URL)。
// href 表示 URL 屬性。它會將 = 後面的值附加在原值的後面，而不是覆蓋。

// 首頁，展示所有商品並將所有設置重置為初始值。
function index() {
    const searchProduct="reset";
    window.location.href="index.do?classification=所有商品&lowest_price=0&highest_price=999999&inventory=2&searchProduct=" + searchProduct + "&sortBy=1";
}

// 首頁，展示木吉他。
function index_woodenGuitar() {
    window.location.href="index.do?classification=木吉他";
}

// 首頁，展示電吉他。
function index_electricGuitar() {
    window.location.href="index.do?classification=電吉他";
}

// 首頁，展示...。
// ...

// 根據元素文本判斷是登入還是登出。
function logInOut() {
    // 獲取當前登入狀態的文本，即會員圖示下的文字。("會員登入" 或 "登出")。
    var element = document.getElementById("id_logInOut");
    var text_logInOut = element.textContent;

    if("會員登入" === text_logInOut){
        window.location.href="index.do?operate=logInPage";
    }else{
        window.location.href="index.do?operate=logOut";
    }
}

// 點擊 "會員註冊" 時發送相應請求。
function register() {
    window.location.href="index.do?operate=registerPage";
}

// 點擊 "商品收藏" 時發送相應請求。
function favorite() {
    const searchProduct="reset";
    window.location.href="favorite.do?operate=getFavoriteByUserId&classification=所有商品&lowest_price=0&highest_price=999999&inventory=2&searchProduct=" + searchProduct + "&sortBy=1";
}

// 點擊 "購物車" 時發送相應請求。
function trolley() {
    window.location.href="trolley.do?operate=getTrolleyByUserId";
}

// 點擊商品時發送相應請求，根據商品 id 來獲取該商品的詳細資訊。
function productDescription(id){
    window.location.href="productController.do?operate=productDescription&id=" + id;
}

// 添加或刪除追蹤的商品。
function add_delete_Favorite(productId){
    axios({
        // 發送異步請求。
        method:"post", // 請求的方式。
        url:"favorite.do", // 發送請求的服務端對象。
        params:{ // 要攜帶的參數。(鍵值對)
            operate:"add_delete_Favorite",
            productId:productId
        }
        }).then(function(value){ // 當服務端成功響應時，執行該方法中的匿名函式。
            // value.data 可以獲取到服務器端響應的內容。
            if(value.data == "logIn"){ // 若接收的內容為 "logIn"，表示用戶尚未登入，因此發送會員登入頁面的請求。
                window.location.href="index.do?operate=logInPage";
            }else if(value.data == "add_success"){
                alert("商品已成功追蹤!");
                window.location.href="index.do";
            }else if(value.data == "delete_success"){
                alert("商品已移除追蹤!");
                window.location.href="index.do";
            }
        }).catch(function(reason){ // 當服務端響應失敗時 (也就是異常)，執行該方法中的匿名函式。
            // reason.response.date 可以獲取到異常訊息的內容。
            console.log(reason.response.data);
        });
}

// 添加或刪除購物車中的商品。
function add_delete_Trolley(productId){
    axios({
        // 發送異步請求。
        method:"post", // 請求的方式。
        url:"trolley.do", // 發送請求的服務端對象。
        params:{ // 要攜帶的參數。(鍵值對)
            operate:"add_delete_Trolley",
            productId:productId
        }
        }).then(function(value){ // 當服務端成功響應時，執行該方法中的匿名函式。
            // value.data 可以獲取到服務器端響應的內容。
            if(value.data == "logIn"){ // 若接收的內容為 "logIn"，表示用戶尚未登入，因此發送會員登入頁面的請求。
                window.location.href="index.do?operate=logInPage"
            }else if(value.data == "add_success"){
                alert("商品已加入購物車!");
                window.location.href="index.do";
            }else if(value.data == "delete_success"){
                alert("商品已從購物車中移除!");
                window.location.href="index.do";
            }
        }).catch(function(reason){ // 當服務端響應失敗時 (也就是異常)，執行該方法中的匿名函式。
            // reason.response.date 可以獲取到異常訊息的內容。
            console.log(reason.response.data);
        });
}

// 再次確認是否提交表單 (送出訂單時用的)
const form = document.getElementById("checkoutForm");

// 添加監聽器來處理表單提交。
form.addEventListener("submit", function(event) {
  // 阻止默認的表單提交行為。
  event.preventDefault();

  // 彈出警告框。
  const isConfirmed = confirm("您確定要送出訂單嗎 ?");

  // 若按下確定則執行提交。
  if(isConfirmed){
    form.submit();
  }
});

/* 下拉式選單元件 ============================================= */
// 取得下拉式選單的元素。
const dropdownMenu1 = document.querySelector('#dropdown-menu1');
const dropdownMenu2 = document.querySelector('#dropdown-menu2');

// 在下拉式選單元素上監聽 click 事件。
dropdownMenu1.addEventListener('click', (event) => {
  // 取得被點選的選項元素和選項的文字內容。
  const selectedOption = event.target;
  const selectedText = selectedOption.textContent;

  // 將選項文字顯示在指定的元素上。
  const selectedValue = document.querySelector('#dropdownMenuButton1');
  selectedValue.textContent = selectedText;
});

dropdownMenu2.addEventListener('click', (event) => {
  // 取得被點選的選項元素和選項的文字內容。
  const selectedOption = event.target;
  const selectedText = selectedOption.textContent;

  // 將選項文字顯示在指定的元素上。
  const selectedValue = document.querySelector('#dropdownMenuButton2');
  selectedValue.textContent = selectedText;
});

/* ========================================================= */