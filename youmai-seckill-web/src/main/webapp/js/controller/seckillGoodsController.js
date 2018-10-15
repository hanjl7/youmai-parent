app.controller('seckillGoodsController', function ($scope, $interval, $location, seckillGoodsService) {

    $scope.findList = function () {
        seckillGoodsService.findList().success(
            function (response) {
                $scope.list = response;
            }
        )
    }

    $scope.findOne = function () {
        var id = $location.search()['id'];
        seckillGoodsService.findOne(id).success(
            function (response) {
                $scope.entity = response;

                //总秒数
                allSeconds = Math.floor((new Date($scope.entity.endTime).getTime() - (new Date().getTime())) / 1000);


                time = $interval(function () {
                    if (allSeconds > 0) {
                        allSeconds = allSeconds - 1;
                        $scope.timeStr = converTimeString(allSeconds);
                    } else {
                        $interval.cancel(time);
                        alert("秒杀已结束")
                    }
                }, 1000)

                converTimeString = function (allSeconds) {
                    //天数
                    var days = Math.floor(allSeconds / (60 * 60 * 24));
                    //小时数
                    var hours = Math.floor((allSeconds - days * 60 * 60 * 24) / (60 * 60));
                    //分钟数
                    var minutes = Math.floor((allSeconds - days * 60 * 60 * 24 - hours * 60 * 60) / 60);
                    //秒数
                    var seconds = Math.floor(allSeconds - days * 60 * 60 * 24 - hours * 60 * 60 - minutes * 60);
                    var timeStr = "";

                    if (days > 0) {
                        timeStr = days + "天";
                    }
                    if (seconds < 10) {
                        seconds = "0" + seconds;
                    } else if (minutes < 10) {
                        minutes = "0" + minutes;
                    } else if (hours < 10) {
                        hours = "0" + hours;
                    }
                    return timeStr + " " + hours + ":" + minutes + ":" + seconds;
                }
            }
        )
    }


})