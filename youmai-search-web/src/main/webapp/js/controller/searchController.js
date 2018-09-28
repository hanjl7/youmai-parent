app.controller("searchController", function ($scope, $location, searchService) {

    //keywords =关键词 category = 过滤条件商品分类 brand = 过滤条件品牌分类
    $scope.searchMap = {
        'keywords': '',
        'category': '',
        'brand': '',
        'spec': {},
        'price': '',
        'pageNo': 1,
        'pageSize': 20,
        'sortField': '',
        'sort': ''
    };


    $scope.search = function () {

        //转换成int类型
        $scope.searchMap.pageNo = parseInt($scope.searchMap.pageNo);

        searchService.search($scope.searchMap).success(
            function (response) {
                $scope.resultMap = response;
                //调用分页
                buildPageLabel();
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

    //构建分页标签
    buildPageLabel = function () {
        //分签页
        $scope.pageLabel = [];
        //最后页码
        var maxpageNo = $scope.resultMap.totalPages;
        //第一页
        var firstPage = 1;
        //截至页码
        var lastPage = maxpageNo;
        //前面的点
        $scope.firstDot = true;
        //后面的点
        $scope.lastDot = true;
        if ($scope.resultMap.totalPages > 5) {
            //总页数大于5显示部分页码
            if ($scope.searchMap.pageNo <= 3) {
                //如果当前页小于3
                lastPage = 5;//显示前5页
                //前五页没点
                $scope.firstDot = false;
            } else if ($scope.searchMap.pageNo >= lastPage - 2) {
                //当前页大于等于最大页码-2
                firstPage = maxpageNo - 4;
                $scope.lastDot = false;
            } else {
                //显示当前页为中心的5页
                firstPage = $scope.searchMap.pageNo - 2;
                lastPage = $scope.searchMap.pageNo + 2;
            }
        } else {
            //页面小于5页没有点
            $scope.firstDot = false;
            $scope.lastDot = false;
        }

        for (var i = firstPage; i <= lastPage; i++) {
            $scope.pageLabel.push(i);
        }
    }

    //根据分页查询
    $scope.queryByPage = function (pageNo) {
        //页码验证
        if (pageNo < 1 || pageNo > $scope.resultMap.totalPages) {
            return;
        }
        $scope.searchMap.pageNo = pageNo;
        $scope.search();
    }

    //判断当前页是否是第一页
    $scope.isToPage = function () {
        if ($scope.searchMap.pageNo == 1) {
            return true;
        } else {
            return false;
        }
    }

    //判断当前页是否是最后一页
    $scope.isEndPage = function () {
        if ($scope.searchMap.pageNo == $scope.resultMap.totalPages) {
            return true;
        } else {
            return false;
        }
    }

    //设置排序规则
    $scope.sortSearch = function (sortField, sort) {
        $scope.searchMap.sortField = sortField;
        $scope.searchMap.sort = sort;
        $scope.search();
    }

    //判断关键字是否是品牌
    $scope.keywordsIsBrand = function () {
        for (var i = 0; i < $scope.resultMap.brandList.length; i++) {
            if ($scope.searchMap.keywords.indexOf($scope.resultMap.brandList[i].text) >= 0) {//如果包含
                return true;
            }
        }
        return false;
    }

    //加载从主页面要查询的关键字
    $scope.loadKeywords = function () {
        $scope.searchMap.keywords = $location.search()['keywords'];
        $scope.search();
    }


});