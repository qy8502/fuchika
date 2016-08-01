angular.module('MyApp')
    .directive('projectList', function () {
        return {
            restrict: 'E',
            templateUrl: '/templates/project-list.html',
            scope: {
                prefix: '@',
                projectList: '='
            },
            link: function (scope, element, attributes, ngModel) {
                scope.$watch("prefix", function (nv, ov) {
                    if (!nv) {
                        scope.prefix = 'project';
                    }
                });
            }
        };
    });

