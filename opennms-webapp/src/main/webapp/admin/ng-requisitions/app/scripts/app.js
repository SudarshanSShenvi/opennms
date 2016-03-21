/**
* @author Alejandro Galue <agalue@opennms.org>
* @copyright 2014 The OpenNMS Group, Inc.
*/

(function() {

  'use strict';

  angular.module('onms-requisitions', [
    'ngRoute',
    'ngAnimate',
    'ui.bootstrap',
    'angular-growl',
    'angular-loading-bar'
  ])

  .factory('authHttpResponseInterceptor',['$q','$window', function($q, $window) {
    return {
      response: function(response) {
        return response || $q.when(response);
      },
      responseError: function(rejection) {
        if (rejection.status === 401) {
          $window.location = '/opennms/login.jsp';
        }
        return $q.reject(rejection);
      }
    };
  }])

  .config(['$httpProvider',function($httpProvider) {
    $httpProvider.interceptors.push('authHttpResponseInterceptor');
  }])

  .config(['$routeProvider', function ($routeProvider) {
    $routeProvider
    .when('/requisitions', {
      templateUrl: 'views/requisitions.html',
      controller: 'RequisitionsController'
    })
    .when('/requisitions/:foreignSource', {
      templateUrl: 'views/requisition.html',
      controller: 'RequisitionController'
    })
    .when('/requisitions/:foreignSource/foreignSource', {
      templateUrl: 'views/foreignsource.html',
      controller: 'ForeignSourceController'
    })
    .when('/requisitions/:foreignSource/nodes/:foreignId', {
      templateUrl: 'views/node.html',
      controller: 'NodeController'
    })
    .when('/requisitions/:foreignSource/nodes/:foreignId/vertical', {
      templateUrl: 'views/node-panels.html',
      controller: 'NodeController'
    })
    .otherwise({
      redirectTo: '/requisitions'
    });
  }])

  .config(['growlProvider', function(growlProvider) {
    growlProvider.globalTimeToLive(3000);
    growlProvider.globalPosition('bottom-center');
  }])

  .config(['$uibTooltipProvider', function($uibTooltipProvider) {
    $uibTooltipProvider.setTriggers({
      'mouseenter': 'mouseleave'
    });
    $uibTooltipProvider.options({
      'placement': 'left',
      'trigger': 'mouseenter'
    });
  }]);

}());
