var app = angular.module("youmai", []); //定义未分页模块

/*$sce服务写成过滤器*/
app.filter("trustHtml",["$sce",function ($sce) {
    //传入被过滤的内容
    return function (date) {
        //返回过滤后的内容
        return $sce.trustAsHtml(date);
    }
}])