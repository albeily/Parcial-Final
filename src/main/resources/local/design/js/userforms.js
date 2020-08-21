function formList() {

    var data = dataBase.result.transaction(["forms"]);
    var forms = data.objectStore("forms");
    var counter = 0;
    var formsrecovered = [];

    forms.openCursor().onsuccess = function (e) {

        var cursor = e.target.result;
        if (cursor) {
            counter++;
            formsrecovered.push(cursor.value);
            cursor.continue();

        } else {
            console.log("Amount recovered: " + counter);
        }
    };

    data.oncomplete = function () {
        printTable(formsrecovered);
    }

}


function printTable(formlist) {

    var table = document.createElement("table");
    var rowTable = table.insertRow();
    table.className = "table table-sm";
    table.style.width = "100%";
    table.isContentEditable = false;

    for (var i in formlist) {

        rowTable = table.insertRow();
        var id = rowTable.insertCell();
        id.textContent = formlist[i].id;
        id.style.fontWeight = "bold";
        var name = rowTable.insertCell();
        name.textContent = formlist[i].person.name;

        var lastname = rowTable.insertCell();
        lastname.textContent = formlist[i].person.lastname;
        var open = rowTable.insertCell();
        var openbtn = document.createElement("button");
        openbtn.className = "btn btn-success";
        openbtn.innerHTML = '<svg  width="20px" height="20px" viewBox="0 0 16 16" class="bi bi-folder2-open mb-1 text-white" fill="currentColor" xmlns="http://www.w3.org/2000/svg">\n' +
            '    <path fill-rule="evenodd" d="M1 3.5A1.5 1.5 0 0 1 2.5 2h2.764c.958 0 1.76.56 2.311 1.184C7.985 3.648 8.48 4 9 4h4.5A1.5 1.5 0 0 1 15 5.5v.64c.57.265.94.876.856 1.546l-.64 5.124A2.5 2.5 0 0 1 12.733 15H3.266a2.5 2.5 0 0 1-2.481-2.19l-.64-5.124A1.5 1.5 0 0 1 1 6.14V3.5zM2 6h12v-.5a.5.5 0 0 0-.5-.5H9c-.964 0-1.71-.629-2.174-1.154C6.374 3.334 5.82 3 5.264 3H2.5a.5.5 0 0 0-.5.5V6zm-.367 1a.5.5 0 0 0-.496.562l.64 5.124A1.5 1.5 0 0 0 3.266 14h9.468a1.5 1.5 0 0 0 1.489-1.314l.64-5.124A.5.5 0 0 0 14.367 7H1.633z"/>\n' +
            '</svg>';
        openbtn.type = "button";
        openbtn.id = formlist[i].id;
        openbtn.onclick = function () {
            console.log(this.id);
            openForm(this.id);
        };
        open.appendChild(openbtn);
        var erase = rowTable.insertCell();
        var erasebtn = document.createElement("button");
        erasebtn.className = "btn btn-danger";
        erasebtn.type = "button";
        erasebtn.innerHTML = '<svg width="20px" height="20px" viewBox="0 0 16 16" class="bi bi-trash text-white mb-1" fill="currentColor" xmlns="http://www.w3.org/2000/svg">\n' +
            '    <path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6z"/>\n' +
            '    <path fill-rule="evenodd" d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1zM4.118 4L4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z"/>\n' +
            '</svg>';
        erasebtn.id = formlist[i].id;
        erasebtn.onclick = function () {
            console.log(this.id);
            deleteForm(this.id);
        };

        erase.appendChild(erasebtn);
    }

    document.getElementById("myForms").innerHTML = "";
    document.getElementById("myForms").appendChild(table);

}

function openForm(idForm) {

    console.log("form id: " + idForm);

    var data = dataBase.result.transaction(["forms"]);
    var forms = data.objectStore("forms");

    forms.get(Number(idForm)).onsuccess = function (e) {

        var results = e.target.result;
        console.log("the data is: " + results);

        if (results !== undefined) {
            document.getElementById("formname").value = results.person.name;
            document.getElementById("formlastname").value = results.person.lastname;
            document.getElementById("formsector").value = results.sector;
            document.getElementById("formeducation").value = results.education;
            document.getElementsByClassName("btn-update").id = results.id;
        } else {
            console.log("Form not found...");
        }
    };
}

function updateForm() {

    var name = document.getElementById("formname").value;
    var lastname = document.getElementById("formlastname").value;
    var sector = document.getElementById("formsector").value;
    var eduLevel = document.getElementById("formeducation").value;
    var id = document.getElementsByClassName("btn-update").id;

    var data = dataBase.result.transaction(["forms"], "readwrite");
    var forms = data.objectStore("forms");

    console.log(Number(id));
    forms.get(Number(id)).onsuccess = function (e) {

        var results = e.target.result;

        if (results !== undefined) {

            results.person.name = name;
            results.person.lastname = lastname;
            results.sector = sector;
            results.education = eduLevel;

            var update = forms.put(results);

            update.onsuccess = function (e) {
                console.log("Data updated!");
            }

            update.onerror = function (e) {
                console.error("Error !");
            }

        } else {
            console.log("Form not found");
        }
    };

    formList();
}

function deleteForm(idForm) {
    var data = dataBase.result.transaction(["forms"], "readwrite");
    var forms = data.objectStore("forms");

    forms.delete(Number(idForm)).onsuccess = function (e) {
        console.log("Form deleted...");
    };

    formList();
}