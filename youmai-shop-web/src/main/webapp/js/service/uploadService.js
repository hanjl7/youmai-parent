app.service("uploadService",function ($http) {
   
    //上传文件
    this.uploadFile=function () {
        var formData = new FormData();
        formData.append('file',file.files[0])

        return $http({
            url:'../upload.do',
            method:'post',
            data:formData,
            headers:{'Content-Type':undefined},
            transformRequest:angular.identity
        })
    }
});