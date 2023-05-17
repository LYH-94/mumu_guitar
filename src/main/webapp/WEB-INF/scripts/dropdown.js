/* My Custom JavaScript */

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