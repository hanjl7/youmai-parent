app.controller("baseController",function ($scope) {

    //分页控件配置
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function () {
            $scope.reloadList();
        }
    };

    //刷新列表
    $scope.reloadList = function () {
        $scope.searchBrand($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    };

    //选中的id集合
    $scope.selectIds = [];

    //勾选复选框
    $scope.updateSelection = function ($event, id) {
        if ($event.target.checked) {
            //添加
            $scope.selectIds.push(id);
        } else {
            var idx = $scope.selectIds.indexOf(id);
            //删除
            $scope.selectIds.splice(idx, 1);
        }

    };
});