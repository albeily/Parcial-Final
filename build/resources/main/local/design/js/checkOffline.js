window.addEventListener("offline", () => {
    alert("Disconnected from server");
});

window.addEventListener("online", () => {
    document.cookie = "UID=" + localStorage.getItem("UID");
    document.cookie = "UPA=" + localStorage.getItem("UPA");

    alert("Connection recovered");
    window.location.reload();
});

function checkWebStorage() {
    if (localStorage.getItem("UID") === null) {
        cookie();
    }
}

function signout() {
    localStorage.clear();
}