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
    //按登录用户查找地址列表
    $scope.findAddressListByLoginUser = function () {
        cartService.findAddressListByLoginUser().success(
            function (response) {
                $scope.addressList = response;
                //查询 设置默认地址
                for (var i = 0; i < $scope.addressList.length; i++) {
                    if ($scope.addressList[i].isDefault == '1') {
                        $scope.address = $scope.addressList[i];
                        break;
                    }

                }

            }
        )
    }

    $scope.selectIsDefault = function (type) {
        $scope.newAddress.isDefault = type;
    }

    //选择地址
    $scope.selectAddress = function (address) {
        $scope.address = address;
    }

    //判断当前是否选择的地址
    $scope.isSelectAddress = function (address) {
        if (address == $scope.address) {
            return true;
        } else {
            return false;
        }
    }

    //添加收获地址
    $scope.saveAddress = function () {
        cartService.saveAddress($scope.newAddress).success(
            function (response) {
                if (response.success) {
                    $scope.findAddressListByLoginUser();
                } else {
                    alert(response.message);
                }
            }
        )
    }

    //默认付款方式
    $scope.order = {paymentType: '1'};
    //付款方式
    $scope.selectPayType = function (type) {
        $scope.order.paymentType = type;
    }

    //查询地址
    $scope.findOne = function (id) {
        cartService.findOne(id).success(
            function (response) {
                $scope.newAddress = response;
            }
        );
    }

    //保存订单
    $scope.submitOrder = function () {
        //订单地址
        $scope.order.receiverAreaName = $scope.address.address;
        //手机号
        $scope.order.receiverMobile = $scope.address.mobile;
        //联系人姓名
        $scope.order.receiver = $scope.address.contact;

        cartService.submitOrder($scope.order).success(
            function (response) {
                if (response.success) {
                    //跳转支付页面
                    if ($scope.order.paymentType = '1') {
                        //微信支付页面
                        location.href = "pay.html";
                    } else {
                        //货到付款
                        location.href = "paysuccess.html";
                    }
                } else {
                    alert(response.message);
                }
            }
        )
    }
});
