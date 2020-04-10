function initListener() {
    //监听折叠
    element.on('collapse(static)', function (data) {
        if (data.show === true) {
            const chainId = data.content[0].children[0].id;
            asyncHttpGet(static_chain_api + '/' + chainId, function (rtn) {
                if (!isEmpty(rtn)) {
                    $('#' + chainId).html('<ul>' + buildTree(rtn) + '</ul>');
                    bindAnimation(chainId);
                } else {
                    layer.msg('This id:' + chainId + ' server has no corresponding data');
                }
            });
        }
    });
}

// 构建tree dom
function buildTree(obj) {
    let signatureId = obj.signatureId;
    signatureId += '$' + getRandomId();
    let methodName = obj.name;
    let className = obj.className;
    // if (!isEmpty(className)) {
    //     className = className.substring(className.lastIndexOf(".") + 1) + '.';
    // }
    let child = obj.children;

    let html = '<li><span title="' + className + '">';
    if (hasChild(child)) {
        html += '<i class="icon-minus-sign"></i>';// 菜单结点
    } else {
        html += '<i class="icon-leaf"></i>';// 叶子结点
    }
    html += className +'-->'+ methodName + '()</span><a href="javascript:void(0);" id="' + signatureId + '" onclick="info(this.id)">详情</a></i>';

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