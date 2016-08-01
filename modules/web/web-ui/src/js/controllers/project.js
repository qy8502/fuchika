angular.module('MyApp')
    .controller('ProjectCtrl', function ($state, $scope, $uibModal, $log, ProjectService, AppMessager) {

        //$('#side-affix').affix({ offset: { top: 220, bottom: 200 } });

        ProjectService.getTagListPop()
            .then(function (response) {
                $scope.tagListPop = response.data;
            })
            .catch(function (response) {
                AppMessager.errorResponse(response);
            });

        ProjectService.getProjectListOwn()
            .then(function (response) {
                $scope.projectListOwn = response.data;
            })
            .catch(function (response) {
                AppMessager.errorResponse(response);
            });
        ProjectService.getProjectListJoin()
            .then(function (response) {
                $scope.projectListJoin = response.data;
            })
            .catch(function (response) {
                AppMessager.errorResponse(response);
            });
        ProjectService.getProjectListFollow()
            .then(function (response) {
                $scope.projectListFollow = response.data;
            })
            .catch(function (response) {
                AppMessager.errorResponse(response);
            });
        ProjectService.getProjectListPop()
            .then(function (response) {
                $scope.projectListPop = response.data;
            })
            .catch(function (response) {
                AppMessager.errorResponse(response);
            });

        $scope.openCreateProjectModal = function () {
            var modalInstance = $uibModal.open({
                templateUrl: 'createProjectModal.html',
                controller: 'CreateProjectCtrl'
            });
            modalInstance.result.then(function () {
                $log.info('Modal Ok at: ' + new Date());
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        };


    }).controller('CreateProjectCtrl', function ($state, $scope, $location, $uibModalInstance, ProjectService, AppMessager) {
        $scope.project = {};
        $scope.createProject = function () {
            ProjectService.createProject($scope.project)
                .then(function (response) {
                    console.log(response);
                    $uibModalInstance.close();
                    $location.path('/project/' + response.data.id);
                })
                .catch(function (response) {
                    AppMessager.errorResponse(response);
                });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    })
    .controller('ProjectDetailCtrl', function ($stateParams, $scope, ProjectService, AppMessager, TaskHelper) {
        $scope.daysOfWeek = TaskHelper.daysOfWeek;
        $scope.getRepertMode = TaskHelper.getRepertMode;
        $scope.ganttScrollTo = function (left) {
            var gl = angular.element(document.getElementById('task-gantt-list'));
            gl.scrollLeft(left);
        };
        $scope.ganttScrolled = function (startIndex, endIndex, width) {
            var startDay = moment($scope.project.startDate).startOf('day').add(startIndex, 'day');
            var endDay = moment($scope.project.startDate).startOf('day').add(endIndex, 'day');
            $scope.gantt.width = width;
            angular.forEach($scope.project.tasks, function (task) {
                var notfound = true;
                task.showTitle = false;
                task.showTitleDayIndex = 0;
                angular.forEach(task.dailyWorks, function (work) {
                    if (moment(work.date).isBetween(startDay, endDay) && notfound) {
                        task.showTitle = true;
                        task.showTitleDayIndex = moment(work.date).diff($scope.project.startDate, 'days') + 1;
                        notfound = false;
                    }
                });
            });
        };
        ProjectService.getProject($stateParams.projectId)
            .then(function (response) {
                $scope.project = response.data;
                $scope.gantt = TaskHelper.getGantt($scope.project);
            })
            .catch(function (response) {
                AppMessager.errorResponse(response);
            });


    })
    .factory('TaskHelper', ['AppMessager', function (AppMessager) {
        var helper = {
            repertTypeMap: {"ONCE": "仅一次", "EVERYDAY": "每天", "WORKINGDAY": "每工作日", "MONTHLY": "每月", "WEEKLY": "每周"},
            daysOfWeek: ["日", "一", "二", "三", "四", "五", "六"],
            getRepertMode: function (type, value) {
                if (angular.isString(value) && value) {
                    value = angular.fromJson(value);
                }
                var mode = helper.repertTypeMap[type];
                if (type == "WEEKLY") {
                    var days = [];
                    angular.forEach(value, function (day) {
                        days.push(helper.daysOfWeek[day]);
                    });
                    mode += days.join(",");
                } else if (type == "MONTHLY") {
                    var groupday = [];
                    var groups = [];

                    function addGroup() {
                        if (groupday.length == 1)
                            groups.push(groupday[0]);
                        else if (groupday.length == 2)
                            groups.push(groupday[0] + "," + groupday[1]);
                        else if (groupday.length > 2)
                            groups.push(groupday[0] + "-" + groupday[groupday.length - 1]);
                    }

                    angular.forEach(value, function (day) {
                        if (groupday.length && day - groupday[groupday.length - 1] > 1) {
                            addGroup();
                            groupday = [];
                            groupday.push(day);
                        } else {
                            groupday.push(day);
                        }
                    });
                    addGroup();
                    mode += groups.join(",") + "日";
                }
                return mode;
            },
            getGantt: function (project) {
                var gantt = {};
                gantt.days = [];
                gantt.weeks = [];

                var startDate = moment(project.startDate).startOf('day');
                var endDate = moment(project.endDate).startOf('day');

                /*//制造任务每日工作数据 - 开始
                 angular.forEach(project.tasks, function (task) {

                 if (angular.isString(task.repertValue) && task.repertValue) {
                 task.repertValue = angular.fromJson(task.repertValue);
                 }

                 var taskStartDate = moment(task.startDate).startOf('day');
                 var taskEndDate = moment(task.endDate).startOf('day');

                 task.dailyWorks = {};
                 var dayCount = taskEndDate.diff(taskStartDate, 'days');
                 for (var i = 0; i < dayCount + 1; i++) {
                 if (task.repertType == "ONCE" ||
                 task.repertType == "EVERYDAY" ||
                 (task.repertType == "WORKINGDAY" && [1, 2, 3, 4, 5].indexOf(taskStartDate.weekday()) >= 0) ||
                 (task.repertType == "MONTHLY" && task.repertValue.indexOf(taskStartDate.date()) >= 0) ||
                 (task.repertType == "WEEKLY" && task.repertValue.indexOf(taskStartDate.weekday()) >= 0)) {
                 task.dailyWorks[taskStartDate.valueOf()] = {
                 "id": task.id + taskStartDate.valueOf(),
                 "date": taskStartDate.clone().toDate(),
                 "isCompleted": taskStartDate.isBefore(moment()) && !taskStartDate.isSame(moment().add(-2, 'day'), 'day'),
                 "isTerminated": taskStartDate.isSame(moment().add(-2, 'day'), 'day') || taskStartDate.isSame(moment().add(-1, 'day'), 'day')};
                 }
                 taskStartDate.add(1, 'days');
                 }
                 });
                 //制造任务每日工作数据 - 结束*/

                angular.forEach(project.tasks, function (task) {
                    task.dailyWork = function (day) {
                        return task.dailyWorks[day.moment.valueOf()];
                    };
                    task.isStart = function (day) {
                        return !task.dailyWorks[day.moment.valueOf() - 24 * 3600 * 1000]
                    };
                    task.isEnd = function (day) {
                        return !task.dailyWorks[day.moment.valueOf() + 24 * 3600 * 1000]
                    };
                    var lastday, lasttodo;
                    angular.forEach(task.dailyWorks, function (work, key) {
                        key = parseInt(key);
                        lastday = lastday > key ? lastday : key;
                        if (!work.isTerminated && !work.isCompleted) {
                            lasttodo = lasttodo < key ? lasttodo : key;
                        }
                    });
                    lasttodo = lasttodo || lastday;
                    task.lastToDo = moment(lasttodo).diff(project.startDate, "days");
                });

                var dayCount = endDate.diff(startDate, 'days');
                for (var i = 0; i < dayCount + 1; i++) {
                    var date = startDate.clone().add(i, 'days');
                    gantt.days.push({
                        moment: date,
                        isWeekend: [6, 0].indexOf(date.weekday()) >= 0,
                        isBigline: [1, 5, 10, 15, 20, 25].indexOf(date.date()) >= 0,
                        isToday: date.isSame(moment(), 'day'),
                        weekday: "周" + helper.daysOfWeek[date.weekday()]
                    });
                }
                return gantt;
            }
        };
        return helper;
    }])
;