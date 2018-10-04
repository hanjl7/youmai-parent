//控制层
app.controller('itemController', function ($scope) {

    //批量效验
    $scope.addNum = function (x) {
        $scope.num = $scope.num + x;
        if ($scope.num < 1) {
            $scope.num = 1;
        } else if ($scope.num > 200) {
            $scope.num = 200;
        }
    }

    //记录用户选择的规格
    $scope.specificationItems = {};

    //用户选择的规格
    $scope.selectSpecification = function (key, value) {
        $scope.specificationItems[key] = value;
        //根据规格选择sku
        searchSKU();
    }

    //判断某个规格是否被选中
    $scope.isSelected = function (key, value) {
        if ($scope.specificationItems[key] == value) {
            return true;
        } else {
            return false;
        }
    }

    //当前选择的sku
    $scope.sku = {};

    //加载默认SKU列表
    $scope.loadDefaultSKU = function () {
        $scope.sku = skuList[0];
        $scope.specificationItems = JSON.parse(JSON.stringify($scope.sku.spec));
    }


    //匹配两个对象是否相等
    matchObject = function (map1, map2) {
        for (var key  in map1) {
            if (map1[key] != map2[key]) {
                return false;
            }

        }
        for (var key  in map2) {
            if (map2[key] != map1[key]) {
                return false;
            }

        }
        return true;
    }

    //根据规格查询SKU
    searchSKU = function () {

        for (var i = 0; i < skuList.length; i++) {
            if (matchObject(skuList[i].spec, $scope.specificationItems)) {
                $scope.sku = skuList[i];
                return;
            }
        }
        $scope.sku={id:0,title:'--------',price:0}
    }

    //添加商品到购物车
    $scope.addToCart=function () {
        alert('skuid'+$scope.sku.id);
    }
});
