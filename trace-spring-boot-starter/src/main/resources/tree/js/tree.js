let html = "";

function initListener() {
    //监听折叠
    element.on('collapse(panel)', function (data) {
        if (data.show === true) {
            const traceId = data.content[0].children[0].id;
            $.ajax({
                type: "get",
                url: '/trace/singleTrace?traceId=' + traceId,
                async: true,
                dataType: "json",
                success: function (rtn) {
                    layer.msg('traceId:' + traceId);
                    // console.log('singleTrace:',rtn);
                    buildTree(rtn);
                    $('#' + traceId).html("<ul>" + html + "</ul>");
                    bindAnimation(traceId);
                    html = "";
                }
            });
        }
    });
}

// 构建tree dom
function buildTree(obj) {
    const methodName = obj.methodName;
    const relativeTime = obj.relativeTime;
    const randomId = obj.randomId;
    // console.log('buildTree:',randomId);
    const className = obj.className;
    const childNodeList = obj.childNodeList;
    const exception = obj.exception;

    const classContent = "badge badge-important";

    html += "<li><span title=\"" + className + "\" ";

    if (exception === true) {
        html += "class=\"" + classContent + "\" ";
    }

    if (isEmpty(html)) {
        // 根节点
        html += "><i class=\"icon-folder-open\"></i>"
    } else if (hasChild(childNodeList)) {
        // 菜单结点
        html += "><i class=\"icon-minus-sign\"></i>";
    } else {
        // 叶子结点
        html += "><i class=\"icon-leaf\"></i>";
    }
    html += methodName + "</span><i> 耗时:"
        + relativeTime + "ms</i> <i><a href=\"javascript:void(0);\" id=\"" + randomId + "\" onclick=\"info(this.id)\">详情</a></i>";

    if (hasChild(childNodeList)) {
        html += "<ul>";
        for (let i = 0; i < childNodeList.length; i++) {
            buildTree(childNodeList[i]);
        }
        html += "</ul>";
    }
    html += "</li>"
}

// 有孩子结点
function hasChild(obj) {
    return !isEmpty(obj) && obj.length >= 1;
}

// 不为空
function isEmpty(obj) {
    return obj === null || obj === '' || obj === 'undefined';
}

// 为不同traceId绑定动画
function bindAnimation(traceId) {
    if (!isEmpty(traceId)) {
        $('div#' + traceId + ' li:has(ul)').addClass('parent_li');
        $('div#' + traceId + ' li.parent_li > span').on('click', function (e) {
            var children = $(this).parent('li.parent_li').find(' > ul > li');
            if (children.is(":visible")) {
                children.hide('fast');
                $(this).find(' > i').addClass('icon-plus-sign').removeClass('icon-minus-sign');
            } else {
                children.show('fast');
                $(this).find(' > i').addClass('icon-minus-sign').removeClass('icon-plus-sign');
            }
            e.stopPropagation();
        });
    }
}