//购物车服务层
app.service('cartService', function ($http) {
    //购物车列表
    this.findCartList = function () {
        return $http.get('cart/findCartList.do');
    }
    //添加商品到购物车列表
    this.addGoodsToCartList = function (itemId, num) {
        return $http.get('cart/addGoodsToCartList.do?itemId=' + itemId + '&num=' + num);

    }

    //求合计
    this.sum = function (cartList) {
        var totalValue = {totalNum: 0, totalMoney: 0.00};
        for (var i = 0; i < cartList.length; i++) {
            var cart = cartList[i];
            for (var j = 0; j < cart.orderItemList.length; j++) {
                //当前商品购物车明细
                var orderItem = cart.orderItemList[j];
                totalValue.totalNum += orderItem.num;
                totalValue.totalMoney += orderItem.totalFee;
            }
        }
        return totalValue;
    }

    this.findAddressListByLoginUser = function () {
        return $http.get('address/findAddressListByLoginUser.do');
    }

    this.saveAddress = function (newAddress) {
        return $http.post('address/add.do', newAddress);
    }

    this.findOne=function(id){
        return $http.get('address/findOne.do?id='+id);
    }

    //保存订单
    this.submitOrder=function(order){
        return $http.post('order/add.do',order);
    }

    this.findProvinces=function () {
        return $http.get('address/findProvinces.do');
    }

    this.findCitiesByProvinces=function (provinceId) {
        return $http.get('address/findCitiesByProvinces.do?provincesId='+provinceId);
    }

    this.findAreasByCities=function (citiesId) {
        return $http.get('address/findAreasByCities.do?citiesId='+citiesId);
    }


});
