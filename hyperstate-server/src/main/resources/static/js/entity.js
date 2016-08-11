var app = angular.module('hyperstate', []);

app.config(function($locationProvider, $httpProvider) {
    $locationProvider.html5Mode(true);
    $httpProvider.defaults.cache=false;
    $httpProvider.defaults.headers.common.Accept = 'application/vnd.siren+json';
    
    $httpProvider.interceptors.push(function($q) {
        return {
            'request' : function(config) {
                angular.element(document.getElementById('controller')).scope().controller.loading = true;
                console.log('request', new Date());
                console.log(config);
                return config;
            },

            'requestError' : function(rejection) {
                angular.element(document.getElementById('controller')).scope().controller.loading = false;
                console.log('requestError', new Date());
                console.log(rejection);
                return $q.reject(rejection);
            },

            'response' : function(response) {
                angular.element(document.getElementById('controller')).scope().controller.loading = false;
                console.log('response', new Date());
                console.log(response);
                return response;
            },

            'responseError' : function(rejection) {
                angular.element(document.getElementById('controller')).scope().controller.loading = false;
                console.log('responseError', new Date());
                console.log(rejection);
                return $q.reject(rejection);
            }
        };
    });
});


app.controller('EntityController', function($scope, $http, $location, $window) {
    var controller = this;
    
    controller.debug = true;
    
    controller.loading = true;


    controller.getLocation = function(href) {
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
    
    controller.errorCallback = function(response) {
        controller.status = response.status;
        controller.entity = response.data;
    };

    controller.successCallback = function(response) {
        controller.status = response.status;
        if (response.status === 200) {
            controller.entity = response.data;
        } else if (response.status === 201 || response.status === 204) {
            var location = controller.getLocation(response.headers("Location"));
            var currLoc = controller.getLocation($location.absUrl());
            if (location.protocol === currLoc.protocol && location.host === currLoc.host) {

                controller.entity = {};
                $location.url(location.pathname + location.search + location.hash);

                $http.get("" + location).then(controller.successCallback, controller.errorCallback);
            } else {
                controller.entity = response.data;
                $window.location.href = location;
            }
        } else {
            alert("TODO: handle " + response.status + " responses");
        }
    };

    $http.get($window.location.href, {
        cache : false
    }).then(controller.successCallback, controller.errorCallback);

    controller.processForm = function(form) {
        console.log("processForm");
        console.log(form);
        var action = form.action;
        controller.entity = {};
        $http({
            method : action.method || "GET",
            url : action.href,
            data : $.param(action.fields), // pass in data as strings
            headers : {
                'Content-Type' : action.type || "application/x-www-form-urlencoded"
            }
        }).then(controller.successCallback, controller.errorCallback);
        return false;
    };
    
    controller.processNavClick = function(event) {
        console.log("processNavClick");
        console.log(event);
        controller.entity = {};
        
        controller.doLoad(event.target.href);
    };

    controller.doLoad = function(href) {
        $http.get(href).then(controller.successCallback, controller.errorCallback);
    }
    
    $scope.$on('$locationChangeStart', function(event, newUrl, oldUrl) {
        console.log('$locationChangeStart:', oldUrl + " -> " + newUrl, new Date());
    });

    $scope.$on('$locationChangeSuccess', function(event, newUrl, oldUrl) {
        console.log('$locationChangeSuccess:', oldUrl + " -> " + newUrl, new Date());
        if (newUrl != null) {
            controller.doLoad(newUrl);
        }
    });
});