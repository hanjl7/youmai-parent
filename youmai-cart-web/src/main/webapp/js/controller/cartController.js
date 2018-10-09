//购物车控制层 
app.controller('cartController', function ($scope, cartService) {
    //查询购物车列表
    $scope.findCartList = function () {
        cartService.findCartList().success(
            function (response) {
                //购物车列表
                $scope.cartList = response;
                //求合计
                $scope.totalValue = cartService.sum($scope.cartList);
            }
        );
    }
    //添加商品到购物车列表
    $scope.addGoodsToCartList = function (itemId, num) {

        cartService.addGoodsToCartList(itemId, num).success(
            function (response) {
                if (response.success) {
                    //刷新列表
                    $scope.findCartList();
                } else {
                    alert(response.message);
                }
            }
        )


    }
});
