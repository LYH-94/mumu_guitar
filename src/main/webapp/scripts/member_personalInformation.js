// 將所有<input>元素的value值儲存起來，用於用戶修改資料但取消時的重新賦值。
let inputValues = [];
const inputElements = document.querySelectorAll('.js_inputElement');
inputElements.forEach(inputElement => {
    if(inputElement.type == "radio"){
        if(inputElement.checked == true){
            inputValues.push(true);
        }else{
            inputValues.push(false);
        }
    }else{
        inputValues.push(inputElement.value);
    }
});

// 獲取from表單中button元素。
const btnElements = document.querySelectorAll('.js_personalInfo_btn');

function editPersonalInfo() {
    // 設置from表單中所有<input>元素為可用。預設皆為不可用。
    inputElements.forEach(inputElement => {
        inputElement.disabled = !inputElement.disabled;
    });

    // 設置from表單中button元素為隱藏or不隱藏。預設修改為不隱藏、取消與確定為隱藏。
    btnElements.forEach(btnElement => {
        btnElement.hidden = !btnElement.hidden;
    });
}

function cancelEdit(){
    // 將事先儲存的用戶資料重新賦值並設置from表單中所有<input>元素為不可用。
    inputElements.forEach((inputElement, index) => {
        let value = inputValues[index];
        if(inputElement.type == "radio"){
            if(inputElement.checked != value){
                inputElement.checked = !inputElement.checked;
            }
        }else{
            inputElement.value = value;
        }

        inputElement.disabled = !inputElement.disabled;
    });

    // 設置from表單中button元素為隱藏or不隱藏。
    btnElements.forEach(btnElement => {
        btnElement.hidden = !btnElement.hidden;
    });
}