//控制层
app.controller('goodsController', function ($scope, $controller, goodsService, uploadService,itemCatService) {

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
        goodsService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    }

    //保存
    $scope.save = function () {
        goodsService.update($scope.entity).success(
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

    //添加
    $scope.add = function () {
        $scope.entity.goodsDesc.introduction = editor.html();

        goodsService.add($scope.entity).success(
            function (response) {
                if (response.success) {
                    //重新查询
                    alert("添加成功")
                    //添加成功后，清空
                    $scope.entity = {};
                    //清空富文本编辑器
                    editor.html("");
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


    //上传文件
    $scope.uploadFile = function () {
        uploadService.uploadFile().success(
            function (response) {
                if (response.success) {

                    //上传成功获取地址
                    $scope.image_entity.url = response.message;
                } else {
                    alert(response.message);
                }
            }
        ).error(function () {
            alert("上传发生错误")
        })
    }

    //定义页面实体结构
    $scope.entity={goods:{},goodsDesc:{itemImages:[]}};
    //把当前上传的图片添加到图片列表
    $scope.add_image_entity=function () {
        $scope.entity.goodsDesc.itemImages.push($scope.image_entity)
    }

    //移除图片
    $scope.remove_image_entity=function (index) {
        $scope.entity.goodsDesc.itemImages.splice(index,1);
    }
    //查询一级分类
    $scope.selectItemCat1List=function () {
        itemCatService.findByParentId(0).success(
            function (response) {
                $scope.itemCat1List=response;
            }
        )
    }
    //查询二级分类
    $scope.$watch("entity.goods.category1Id",function (newValue,oldValue) {
        //根据一级分类的id查询二级分类 $watch方法用于监控某个变量的值，当被监控的值发生变化，就自动执行相应的函数
        itemCatService.findByParentId(newValue).success(
            function (response) {
                $scope.itemCat2List=response;
            }
        )
    })
    //查询三级分类
    $scope.$watch("entity.goods.category2Id",function (newValue,oldValue) {
        itemCatService.findByParentId(newValue).success(
            function (response) {
                $scope.itemCat3List=response;
            }
        )
    })
    //查询模板id
    $scope.$watch("entity.goods.category3Id",function (newValue,oldValue) {
        itemCatService.findOne(newValue).success(
            function (response) {
                //跟新模板id
                $scope.entity.goods.typeTemplateId=response.typeId;
            }
        )
    })
});
