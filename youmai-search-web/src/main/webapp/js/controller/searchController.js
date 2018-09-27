app.controller("searchController", function ($scope, searchService) {

    //keywords =关键词 category = 过滤条件商品分类 brand = 过滤条件品牌分类
    $scope.searchMap = {'keywords': '', 'category': '', 'brand': '', 'spec': {},'price':''};

    $scope.search = function () {
        searchService.search($scope.searchMap).success(
            function (response) {
                $scope.resultMap = response;
            }
        )
    }

    //添加搜索条件
    $scope.addSearchItem = function (key, value) {
        //如果点击的是商品分类或者是品牌
        if (key == 'category' || key == 'brand' || key == 'price') {
            $scope.searchMap[key] = value;
        } else {
            //key是规格 条件
            $scope.searchMap.spec[key] = value;
        }
        //执行搜索
        $scope.search();
    }

    //移除搜索条件
    $scope.removeSearchItem = function (key) {
        //如果点击的是商品分类或者是品牌
        if (key == 'category' || key == 'brand' || key == 'price') {
            $scope.searchMap[key] = "";
        } else {
            //key是规格 条件
            //移除属性
           delete $scope.searchMap.spec[key];
        }

        //执行搜索
        $scope.search();
    }


});