// 有孩子结点
function hasChild(obj) {
    return !isEmpty(obj) && obj.length >= 1;
}

// 不为空
function isEmpty(obj) {
    return obj === null || obj === 'undefined' || obj === '';
}

function getRandomId() {
    return Number(Math.random().toString().substr(3, 10) + Date.now()).toString(36);
}

// 异步get
function asyncHttpGet(url, callback) {
    return asyncHttp("get", url, callback);
}

// 同步get
function syncHttpGet(url) {
    return syncHttp("get", url);
}

function syncHttp(type, url) {
    let data;
    $.ajax({
        type: type,
        url: url,
        async: false,
        success: function (rtn) {
            data = rtn;
        }
    });
    return data;
}

function asyncHttp(type, url, callback) {
    $.ajax({
        type: type,
        url: url,
        async: true,
        success: function (rtn) {
            if (typeof callback === "function") {
                callback(rtn);
            }
        }
    });
}