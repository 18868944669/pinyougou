//文件上传服务层
app.service('uploadService', function ($http) {
    this.uploadFile = function () {
        var formData = new FormData();
        //file是文件上传框的name
        formData.append('file', file.files[0]);
        return $http({
            method: 'POST',
            url: "../upload.do",
            data: formData,
            //指定上传类型
            headers: {'Content-Type': undefined},
            //对表单进行二进制序列化
            transformRequest: angular.identity
        });
    }

});