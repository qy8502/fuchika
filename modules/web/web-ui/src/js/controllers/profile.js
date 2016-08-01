angular.module('MyApp')
    .controller('ProfileCtrl', function ($location, $scope, AppMessager, AccountService) {
        $scope.getProfile = function () {
            AccountService.getMe()
                .then(function (response) {
                    $scope.user = response.data;
                })
                .catch(function (response) {
                    AppMessager.errorResponse(response);
                });
        };
        $scope.updateProfile = function () {
            AccountService.updateMe($scope.user)
                .then(function () {
                    AppMessager.success('Profile has been updated');
                })
                .catch(function (response) {
                    AppMessager.errorResponse(response);
                });
        };
        $scope.link = function (provider) {
            AccountService.link(provider)
                .then(function () {
                    AppMessager.success('You have successfully linked a ' + provider + ' account');
                    $scope.getProfile();
                })
                .catch(function (response) {
                    AppMessager.errorResponse(response);
                });
        };
        $scope.unlink = function (provider) {
            AccountService.unlink(provider)
                .then(function () {
                    AppMessager.info('You have unlinked a ' + provider + ' account');
                    $scope.getProfile();
                })
                .catch(function (response) {
                    AppMessager.errorResponse(response, 'Could not unlink ' + provider + ' account');
                });
        };

        $scope.getProfile();
    });
