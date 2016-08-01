angular.module('MyApp')
    .controller('SignupCtrl', function ($scope, $location, AppMessager, AccountService) {
        $scope.signup = function () {
            AccountService.signup($scope.user)
                .then(function (response) {
                    $location.path('/');
                    AppMessager.info('You have successfully created a new account and have been signed-in');
                })
                .catch(function (response) {
                    AppMessager.error(response);
                });
        };
    });