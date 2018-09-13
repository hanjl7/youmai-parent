//规范
app.service("specificationService", function ($http) {
    this.findAll=function (page,size) {
        return $http.get('../specification/findAll.do?page=' + page + '&size=' + size)
    };

    this.update=function (entity) {
        return $http.post('../specification/update.do',entity);
    };

    this.add=function (entity) {
        return $http.post('../specification/add.do',entity);
    };

    this.findOne=function (id) {
        return $http.get('../specification/findOne.do?id=' + id)
    };

    this.deleteSpec =function (ids) {
        return $http.get('../specification/delete.do?ids='+ids);
    };

    this.selectOptionList =function () {
        return $http.get('../specification/selectOptionList.do');
    }
});