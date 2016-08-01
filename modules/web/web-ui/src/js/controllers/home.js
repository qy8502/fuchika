angular.module('MyApp')
    .controller('HomeCtrl', function ($scope, $http) {
        var words = ['这么多事情', '根本做不完嘛', '施展魔法', '咐叱喀！！！'];
        function changeWord() {
            nextIndex = words.indexOf($scope.word) + 1;
            nextIndex = nextIndex < words.length ? nextIndex : 0;
            setTimeout(changeWord, 3000);
            $scope.$apply(function() {
                $scope.word = words[nextIndex];
            });
        }
        setTimeout(changeWord, 0);

    });