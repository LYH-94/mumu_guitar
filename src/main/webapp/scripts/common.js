
//window 表示當前視窗 (網頁)。
//location 表示地址欄 (URL)。
//href 表示 URL 屬性。它會將 = 後面的值附加在原值的後面，而不是覆蓋。
function index() {
    window.location.href='index.do'; //發送 index.do 請求。
}

function logIn_index() {
    window.location.href='./index.do'; //發送 index.do 請求。
}

function logInOut() {
    //獲取當前會員圖示下的文字。("會員登入" 或 "登出")。
    var element = document.getElementById("id_logInOut");
    var text_logInOut = element.textContent;

    //alert(text_logInOut);
    if("會員登入" === text_logInOut){
        window.location.href='index.do?operate='+"logInPage"; //發送調用 logInPage 請求。
    }else{
        window.location.href='index.do?operate='+"logOut"; //發送調用 logOut 請求。
    }
}

function register() {
    window.location.href='index.do?operate='+"registerPage"; //發送 registerPage 請求。
}

function favorite() {
    window.location.href='favorite.do?operate='+"getFavoriteByUserId"; //發送 favorite 請求。
}

function trolley() {
    window.location.href='trolley.do?operate='+"getTrolleyByUserId"; //發送 trolley 請求。
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