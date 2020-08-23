var webSocket;
const timeout = 5000;

setInterval(reconnect, timeout);

$(document).ready(function () {
    connect();
});

function connect() {
    webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/admin");

    webSocket.onopen = function (e) {
        console.log("Connected");

        const command = {
            method: "LIST",
        };

        webSocket.send(JSON.stringify(["FORM", command]));
        webSocket.send(JSON.stringify(["USER", command]));
    };

    webSocket.onclose = function (e) {
        console.log("Disconnected");
    };

    webSocket.onmessage = function (message) {
        const data = JSON.parse(message.data);
        const info = data.command.entity;

        if (data.type === "FORM") {
            printForms(info);
            createLocations(info);
        } else if (data.type === "USER") {
            printUsers(info);
        }
    }

    webSocket.onerror = function (e) {
        console.log("Error");
    }
}

function reconnect() {
    if (!webSocket || webSocket.readyState == 3) {
        connect();
    }
}

function printUsers(users) {

    const table = document.createElement("table");
    let rowTable;
    let username, name, lastname;

    table.className = "table table-sm table-hover";
    table.style.width = "100%";
    table.isContentEditable = false;

    for (let i in users) {
        rowTable = table.insertRow();

        username = rowTable.insertCell();
        username.textContent = users[i].username;
        username.style.fontWeight = "bold";

        name = rowTable.insertCell();
        name.textContent = users[i].person.name;

        lastname = rowTable.insertCell();
        lastname.textContent = users[i].person.lastname;

        const open = rowTable.insertCell();
        const btnOpen = document.createElement("button");
        btnOpen.className = "btn btn-success";
        btnOpen.innerHTML = '<svg  width="20px" height="20px" viewBox="0 0 16 16" class="bi bi-folder2-open mb-1 text-white" fill="currentColor" xmlns="http://www.w3.org/2000/svg">\n' +
            '    <path fill-rule="evenodd" d="M1 3.5A1.5 1.5 0 0 1 2.5 2h2.764c.958 0 1.76.56 2.311 1.184C7.985 3.648 8.48 4 9 4h4.5A1.5 1.5 0 0 1 15 5.5v.64c.57.265.94.876.856 1.546l-.64 5.124A2.5 2.5 0 0 1 12.733 15H3.266a2.5 2.5 0 0 1-2.481-2.19l-.64-5.124A1.5 1.5 0 0 1 1 6.14V3.5zM2 6h12v-.5a.5.5 0 0 0-.5-.5H9c-.964 0-1.71-.629-2.174-1.154C6.374 3.334 5.82 3 5.264 3H2.5a.5.5 0 0 0-.5.5V6zm-.367 1a.5.5 0 0 0-.496.562l.64 5.124A1.5 1.5 0 0 0 3.266 14h9.468a1.5 1.5 0 0 0 1.489-1.314l.64-5.124A.5.5 0 0 0 14.367 7H1.633z"/>\n' +
            '</svg>';
        btnOpen.type = "button";
        btnOpen.id = users[i].username;
        btnOpen.onclick = function () {
            viewUser(users[i].username, users[i].password, users[i].person.name, users[i].person.lastname, users[i].role);
        };
        open.appendChild(btnOpen);

        const erase = rowTable.insertCell();
        const btnErase = document.createElement("button");
        btnErase.className = "btn btn-danger";
        btnErase.type = "button";
        btnErase.disabled = true;
        btnErase.innerHTML = '<svg  width="20px" height="20px" viewBox="0 0 16 16" class="bi bi-trash text-white mb-1" fill="currentColor" xmlns="http://www.w3.org/2000/svg">\n' +
            '    <path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6z"/>\n' +
            '    <path fill-rule="evenodd" d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1zM4.118 4L4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z"/>\n' +
            '</svg>';
        btnErase.id = users[i].username;
        btnErase.onclick = function () {
            eraseUser(users[i].username);
        };
        erase.appendChild(btnErase);
    }

    document.getElementById("users").innerHTML = "";
    document.getElementById("users").appendChild(table);
}

