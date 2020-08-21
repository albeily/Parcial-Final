// Object reference, depending the version of the browser
var indexedDB = window.indexedDB || window.mozIndexedDB || window.webkitIndexedDB || window.msIndexedDB

// Name and version of database
var dataBase = indexedDB.open("Parcial-2", 1);

// Executes the first time the structure is created or changes database version
dataBase.onupgradeneeded = function (e) {
    // Active connection
    active = dataBase.result;

    // Creating collection
    var forms = active.createObjectStore("forms", {keyPath: 'id', autoIncrement: true});
    forms.createIndex("by_name", "name", {unique: false});
    forms.createIndex('by_user', 'user', {unique: false});
};

dataBase.onsuccess = function (e) {
    // console.log('Process executed correctly!');
};

dataBase.onerror = function (e) {
    console.error('Error during process process: ' + e.target.errorCode);
};