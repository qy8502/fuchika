angular.module('MyApp', ['ngResource', 'ngMessages', 'ngAnimate', 'ui.router', 'ui.bootstrap', 'toastr', 'duScroll'])
    .constant('AppConfig', {
        apiBaseUrl: 'http://api.fuchika.com',
        apiAuthorizationBasicHeader: 'Basic MzUzYjMwMmM0NDU3NGY1NjUwNDU2ODdlNTM0ZTdkNmE6Mjg2OTI0Njk3ZTYxNWE2NzJhNjQ2YTQ5MzU0NTY0NmM='
    })
    .value('duScrollOffset', 70)
    .config(function ($stateProvider, $urlRouterProvider) {

        $stateProvider
            .state('home', {
                url: '/',
                controller: 'HomeCtrl',
                templateUrl: '../templates/home.html'
            })
            .state('project', {
                url: '/project',
                controller: 'ProjectCtrl',
                templateUrl: '../templates/project.html'
            })
            .state('projectDetail', {
                url: '/project/:projectId',
                controller: 'ProjectDetailCtrl',
                templateUrl: '../templates/project-detail.html'
            })
            .state('schedule', {
                url: '/schedule',
                controller: 'ProjectCtrl',
                templateUrl: '../templates/project.html',
                resolve: {
                    loginRequired: loginRequired
                }
            })
            .state('login', {
                url: '/login',
                templateUrl: '../templates/login.html',
                controller: 'LoginCtrl',
                resolve: {
                    skipIfLoggedIn: skipIfLoggedIn
                }
            })
            .state('signup', {
                url: '/signup',
                templateUrl: '../templates/signup.html',
                controller: 'SignupCtrl',
                resolve: {
                    skipIfLoggedIn: skipIfLoggedIn
                }
            })
            .state('logout', {
                url: '/logout',
                template: null,
                controller: 'LogoutCtrl'
            })
            .state('profile', {
                url: '/profile',
                templateUrl: '../templates/profile.html',
                controller: 'ProfileCtrl',
                resolve: {
                    loginRequired: loginRequired
                }
            });

        $urlRouterProvider.otherwise('/');


        function skipIfLoggedIn($q, $location, AccountService) {
            var deferred = $q.defer();
            if (AccountService.isAuthenticated()) {
                $location.path('/');
                deferred.reject();
            } else {
                deferred.resolve();
            }
            return deferred.promise;
        }

        function loginRequired($q, $location, AccountService, AppMessager) {
            var deferred = $q.defer();
            if (AccountService.isAuthenticated()) {
                deferred.resolve();
            } else {
                AppMessager.error("用户尚未登录，请先登录网站。", 401);
                var url = encodeURIComponent($location.url());
                $location.path('/login').search({url: url});
            }
            return deferred.promise;
        }
    })
    .factory('AppMessager', ['toastr', '$location', function (toastr, $location) {
        var messager = {
            errorResponse: function (response, defaultMessage) {
                defaultMessage = defaultMessage || "发生意外错误... (⊙⊙！) "
                var that = this;
                if (response.status == 401) {
                    var url = encodeURIComponent($location.url());
                    that.error('当前登录状态已经失效，请<a href="/#/login?url=' + url + '">重新登录</a>。', response.status.toString(), {
                        allowHtml: true
                    });
                } else if (response.data) {
                    if (response.data.validationErrors && response.data.validationErrors.length) {
                        angular.forEach(response.data.validationErrors, function (item) {
                            that.error(item.message, response.status);
                        })
                    } else if (response.data.consumerMessage) {
                        that.error(response.data.consumerMessage, response.status);
                    } else if (response.data.message) {
                        that.error(response.data.message, response.status);
                    } else if (response.data.error) {
                        that.error(response.data.error, response.status);
                    } else {
                        that.error(defaultMessage, response.status);
                    }
                } else if (response.message) {
                    that.error(response.message, response.status);
                } else {
                    that.error(defaultMessage, response.status);
                }
            }
        };
        angular.extend(messager, toastr);
        return messager;
    }])
    .run();
