//品牌服务
app.service("brandService", function ($http) {
    this.findAll = function () {
        return $http.get("../brand/findAll.do");
    };

    this.findPage = function (page, size) {
        return $http.get('../brand/findPage.do?page=' + page + '&size=' + size)
    };

    this.add = function (entity) {
        return $http.post('../brand/add.do', entity);
    };

    this.update = function (entity) {
        return $http.post('../brand/update.do', entity);
    };
    this.findOne = function (id) {
        return $http.get("../brand/findOne.do?id=" + id);
    };

    this.deleteBrands = function (selectIds) {
        return $http.get("../brand/delete.do?ids=" + selectIds);
    };

    this.search = function (page, size, searchEntity) {
        return $http.post('../brand/search.do?page=' + page + '&size=' + size, searchEntity);
    }
});
