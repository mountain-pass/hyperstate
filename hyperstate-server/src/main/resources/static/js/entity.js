var app = angular.module('hyperstate', []);

app.config(function($locationProvider, $httpProvider) {
    $locationProvider.html5Mode(true);
    $httpProvider.defaults.cache = false;
    $httpProvider.defaults.headers.common.Accept = 'application/vnd.siren+json';

    var handleResponse = function(response, scope) {
        console.log('response', response, new Date());
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
                console.log('request', config, new Date());
                return config;
            },

            'requestError' : function(rejection) {
                console.log('requestError', rejection, new Date());
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
    
    // TODO: fix app URL. It's only correct when you enter the page from the base URL
    $rootScope.appUrl = $window.location.href
    $rootScope.appName = "Hyperstate Tester"

    $rootScope.debug = true;
    $rootScope.debugData = "";

    $rootScope.loading = true;

    controller.parseLocation = function(href) {
        var location = document.createElement("a");
        location.href = href;
        // IE doesn't populate all link properties when setting .href with a
        // relative URL,
        // however .href will return an absolute URL which then can be used on
        // itself
        // to populate these additional fields.
        if (location.host === "") {
            location.href = location.href;
        }
        return location;
    };

    
    controller.successCallback = function(response) {
        if (response.headers("Location")) {
            var newLoc = controller.parseLocation(response.headers("Location"));
            var currLoc = controller.parseLocation($location.absUrl());
            if (newLoc.protocol === currLoc.protocol && newLoc.host === currLoc.host) {

                $location.url(newLoc.pathname + newLoc.search + newLoc.hash);

                controller.doLoad($location.url());
            } else {
                $window.location.href = newLoc.href;
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
        console.log("processForm", form, new Date());
        var action = form.action;
        var method = action.method || "GET";
        var href = action.href;
        var data = $.param(action.fields);
        var headers = {
                'Content-Type' : action.type || "application/x-www-form-urlencoded"
        }
        if( method === "GET" ) {
            href = href + "?" + data;
            data = "";
            headers = {};
        }
        $http({
            method : method,
            url : href,
            data : data, // pass in data as strings
            headers : headers
        }).then(controller.successCallback);
        return false;
    };

    controller.processNavClick = function(event) {
        console.log("processNavClick", event, new Date());
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