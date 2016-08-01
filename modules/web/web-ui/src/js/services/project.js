angular.module('MyApp')
    .service('ProjectService', ['$q', '$http', 'AppConfig',
        function ($q, $http, appConfig) {
            return  {
                createProject: function (requestData) {
                    return $http.post(appConfig.apiBaseUrl + '/project/', requestData);
                },
                getProjectListOwn: function () {
                    return $http.get('/test/projectListOwn.json');
                },
                getProjectListJoin: function () {
                    return $http.get('/test/projectListOwn.json');
                },
                getProjectListFollow: function () {
                    return $http.get('/test/projectListOwn.json');
                },
                getProjectListPop: function () {
                    return $http.get('/test/projectListOwn.json');
                },
                getTagListPop: function () {
                    return $http.get('/test/tagListPop.json');
                },
                getProject: function (projectId) {
                    return $http.get('/test/project.json');
                }
            };
        }]);