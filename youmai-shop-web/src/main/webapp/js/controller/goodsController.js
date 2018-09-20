//控制层
app.controller('goodsController', function ($scope, $controller, $location, goodsService, uploadService, itemCatService, typeTemplateService) {

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
                    $scope.entity.itemList[i].spec=JSON.parse($scope.entity.itemList[i].spec);
                }

            }
        );
    }

    //保存
    $scope.save = function () {
        $scope.entity.goodsDesc.introduction = editor.html();

        var serviceObject;
        if ($scope.entity.goods.id != null){
            serviceObject=goodsService.update($scope.entity);
        }else {
            serviceObject=goodsService.add($scope.entity);
        }
        serviceObject.success(
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

    //定义页面实体结构🎈
    $scope.entity = {goods: {}, goodsDesc: {itemImages: [], specificationItems: []}};


    //把当前上传的图片添加到图片列表
    $scope.add_image_entity = function () {
        $scope.entity.goodsDesc.itemImages.push($scope.image_entity)
    }

    //移除图片
    $scope.remove_image_entity = function (index) {
        $scope.entity.goodsDesc.itemImages.splice(index, 1);
    }
    //查询一级分类
    $scope.selectItemCat1List = function () {
        itemCatService.findByParentId(0).success(
            function (response) {
                $scope.itemCat1List = response;
            }
        )
    }
    //查询二级分类
    $scope.$watch("entity.goods.category1Id", function (newValue, oldValue) {
        //根据一级分类的id查询二级分类 $watch方法用于监控某个变量的值，当被监控的值发生变化，就自动执行相应的函数
        itemCatService.findByParentId(newValue).success(
            function (response) {
                $scope.itemCat2List = response;
            }
        )
    })
    //查询三级分类
    $scope.$watch("entity.goods.category2Id", function (newValue, oldValue) {
        itemCatService.findByParentId(newValue).success(
            function (response) {
                $scope.itemCat3List = response;
            }
        )
    })
    //查询模板id
    $scope.$watch("entity.goods.category3Id", function (newValue, oldValue) {
        itemCatService.findOne(newValue).success(
            function (response) {
                //跟新模板id
                $scope.entity.goods.typeTemplateId = response.typeId;
            }
        )
    })
    //模板id选择后，更新品牌列表,读取扩展属性
    $scope.$watch("entity.goods.typeTemplateId", function (newValue, oldValue) {
        typeTemplateService.findOne(newValue).success(
            function (response) {
                //获取类型模板
                $scope.typeTemplate = response;
                //品牌列表
                $scope.typeTemplate.brandIds = JSON.parse($scope.typeTemplate.brandIds);

                //如果没有id,就加载模板中的扩展数据
                if ($location.search()['id'] == null) {
                    //扩展属性
                    $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.typeTemplate.customAttributeItems);
                }
            }
        );
        //同时查询规格列表
        typeTemplateService.findSpecList(newValue).success(
            function (response) {
                $scope.specList = response;
            }
        )
    })

    //获取规格添加
    $scope.updateSpecAttribute = function ($event, name, value) {
        var object = $scope.searchObjectByKey($scope.entity.goodsDesc.specificationItems, 'attributeName', name);
        if (object != null) {
            if ($event.target.checked) {
                object.attributeValue.push(value);
            } else {
                //取消勾选
                object.attributeValue.splice(object.attributeValue.indexOf(value), 1);//移除选项
                //如果选项都取消，此条记录移除
                if (object.attributeValue.length == 0) {
                    $scope.entity.goodsDesc.specificationItems.splice(
                        $scope.entity.goodsDesc.specificationItems.indexOf(object), 1);
                }
            }
        } else {
            $scope.entity.goodsDesc.specificationItems.push({"attributeName": name, "attributeValue": [value]})
        }
    }

    //根据规格名称和选项名称返回查看是否被勾选
    $scope.checkAttributeValue = function (specName, optionName) {
        var items = $scope.entity.goodsDesc.specificationItems;
        var object = $scope.searchObjectByKey(items, 'attributeName', specName);

        if (object == null) {
            return false;
        } else {
            if (object.attributeValue.indexOf(optionName) >= 0) {
                return true;
            } else {
                return false;
            }
        }
    }

    //创建SKU列表
    $scope.createItemList = function () {
        $scope.entity.itemList = [{spec: {}, price: 0, num: 9999, status: '0', isDefault: '0'}];//列表初始

        var items = $scope.entity.goodsDesc.specificationItems;
        for (var i = 0; i < items.length; i++) {
            $scope.entity.itemList = addColumn($scope.entity.itemList, items[i].attributeName, items[i].attributeValue);
        }
    };
    //添加列值
    addColumn = function (list, columnName, columnValues) {
        var newList = [];//新集合
        for (var i = 0; i < list.length; i++) {
            var oldRow = list[i];
            for (var j = 0; j < columnValues.length; j++) {
                var newRow = JSON.parse(JSON.stringify(oldRow));
                newRow.spec[columnName] = columnValues[j];
                newList.push(newRow);
            }
        }
        return newList;
    }

    $scope.status = ['未审核', '已审核', '审核未通过', '关闭'];

    //商品分类列表
    $scope.itemCatList = [];

    $scope.findItemCatList = function () {
        itemCatService.findAll().success(
            function (response) {
                for (var i = 0; i < response.length; i++) {
                    $scope.itemCatList[response[i].id] = response[i].name;
                }
            }
        )
    }
});
