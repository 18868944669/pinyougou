//��Ʒ��ϸҳ�����Ʋ㣩
app.controller('itemController',function($scope){
//��������
$scope.addNum=function(x){
$scope.num=$scope.num+x;
if($scope.num<1){
$scope.num=1;
}
}

$scope.specificationItems={};//��¼�û�ѡ��Ĺ��
//�û�ѡ����
$scope.selectSpecification=function(name,value){
$scope.specificationItems[name]=value;
}
//�ж�ĳ���ѡ���Ƿ��û�ѡ��
$scope.isSelected=function(name,value){
if($scope.specificationItems[name]==value){
return true;
}else{
return false;
}
}

//����Ĭ�� SKU
$scope.loadSku=function(){
$scope.sku=skuList[0];
$scope.specificationItems= JSON.parse(JSON.stringify($scope.sku.spec)) ;
}
});