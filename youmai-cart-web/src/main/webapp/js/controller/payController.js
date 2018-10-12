app.controller('payController', function ($scope, $location, payService) {

    //生成二维码
    $scope.createNative = function () {
        payService.createNative().success(
            function (response) {
                //金额（分）
                $scope.money = (response.total_fee / 100).toFixed(2);
                //订单号
                $scope.out_trade_no = response.out_trade_no;
                //二维码

                //二维码超时提示清空
                $scope.messageNative ='';

                var qr = new QRious({
                    element: document.getElementById('qrious'),
                    size: 250,
                    level: 'H',
                    value: response.code_url
                })

                //生成二维码之后，查询支付是否成功，后台查询
                queryPayStatus(response.out_trade_no);
            }
        )
    }

    //查询支付状态
    queryPayStatus = function (out_trade_no) {
        payService.queryPayStatus(out_trade_no).success(
            function (response) {
                if (response.success) {
                    //支付成功
                    location.href = "paysuccess.html#?money=" + $scope.money;
                } else {
                    if (response.message == '二维码超时') {
                        //二维码超时提示
                        $scope.messageNative = '支付超时，请点击二维码刷新';
                    } else {
                        //支付错误
                        location.href = "payfail.html";
                    }
                }
            }
        )
    }

    //获取金额
    $scope.getMoney=function(){
        return $location.search()['money'];
    }

})