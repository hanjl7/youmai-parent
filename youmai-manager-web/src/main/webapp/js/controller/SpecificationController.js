app.controller("specificationController", function ($scope, $controller,specificationService) {

    $controller("baseController", {$scope:$scope});


    $scope.findAll = function (page, size) {
        specificationService.findAll(page, size).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;
            })
    };

    //添加行
    $scope.addTableRow = function () {
        $scope.entity.specificationOptionList.push({});
    };

    //删除行
    $scope.deleteTableRow = function (index) {
        $scope.entity.specificationOptionList.splice(index, 1);
    };

    //增加
    $scope.save = function () {
        var object;
        if ($scope.entity.specification.id != null) {
            //有id说明修改
            object = specificationService.update($scope.entity)
        } else {
            object = specificationService.add($scope.entity)
        }

        object.success(function (response) {
            if (response.success) {
                $scope.reloadList();
            } else {
                alert(response.message);
            }
        })
    };

    $scope.findOne = function (id) {
        specificationService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        )
    };


    $scope.deleteSpec = function () {
        specificationService.deleteSpec($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();
                } else {
                    alert(response.message);
                }
            }
        )
    };

    $scope.searchEntity = {};

    //分页 + 查询
    $scope.searchBrand = function (page, size) {
        specificationService.findAll(page, size).success(
            function (response) {
                $scope.list = response.rows;//显示当前页数据
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        )
    };

});