function initListener() {
    //监听折叠
    element.on('collapse(real)', function (data) {
        if (data.show === true) {
            const traceId = data.content[0].children[0].id;
            asyncHttpGet(real_trace_api + '/' + traceId, function (rtn) {
                if (!isEmpty(rtn)) {
                    $('#' + traceId).html('<ul>' + buildTree(rtn) + '</ul>');
                    bindAnimation(traceId);
                } else {
                    layer.msg('This id:' + traceId + ' server has no corresponding data');
                }
            });
        }
    });
}

// 构建tree dom
function buildTree(obj) {
    let methodName = obj.method.name;
    let relative = obj.relative;
    let nodeId = obj.nodeId;
    nodeId += '$' + getRandomId();
    let className = obj.method.className;

    let child = obj.children;
    let exception = obj.exception;

    let html = '<li><span title="' + className + '" ';
    if (exception === true) {
        html += 'class="badge badge-important" ';
    }
    if (hasChild(child)) {
        html += '><i class="icon-minus-sign"></i>';// 菜单结点
    } else {
        html += '><i class="icon-leaf"></i>';// 叶子结点
    }
    html += className + '-->' + methodName + '()</span> <i> 耗时:'
        + relative + 'ms</i> <i><a href="javascript:void(0);" id="' + nodeId + '" onclick="info(this.id)">详情</a></i>';

    if (hasChild(child)) {
        html += '<ul>';
        for (let i = 0; i < child.length; i++) {
            html += buildTree(child[i]);
        }
        html += '</ul>';
    }
    html += '</li>';

    return html;
}

// 为不同traceId绑定动画
function bindAnimation(id) {
    if (!isEmpty(id)) {
        $('div #' + id + ' li:has(ul)').addClass('parent_li');
        $('div #' + id + ' li.parent_li > span').on('click', function (e) {
            const children = $(this).parent('li.parent_li').find(' > ul > li');
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