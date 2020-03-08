// 详情
function info(randomId) {
    layer.msg('randomId:' + randomId);
    let methodNode = "";
    $.ajax({
        type: "get",
        url: '/trace/singleMethodNode?randomId=' + randomId,
        async: false,
        dataType: "json",
        success: function (rtn) {
            methodNode = rtn;
            // console.log('singleMethodNode:',rtn);
        }
    });

    // ajax请求参数randomId，获取treeNode
    table.render({
        elem: '#className' //指定原始表格元素选择器（推荐id选择器）
        , title: '静态信息'
        , cols: [[
            {field: 'className', title: '类名', width: '580'}
        ]]
        , data: [
            methodNode
        ]
    });
    //执行渲染
    table.render({
        elem: '#staticinfo' //指定原始表格元素选择器（推荐id选择器）
        , title: '静态信息'
        , cols: [[
            {field: 'modifier', title: '修饰符', width: '190'}
            , {field: 'returnType', title: '返回值', width: '190'}
            , {field: 'methodName', title: '方法名', width: '190'}
        ]]
        , data: [
            methodNode
        ]
    });

    table.render({
        elem: '#parameter' //指定原始表格元素选择器（推荐id选择器）
        , title: '参数信息'
        , cols: [[
            {field: 'parameters', title: '参数列表', width: '290'}
            , {field: 'exceptionTypes', title: '异常列表', width: '290'}
        ]]
        , data: [
            methodNode
        ]
    });

    table.render({
        elem: '#exception' //指定原始表格元素选择器（推荐id选择器）
        , title: '异常'
        , cols: [[
            {field: 'exception', title: '异常', width: '290'}
            , {field: 'exceptionInfo', title: '异常信息', width: '290'}
        ]]
        , data: [
            methodNode
        ]
    });
    table.render({
        elem: '#time' //指定原始表格元素选择器（推荐id选择器）
        , title: '耗时'
        , cols: [[
            {field: 'startTime', title: '起始时间', width: '140'}
            , {field: 'endTime', title: '终止时间', width: '140'}
            , {field: 'relativeTime', title: '耗时', width: '140'}
            , {field: 'childNodeList', title: '子方法', width: '140'}
        ]]
        , data: [
            methodNode
        ]
    });


    layer.open({
        type: 1,
        area: ['600px', '550px'],
        title: '方法详情',
        content: $("#info"),
        shade: 0.3,
        btn: ['关闭'],
        btn1: function (index, layero) {
            layer.closeAll();
        },
        // btn2: function (index, layero) {
        //     alert("重置");
        //     return false;
        // },
        // cancel: function (layero, index) {
        //     layer.closeAll();
        // }
    });
}