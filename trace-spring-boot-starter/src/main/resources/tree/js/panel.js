new Vue({
    el: '#app',
    data: {
        items: []
    }
    ,
    computed: {}
    ,
    created: function () {
        let data = "";
        $.ajax({
            url: "/trace/url",
            async: false,
            dataType: "json",
            success: function (rtn) {
                data = rtn;
            }
        });
        this.items = data;
    }
});