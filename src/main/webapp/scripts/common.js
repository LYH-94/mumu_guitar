// window 表示當前視窗 (網頁)。
// location 表示地址欄 (URL)。
// href 表示 URL 屬性。它會將 = 後面的值附加在原值的後面，而不是覆蓋。

// 首頁，展示所有商品。
function index() {
    window.location.href="index.do"; // 發送 index.do 請求。
}

// 首頁，展示木吉他。
function index_woodenGuitar() {
    window.location.href="index.do?classification=木吉他"; // 發送 index.do 請求。
}

// 首頁，展示電吉他。
function index_electricGuitar() {
    window.location.href="index.do?classification=電吉他"; // 發送 index.do 請求。
}

function logInOut() {
    // 獲取當前會員圖示下的文字。("會員登入" 或 "登出")。
    var element = document.getElementById("id_logInOut");
    var text_logInOut = element.textContent;

    if("會員登入" === text_logInOut){
        window.location.href="index.do?operate=logInPage"; // 發送調用 logInPage 請求。
    }else{
        window.location.href="index.do?operate=logOut"; // 發送調用 logOut 請求。
    }
}

function register() {
    window.location.href="index.do?operate=registerPage"; // 發送 registerPage 請求。
}

function favorite() {
    window.location.href="favorite.do?operate=getFavoriteByUserId"; // 發送 favorite 請求。
}

function trolley() {
    window.location.href="trolley.do?operate=getTrolleyByUserId"; // 發送 trolley 請求。
}

function productDescription(id){
    window.location.href="productController.do?operate=productDescription&id="+id;
}

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
            //value.data 可以獲取到服務器端響應的內容。
            if(value.data == "logIn"){
                window.location.href="index.do?operate=logInPage"; // 發送調用 logInPage 請求。
            }else if(value.data == "add_success"){
                alert("商品已成功追蹤!");
            }else if(value.data == "delete_success"){
                alert("商品已移除追蹤!");
            }
        }).catch(function(reason){ // 當服務端響應失敗時(也就是異常)，執行該方法中的匿名函式。
            //reason.response.date 可以獲取到異常訊息的內容。
            console.log(reason.response.data);
        });
}

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
            //value.data 可以獲取到服務器端響應的內容。
            if(value.data == "logIn"){
                window.location.href="index.do?operate=logInPage"; // 發送調用 logInPage 請求。
            }else if(value.data == "add_success"){
                alert("商品已加入購物車!");
            }else if(value.data == "delete_success"){
                alert("商品已從購物車中移除!");
            }
        }).catch(function(reason){ // 當服務端響應失敗時(也就是異常)，執行該方法中的匿名函式。
            //reason.response.date 可以獲取到異常訊息的內容。
            console.log(reason.response.data);
        });
}

/* ============================= 下拉式選單元件 ============================ */
// 取得下拉式選單的元素
const dropdownMenu1 = document.querySelector('#dropdown-menu1');
const dropdownMenu2 = document.querySelector('#dropdown-menu2');

// 在下拉式選單元素上監聽 click 事件
dropdownMenu1.addEventListener('click', (event) => {
  // 取得被點選的選項元素和選項的文字內容
  const selectedOption = event.target;
  const selectedText = selectedOption.textContent;

  // 將選項文字顯示在指定的元素上
  const selectedValue = document.querySelector('#dropdownMenuButton1');
  selectedValue.textContent = selectedText;
});

dropdownMenu2.addEventListener('click', (event) => {
  // 取得被點選的選項元素和選項的文字內容
  const selectedOption = event.target;
  const selectedText = selectedOption.textContent;

  // 將選項文字顯示在指定的元素上
  const selectedValue = document.querySelector('#dropdownMenuButton2');
  selectedValue.textContent = selectedText;
});

/* ========================================================= */