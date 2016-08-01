angular.module('MyApp')
    .controller('NavbarCtrl', function ($scope, $rootScope, $location, AccountService) {
//        $scope.isAuthenticated = function () {
//            console.info("auth");
//            $scope.user = AccountService.getCurrentUser();
//            return $auth.isAuthenticated();
//        };
        $scope.isActive = function (viewLocation) {
            return viewLocation === $location.path() || $location.path().indexOf(viewLocation + "/") == 0;
        };
        $rootScope.$watch(AccountService.isLogin,
            function (isLogin) {
                if (isLogin) {
                    $rootScope.isAuthenticated = true;
                    $rootScope.currentUser = AccountService.currentUser();
                } else {
                    $rootScope.isAuthenticated = false;
                    $rootScope.currentUser = AccountService.currentUser();
                }
            }
        )
        ;
    });