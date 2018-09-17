app.controller("baseController", function ($scope) {

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

    $scope.reloadList = function () {
        $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
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

    //提取json字符串数据中某个属性，返回拼接字符串 逗号分隔
    $scope.jsonToString = function (jsonString, Key) {
        var json = JSON.parse(jsonString); //将json字符串转换为json对象
        var value = "";
        for (var i = 0; i < json.length; i++) {
            if (i > 0) {
                value += ",";
            }
            value += json[i][Key];
        }
        return value;
    }
    //从集合中按照key查找对象
    $scope.searchObjectByKey = function (list, key, keyValue) {
        for (var i = 0; i < list.length; i++) {
            if (list[i][key] == keyValue) {
                return list[i];
            }
        }
        return null;
    }

});