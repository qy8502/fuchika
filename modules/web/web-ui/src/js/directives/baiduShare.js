angular.module('MyApp')
    .directive('baiduShare', ['$filter', function ($filter) {
        return {
            restrict: 'E',
            transclude: true,
            template: '<div class="bdsharebuttonbox" data-tag="{{dataTag}}" ng-transclude></div>',
            scope: {
                dataTag: '@tag',
                data: '='
            },
            link: function (scope, element, attributes, ngModel) {
                /*if (!scope.dataTag) {
                    scope.dataTag = "default";
                }*/
                function loadBaiduShare() {
                    console.log("Load BaiduShare " + scope.dataTag);
                    window._bd_share_config = window._bd_share_config || {};
                    window._bd_share_config.share = window._bd_share_config.share || [];
                    var sharedata = {tag: scope.dataTag};
                    angular.extend(sharedata, scope.data || {});
                    var oldsharedata = $filter('filter')(window._bd_share_config.share, {tag: scope.dataTag});
                    if (oldsharedata.length) {
                        oldsharedata[0] = sharedata;
                    } else {
                        window._bd_share_config.share.push(sharedata);
                    }
                    if (window._bd_share_main) {
                        window._bd_share_main.init();
                    } else if (angular.isUndefined(window._bd_share_main)) {
                        window._bd_share_main = null;
                        with (document)0[(getElementsByTagName('head')[0] || body).appendChild(createElement('script')).src = 'http://bdimg.share.baidu.com/static/api/js/share.js?v=89860593.js?cdnversion=' + ~(-new Date() / 36e5)]
                    }
                }

                scope.$watch("data", function (nv, ov) {
                    loadBaiduShare();
                });

            }
        };
    }]);