function printForms(forms) {

    const table = document.createElement("table");
    let rowTable;
    let id, name, lastname, sector, education, username;

    table.className = "table table-sm table-hover";
    table.style.width = "100%";
    table.isContentEditable = false;

    for (let i in forms) {
        rowTable = table.insertRow();

        id = rowTable.insertCell();
        id.textContent = forms[i].id;
        id.style.fontWeight = "bold";

        name = rowTable.insertCell();
        name.textContent = forms[i].person.name;

        lastname = rowTable.insertCell();
        lastname.textContent = forms[i].person.lastname;

        const open = rowTable.insertCell();
        const btnOpen = document.createElement("button");
        btnOpen.className = "btn btn-success";
        btnOpen.innerHTML = '<svg  width="20px" height="20px" viewBox="0 0 16 16" class="bi bi-folder2-open mb-1 text-white" fill="currentColor" xmlns="http://www.w3.org/2000/svg">\n' +
            '    <path fill-rule="evenodd" d="M1 3.5A1.5 1.5 0 0 1 2.5 2h2.764c.958 0 1.76.56 2.311 1.184C7.985 3.648 8.48 4 9 4h4.5A1.5 1.5 0 0 1 15 5.5v.64c.57.265.94.876.856 1.546l-.64 5.124A2.5 2.5 0 0 1 12.733 15H3.266a2.5 2.5 0 0 1-2.481-2.19l-.64-5.124A1.5 1.5 0 0 1 1 6.14V3.5zM2 6h12v-.5a.5.5 0 0 0-.5-.5H9c-.964 0-1.71-.629-2.174-1.154C6.374 3.334 5.82 3 5.264 3H2.5a.5.5 0 0 0-.5.5V6zm-.367 1a.5.5 0 0 0-.496.562l.64 5.124A1.5 1.5 0 0 0 3.266 14h9.468a1.5 1.5 0 0 0 1.489-1.314l.64-5.124A.5.5 0 0 0 14.367 7H1.633z"/>\n' +
            '</svg>';
        btnOpen.type = "button";
        btnOpen.id = forms[i].id;
        btnOpen.onclick = function () {
            viewForm(forms[i].user.username, forms[i].person.name, forms[i].person.lastname, forms[i].sector, forms[i].education);
        };
        open.appendChild(btnOpen);

        const erase = rowTable.insertCell();
        const btnErase = document.createElement("button");
        btnErase.className = "btn btn-danger";
        btnErase.type = "button";
        btnErase.innerHTML = '<svg width="20px" height="20px" viewBox="0 0 16 16" class="bi bi-trash text-white mb-1" fill="currentColor" xmlns="http://www.w3.org/2000/svg">\n' +
            '    <path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6z"/>\n' +
            '    <path fill-rule="evenodd" d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1zM4.118 4L4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z"/>\n' +
            '</svg>';
        btnErase.id = forms[i].id;
        btnErase.onclick = function () {
            eraseForm(forms[i].id);
        };
        erase.appendChild(btnErase);
    }

    document.getElementById("forms").innerHTML = "";
    document.getElementById("forms").appendChild(table);
}

function viewUser(username, password, name, lastname, role) {
    document.getElementById("username").value = username;
    document.getElementById("name").value = name;
    document.getElementById("lastname").value = lastname;
    document.getElementById("password").value = password;
    document.getElementById("role").value = role;
}

function viewForm(username, name, lastname, sector, education) {
    document.getElementById("formuser").value = username;
    document.getElementById("formname").value = name;
    document.getElementById("formlastname").value = lastname;
    document.getElementById("sector").value = sector;
    document.getElementById("education").value = education;
}

function newUser() {
    document.getElementById("username").value = "";
    document.getElementById("name").value = "";
    document.getElementById("lastname").value = "";
    document.getElementById("password").value = "";
    document.getElementById("role").value = "";
}

function refreshForms() {
    document.getElementById("formuser").value = "";
    document.getElementById("formname").value = "";
    document.getElementById("formlastname").value = "";
    document.getElementById("sector").value = "";
    document.getElementById("education").value = "";

    listForms();
}

function eraseUser(username) {
    const command = {
        method: "DELETE",
        entity: {
            username: username
        }
    };

    webSocket.send(JSON.stringify(["USER", command]));
}

function eraseForm(id) {
    const command = {
        method: "DELETE",
        entity: {
            id: id
        }
    };

    webSocket.send(JSON.stringify(["FORM", command]));
}

function listUsers() {
    const command = {
        method: "LIST"
    };

    webSocket.send(JSON.stringify(["USER", command]));
}

function listForms() {
    const command = {
        method: "LIST"
    };

    webSocket.send(JSON.stringify(["FORM", command]));
}