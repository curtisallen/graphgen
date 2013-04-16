'use strict';

angular.module('graphApp')
  .controller('MainCtrl', function ($scope, $http, $log) {
    $scope.data = {};
    $scope.rexterServer = 'http://localhost:8182/graphs/mongodbgraph/mis/d3graph';

    // this header messes rexter up
    delete $http.defaults.headers.common['X-Requested-With'];

    $scope.init = function () {
    	$scope.getGraph();
    };

    $scope.getGraph = function () {
    	$http.get($scope.rexterServer).success(function (data) {
    		//console.log(angular.toJson(data));
    		$scope.data = data;
    	})
    };

    $scope.deleteGraph = function () {
    	$http.delete('/service/graphgen').error(function (err) {
    		$log.error("Couldn't clear graph" + angular.toJson(err));
    	}).success(function () {
    		$scope.getGraph();
    	});
    };


  });
