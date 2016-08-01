angular.module('MyApp')
    .config(function ($provide) {
        var scrollbarSize = {width: 17, height: 17};
        $provide.value("scrollbarSize", scrollbarSize);
        function getScrollBarSize() {
            var inner = document.createElement('p');
            inner.style.width = "100%";
            inner.style.height = "100%";

            var outer = document.createElement('div');
            outer.style.position = "absolute";
            outer.style.top = "0px";
            outer.style.left = "0px";
            outer.style.visibility = "hidden";
            outer.style.width = "100px";
            outer.style.height = "100px";
            outer.style.overflow = "hidden";
            outer.appendChild(inner);

            document.body.appendChild(outer);

            var w1 = inner.offsetWidth;
            var h1 = inner.offsetHeight;
            outer.style.overflow = 'scroll';
            var w2 = inner.offsetWidth;
            var h2 = inner.offsetHeight;
            if (w1 == w2) w2 = outer.clientWidth;
            if (h1 == h2) h2 = outer.clientHeight;

            document.body.removeChild(outer);

            scrollbarSize.width = (w1 - w2);
            scrollbarSize.height = (h1 - h2)
        }

        getScrollBarSize();
        $(window).resize(getScrollBarSize);
    })
    .directive('heightScrollbar', ["scrollbarSize", function (scrollbarSize) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                element.css("height", scrollbarSize.height);
                $(window).resize(function () {
                    element.css("height", scrollbarSize.height);
                });
            }
        };
    }])
    .directive('scrollSync', ["scrollbarSize", function (scrollbarSize) {
        return {
            scope: {"scrolled": "&"},
            restrict: 'A',
            link: function (scope, element, attrs) {
                var $scrollSync = $('#' + attrs.scrollSync);

                function scroll() {
                    element.scrollTop($scrollSync.scrollTop());
                    element.scrollLeft($scrollSync.scrollLeft());
                    scope.scrolled({ top: $scrollSync.scrollTop(), left: $scrollSync.scrollLeft(), height: $scrollSync.height(), width: $scrollSync.width()});

                }

                $scrollSync.on('scroll', function () {
                    scroll();
                    scope.$apply();
                });
                scroll();
            }
        };
    }])
    .directive('affixScrollbar', ["scrollbarSize", function (scrollbarSize) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {


                var minHeight = parseInt(attrs.minHeight) || 0;

                function resizeHeight() {
                    var windowBottomHeight = $(document).scrollTop() + $(window).height();
                    //var scrollbarHeight = element.outerHeight(true) - element.innerHeight();
                    if (windowBottomHeight <
                        element.offset().top + element[0].scrollHeight + scrollbarSize.height) {
                        var newHeight = windowBottomHeight - element.offset().top;
                        if (newHeight > minHeight + scrollbarSize.height) {
                            element.css('height', newHeight);
                        } else if (newHeight > 0) {
                            //$('.task-gantt-list').css('height', newHeight);
                        } else {
                            element.css('height', "");
                        }
                    } else {
                        element.css('height', "");
                    }
                }

                $(element).on('scroll', function () {
                    $(document).scrollTop($(document).scrollTop() + element.scrollTop());
                    element.scrollTop(0);
                });

                $(document).on('scroll', resizeHeight);
                $(window).resize(resizeHeight);
                $(window).on("DOMMouseScroll", resizeHeight);
            }
        };
    }])
    .directive('affix', ["scrollbarSize", function (scrollbarSize) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var offset = {};
                if (attrs.offsetTarget) {
                    var $offsetTarget = $('#' + attrs.offsetTarget);
                    offset.top = function () {
                        return $offsetTarget.offset().top - parseInt(attrs.offsetTop || 0);
                    };
                    offset.bottom = function () {
                        return $(document).height() - $offsetTarget.offset().top - $offsetTarget.height() + parseInt(attrs.offsetBottom || 0) + (attrs.offsetTargetScrollbar === "true" ? scrollbarSize.height : 0);
                    }
                } else {
                    offset.top = parseInt(attrs.offsetTop || 0);
                    offset.bottom = parseInt(attrs.offsetBottom || 0);
                }

                var $widthTarget, $innerWidthTarget, $outerWidthTarget;
                if (attrs.widthTarget) {
                    $widthTarget = $('#' + attrs.widthTarget);
                } else if (attrs.innerWidthTarget) {
                    $innerWidthTarget = $('#' + attrs.innerWidthTarget);
                } else if (attrs.outerWidthTarget) {
                    $outerWidthTarget = $('#' + attrs.outerWidthTarget);
                }

                var resizeWidth = function () {
                    if ($widthTarget && $widthTarget.length && $widthTarget.width() >= 0) {
                        element.css("width", $widthTarget.width());
                    } else if ($innerWidthTarget && $innerWidthTarget.length && $innerWidthTarget.innerWidth() >= 0) {
                        element.css("width", $innerWidthTarget.innerWidth());
                    } else if ($outerWidthTarget && $outerWidthTarget.length && $outerWidthTarget.outerWidth() >= 0) {
                        element.css("width", $outerWidthTarget.outerWidth());
                    }
                };

                element.affix({offset: offset});
                var isChanged = false;
                var i=0;
                function changed() {
                    if (!isChanged) {
                        isChanged = true;
                        setTimeout(function () {
                            console.log("DOMSubtreeModified"+i++);
                            element.trigger('scroll.bs.affix.data-api');
                            resizeWidth();
                            isChanged = false;
                        }, 1)
                    }
                }
                scope.$watch(function () {
                    return document.body.innerHTML;
                },changed );

                $(window).resize(changed);

                $(window).on("DOMMouseScroll", changed);
            }
        };
    }]);

