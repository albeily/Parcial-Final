
function login(){
    let password = window.prompt("Please insert the correct password", "Password");
    let token = "";
    const http = new XMLHttpRequest();
    const url='https://albeily.me/api/rest/form/verify';
    http.open("GET", url);
    http.setRequestHeader("client",password.toString());
    http.send();
    http.onreadystatechange = (e) => {
        token = http.getResponseHeader("Authorization");
        console.log(token);
        localStorage.setItem("token",token);
    }
}

function verify(option){
    if(localStorage.getItem("token") === null || localStorage.getItem("token") === "denied"){
        login();
    }else{
        if(localStorage.getItem("token").toString() !=="denied"){
        switch (option){
            case 1:
                currentPosition();
                break;
            case 2:
                formList();
                break;
        }
    }
} }