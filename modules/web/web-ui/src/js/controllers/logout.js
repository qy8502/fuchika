angular.module('MyApp')
    .controller('LogoutCtrl', function ($location, AppMessager, AccountService) {
        if (!AccountService.isAuthenticated()) {
            return;
        }
        AccountService.logout()
            .then(function () {
                AppMessager.info('You have been logged out');
                $location.path('/');
            });
    });