async function getInfo(fromUrl) {
    console.log("Getting data from", fromUrl);
    return doAjaxThings(fromUrl);
}

async function postInfo(toUrl) {
    console.log("Posting to", toUrl);
    await makeRequest("POST", toUrl);
}

async function doAjaxThings(url) {
    return makeRequest("GET", url);
}

async function makeRequest(method, url) {
    return new Promise(function (resolve, reject) {
        let xhr = new XMLHttpRequest();
        xhr.open(method, url);
        xhr.onload = function () {
            if (this.status >= 200 && this.status < 300) {
                resolve(xhr.response);
            } else {
                reject({
                    status: this.status,
                    statusText: xhr.statusText
                });
            }
        };
        xhr.onerror = function () {
            reject({
                status: this.status,
                statusText: xhr.statusText
            });
        };
        xhr.send();
    });
}


async function placeOrder() {
    const quantity = document.getElementById("order-quantity").value;
    const info = document.getElementById("order-info").value;

    postInfo("/owner/add-order/" + user_name + "/" + quantity + "/" + info).then(()=>{
        updateOwner()

    });
}

function loadOrders() {
    const ordersContainer = document.getElementById("owner-orders");
    ordersContainer.innerHTML = "";

    getInfo("/owner/get-orders/" + user_name).then((orders => {
        console.log(orders);
        orders = JSON.parse(orders);
        orders.forEach(function (o) {
            ordersContainer.innerHTML += "<div>" + representOrder(o) + "</div>" + "<br>";
        })
    }));
}

function representOrder(o) {
    let res = "";
    let orderId = o["id"];
    let info = o["info"];
    let quantity = o["quantity"];
    let status = o["status"];
    res = "<span  class='table-row'>" +
        wrapInTableDiv(orderId, 20) +
        wrapInLongDiv(info, 300) +
        wrapInTableDiv("#" + quantity, 30) +
        wrapInTableDiv(status, 100) +
        approveButton(o) +
        "</span>";

    return res;
}

function approveButton(o) {
    let status = o["status"];
    let orderId = o["id"];
    if (status === "waiting for approval")
        return "<input type='button' onclick='setOrderDone(" + orderId + ")' value='Approve'>";
    else
        return "";
}

function wrapInTableDiv(str, width) {
    return "<div class='table'>" + str + "</div>";
}

function wrapInLongDiv(str, width) {
    return "<div class='long'>" + str + "</div>";
}

function loadTasks() {
    const tasksContainer = document.getElementById("gardener-tasks");
    tasksContainer.innerHTML = "";

    getInfo("/gardener/get-tasks/" + user_name).then((tasks => {
        let taskId = 0;
        console.log(tasks);
        tasks = JSON.parse(tasks);
        tasks.forEach(function (o) {
            tasksContainer.innerHTML += "<div>" + representTask(o) + "</div>" + "<br>";
        })
    }));
}

function representTask(o) {
    let orderId = o["id"];
    let info = o["info"];
    let quantity = o["quantity"];
    let status = o["status"];
    let ownerName = o["user"]["login"];
    return "<span class='table-row'>" +
        wrapInTableDiv(orderId, 20) +
        wrapInLongDiv(ownerName, 300) +
        wrapInLongDiv(info, 300) +
        wrapInTableDiv("#" + quantity, 30) +
        wrapInTableDiv(status, 100) +
        markDoneButton(o) +
        "</span>";
}

function markDoneButton(o) {
    let status = o["status"];
    let orderId = o["id"];
    if (status === "initial")
        return "<input type='button' onclick='setTaskDone(" + orderId + ")' value='Mark Done'>";
    else
        return "";
}

function setOrderDone(orderId) {
    postInfo("/owner/done-order/" + orderId).then(() => {
        updateOwner();
    });
}

function setTaskDone(taskId) {
    postInfo("/gardener/done-task/" + taskId).then(() => {
        updateGardener();
    });
}

function updateOwner() {
    console.log("Update owner info");
    loadOrders();
}

function updateGardener() {
    console.log("Update gardener info");
    loadTasks();
}

async function removeFinished() {
    console.log("Remove finished");
    postInfo("/owner/delete/finished/" + user_name).then(() => {
        updateOwner()
    });
}

window.onload = function () {
    if (window.location.href !== "http://localhost:8090/") {
        window.location.href = "http://localhost:8090/"
    }

    console.log("user name", user_name);
    console.log("user role", role_map);

    if (role_map === "owner") {
        updateOwner();
    } else if (role_map === "gardener") {
        updateGardener();
    }

};