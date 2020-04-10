// 详情
function info(id) {

    id = id.split('$')[0];
    let node = syncHttpGet(real_node_api + '/' + id);
    let method = node.method;

    table.render({
        elem: '#time'
        , title: '耗时'
        , cols: [[
            {field: 'start', title: '起始时间', width: '170'}
            , {field: 'end', title: '终止时间', width: '170'}
            , {
                field: 'relative', title: '耗时', width: '170', templet: function (d) {
                    return d.relative + 'ms';
                }
            }
            , {
                field: 'children', title: '子方法数量', width: '170', templet: function (d) {
                    if (!isEmpty(d.children)) {
                        return d.children.length;
                    }
                    return 0;
                }
            }
        ]]
        , data: [
            node
        ]
    });

    table.render({
        elem: '#exception'
        , title: '异常'
        , cols: [[
            {field: 'exception', title: '异常', width: '170'}
            , {
                field: 'name', title: '异常全限定名', width: '170', templet: function (d) {
                    if (!isEmpty(d.exceptionInfo) && !isEmpty(d.exceptionInfo.name)) {
                        return d.exceptionInfo.name;
                    }
                    return '';
                }
            }
            , {
                field: 'lineNumber', title: '异常栈', width: '170', templet: function (d) {
                    if (!isEmpty(d.exceptionInfo) && !isEmpty(d.exceptionInfo.ste)) {
                        return JSON.stringify(d.exceptionInfo.ste);
                    }
                    return '';
                }
            }
            , {
                field: 'msg', title: '异常信息', width: '170', templet: function (d) {
                    if (!isEmpty(d.exceptionInfo) && !isEmpty(d.exceptionInfo.name)) {
                        return JSON.stringify(d.exceptionInfo.name);
                    }
                    return '';
                }
            }

        ]]
        , data: [
            node
        ]
    });

    table.render({
        elem: '#className'
        , title: '静态信息'
        , cols: [[
            {field: 'className', title: '类名', width: '345'}
            ,{field: 'superclass', title: '父类', width: '345'}
        ]]
        , data: [
            method
        ]
    });

    table.render({
        elem: '#interfaces'
        , title: '静态信息'
        , cols: [[
            {field: 'interfaces', title: '接口', width: '690', templet: function (d) {
                    if (!isEmpty(d.interfaces)) {
                        return JSON.stringify(d.interfaces);
                    }
                    return '';
                }}
        ]]
        , data: [
            method
        ]
    });


    table.render({
        elem: '#signature'
        , title: '静态信息'
        , cols: [[
            {field: 'modifier', title: '修饰符', width: '80'}
            , {field: 'returnType', title: '返回值', width: '270'}
            , {field: 'name', title: '方法名', width: '340'}
        ]]
        , data: [
            method
        ]
    });

    table.render({
        elem: '#parameter'
        , title: '参数信息'
        , cols: [[
            {
                field: 'parameters', title: '参数列表', width: '350', templet: function (d) {
                    if (!isEmpty(d.parameters)) {
                        return JSON.stringify(d.parameters);
                    }
                    return '';
                }
            }
            , {
                field: 'exceptions', title: '异常列表', width: '340', templet: function (d) {
                    if (!isEmpty(d.exceptions)) {
                        return JSON.stringify(d.exceptions);
                    }
                    return '';
                }
            }
        ]]
        , data: [
            method
        ]
    });

    layer.open({
        type: 1,
        area: ['700px', '600px'],
        title: '方法详情',
        content: $("#node-info"),
        shade: 0.3,
        btn: ['关闭'],
        btn1: function (index, layero) {
            layer.closeAll();
        },
    });
}