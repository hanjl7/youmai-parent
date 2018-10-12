app.controller('payController', function ($scope, payService) {

    //生成二维码
    $scope.createNative = function () {
        payService.createNative().success(
            function (response) {
                //金额（分）
                $scope.money = (response.total_fee / 100).toFixed(2);
                //订单号
                $scope.out_trade_no = response.out_trade_no;
                //二维码
                var qr = new QRious({
                    elenent: document.getElementById('qrious'),
                    size: 250,
                    level: 'H',
                    value: response.code_url
                })
            }
        )
    }
})