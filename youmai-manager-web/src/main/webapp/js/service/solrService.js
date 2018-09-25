app.service("solrService", function ($http) {

    this.updateSolr=function () {
        return $http.get('../updateSolr.do');
    }
});
