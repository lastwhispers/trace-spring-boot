// 详情
function info(id) {

    id = id.split('$')[0];
    let method = syncHttpGet(static_method_api + '/' + id);

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
        content: $("#method-info"),
        shade: 0.3,
        btn: ['关闭'],
        btn1: function (index, layero) {
            layer.closeAll();
        },
    });
}