app.controller("brandController", function ($scope,$controller, brandService) {

    $controller("baseController",{$scope:$scope});


    //查询品牌列表
    $scope.findAll = function () {
        brandService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        )
    };



    //分页
    $scope.findPage = function (page, size) {
        brandService.findPage(page, size).success(
            function (response) {
                $scope.list = response.rows;//显示当前页数据
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        )
    };


    //增加品牌
    //修改品牌
    $scope.save = function () {
        var object;
        if ($scope.entity.id != null) {
            //更新
            object = brandService.update($scope.entity);
        } else {
            //添加
            object = brandService.add($scope.entity);
        }
        object.success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//成功刷新
                } else {
                    alert(response.message);//失败提示
                }
            }
        )
    };

    //展示修改的实体
    $scope.findOne = function (id) {
        brandService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        )
    }





    //删除
    $scope.deleteBrands = function () {
        brandService.deleteBrands($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();
                } else {
                    alert(response.message)
                }
            }
        )
    };

    $scope.searchEntity = {};

    //分页 + 查询
    $scope.searchBrand = function (page, size) {
        brandService.search(page, size, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;//显示当前页数据
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        )
    };


});