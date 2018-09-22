//控制层
app.controller('goodsController', function ($scope, $controller, $location, itemCatService, goodsService,brandService) {

    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        goodsService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //分页
    $scope.findPage = function (page, rows) {
        goodsService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //查询实体
    $scope.findOne = function (id) {
        //获取参数
        var id = $location.search()['id'];

        if (id == null) {
            return;
        }

        goodsService.findOne(id).success(
            function (response) {
                $scope.entity = response;
                //向富文本添加商品介绍
                editor.html($scope.entity.goodsDesc.introduction);
                //展示图片列表
                $scope.entity.goodsDesc.itemImages = JSON.parse($scope.entity.goodsDesc.itemImages);
                //展示扩展属性
                $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.entity.goodsDesc.customAttributeItems);
                //显示规格
                $scope.entity.goodsDesc.specificationItems = JSON.parse($scope.entity.goodsDesc.specificationItems)
                //SPU列表转换对象
                for (var i = 0; i < $scope.entity.itemList.length; i++) {
                    $scope.entity.itemList[i].spec = JSON.parse($scope.entity.itemList[i].spec);
                }

                findItemCat1($scope.entity.goods.category1Id);
                findItemCat2($scope.entity.goods.category2Id);
                findItemCat3($scope.entity.goods.category3Id);
                findBrand($scope.entity.goods.brandId);
            }
        );
    }


    findItemCat1 = function (id) {
        itemCatService.findOne(id).success(
            function (response) {
                $scope.itemCat1 = response;
            }
        )
    }
    findItemCat2 = function (id) {
        itemCatService.findOne(id).success(
            function (response) {
                $scope.itemCat2 = response;
            }
        )
    }
    findItemCat3 = function (id) {
        itemCatService.findOne(id).success(
            function (response) {
                $scope.itemCat3 = response;
            }
        )
    }

   findBrand = function (id) {
        brandService.findOne(id).success(
            function (response) {
                $scope.brand = response;
            }
        )
    }


    //保存
    $scope.save = function () {
        var serviceObject;//服务层对象
        if ($scope.entity.id != null) {//如果有ID
            serviceObject = goodsService.update($scope.entity); //修改
        } else {
            serviceObject = goodsService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    //重新查询
                    $scope.reloadList();//重新加载
                } else {
                    alert(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        goodsService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//刷新列表
                    $scope.selectIds = [];
                }
            }
        );
    }

    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (page, rows) {
        goodsService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //商品状态审核
    $scope.status = ['未审核', '已审核', '审核未通过', '关闭'];
    //商品列表
    $scope.itemCatList = [];
    //查询商品分类
    $scope.findItemCatList = function () {
        itemCatService.findAll().success(
            function (response) {
                for (var i = 0; i < response.length; i++) {
                    $scope.itemCatList[response[i].id] = response[i].name;
                }

            }
        )
    }


    //批量修改状态
    $scope.updateStatus = function (status) {
        goodsService.updateStatus($scope.selectIds, status).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();
                } else {
                    alert(response.message)
                }
            }
        )
    }


});
