var app = angular.module('hyperstate', []);

var parseLocation = function(href) {
    var location = document.createElement("a");
    location.href = href;
    // IE doesn't populate all link properties when setting .href with a
    // relative URL,
    // however .href will return an absolute URL which then can be used on
    // itself
    // to populate these additional fields.
    if (location.host == "") {
        location.href = location.href;
    }
    return location;
};

app.config(function($locationProvider, $httpProvider) {
    $locationProvider.html5Mode(true);
    $httpProvider.defaults.cache = false;
    $httpProvider.defaults.headers.common.Accept = 'application/vnd.siren+json';

    var handleResponse = function(response, scope) {
        console.log('response', new Date());
        console.log(response);
        scope.status = response.status;
        scope.entity = response.data;
        if (scope.debug) {
            scope.debugData = "" + JSON.stringify(response.data, null, 2);
        }
        scope.loading = false;
    }

    var interceptor = [ '$q', '$rootScope', function($q, $rootScope) {
        return {
            'request' : function(config) {
                $rootScope.loading = true;
                $rootScope.entity = null;
                $rootScope.requestError = null;
                $rootScope.debugData = null
                console.log('request', new Date());
                console.log(config);
                return config;
            },

            'requestError' : function(rejection) {
                console.log('requestError', new Date());
                console.log(rejection);
                $rootScope.requestError = "Whoa! What just happend? I can't talk to my brains!"
                $rootScope.status = 400;
                $rootScope.loading = false;
                return $q.reject(rejection);
            },

            'response' : function(response) {
                handleResponse(response, $rootScope);
                return response;
            },

            'responseError' : function(rejection) {
                handleResponse(rejection, $rootScope);
                return $q.reject(rejection);
            }
        };
    } ];
    $httpProvider.interceptors.push(interceptor);
});

app.controller('EntityController', function($scope, $http, $location, $window, $rootScope) {
    var controller = this;
    $rootScope.appUrl = $window.location.href
    $rootScope.appName = "Hyperstate Tester"

    $rootScope.debug = true;
    $rootScope.debugData = "";

    $rootScope.loading = true;

    controller.successCallback = function(response) {
        if (response.headers("Location")) {
            var location = parseLocation(response.headers("Location"));
            var currLoc = parseLocation($location.absUrl());
            if (location.protocol === currLoc.protocol && location.host === currLoc.host) {

                $location.url(location.pathname + location.search + location.hash);

                controller.doLoad($location.url());
            } else {
                $window.location.href = location.href;
            }
        } else if (response.status === 204) {
            controller.doLoad($location.url());
        }
    };

    
    controller.doLoad = function(href) {
        $http.get(href).then(controller.successCallback);
    }

    controller.doLoad($window.location.href);

    controller.processForm = function(form) {
        console.log("processForm");
        console.log(form);
        var action = form.action;
        $http({
            method : action.method || "GET",
            url : action.href,
            data : $.param(action.fields), // pass in data as strings
            headers : {
                'Content-Type' : action.type || "application/x-www-form-urlencoded"
            }
        }).then(controller.successCallback);
        return false;
    };

    controller.processNavClick = function(event) {
        console.log("processNavClick");
        console.log(event);
        controller.doLoad(event.target.href);
    };


    $scope.$on('$locationChangeStart', function(event, newUrl, oldUrl) {
        console.log('$locationChangeStart:', oldUrl + " -> " + newUrl, new Date());
    });

    $scope.$on('$locationChangeSuccess', function(event, newUrl, oldUrl) {
        console.log('$locationChangeSuccess:', oldUrl + " -> " + newUrl, new Date());
        controller.doLoad(newUrl);
    });
});