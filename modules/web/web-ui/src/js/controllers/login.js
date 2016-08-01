angular.module('MyApp')
    .controller('LoginCtrl', function ($scope, $location, $httpParamSerializer, AccountService, AppMessager) {
        $scope.login = function () {
            AccountService.login($scope.user)
                .then(function () {
                    AppMessager.success('You have successfully signed in');
                    $location.url(decodeURIComponent($location.search().url) || '/');
                })
                .catch(function (response) {
                    AppMessager.errorResponse(response);
                });
        };
        $scope.authenticate = function (provider) {
            AccountService.authenticate(provider)
                .then(function () {
                    AppMessager.success('You have successfully signed in with ' + provider);
                    $location.url(decodeURIComponent($location.search().url) || '/');
                })
                .catch(function (response) {
                    AppMessager.errorResponse(response);
                });
        };
    });