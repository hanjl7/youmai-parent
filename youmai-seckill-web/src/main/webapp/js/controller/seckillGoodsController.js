app.controller('seckillGoodsController', function ($scope, seckillGoodsService) {

    $scope.findList = function () {
        seckillGoodsService.findList().success(
            function (response) {
                $scope.list = response;
            }
        )
    }

})