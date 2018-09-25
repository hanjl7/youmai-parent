app.controller('solrController', function ($scope, solrService) {
    $scope.updateSolr = function () {
       solrService.updateSolr().success(
           function (response) {
               if (response.success){
                   alert(response.message);
               } else {
                   alert(response.message);
               }
           }
       )
    }

})