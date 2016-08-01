angular.module('MyApp')
    .service('AccountService', ['$q', '$http','$httpParamSerializerJQLike', 'AppConfig', 'AccountConfig', 'AccountUtils', 'AccountShared', 'AccountOauth1', 'AccountOauth2',
        function ($q, $http,$httpParamSerializerJQLike, appConfig, config, utils, shared, Oauth1, Oauth2) {
            return  {
                currentUser: function () {
                    return shared.getCurrentUser();
                },
                isLogin: function () {
                    return !!shared.getCurrentUser();
                },
                isAuthenticated: function () {
                    return shared.isAuthenticated();
                },
                logout: function () {
                    return shared.logout();
                },
                login: function (user) {
                    var thisService = this;
                    var deferred = $q.defer();
                    var opts = {headers: { 'Content-Type': 'application/x-www-form-urlencoded'}};
                    opts.headers[config.authHeader] = config.authClientToken;
                    user.grant_type = 'password';
                    $http.post(appConfig.apiBaseUrl + '/oauth/token', $httpParamSerializerJQLike(user), opts)
                        .then(function (response) {
                            shared.setToken(response.data.access_token, false);
                            thisService.getMe()
                                .then(function (response) {
                                    shared.setCurrentUser(response.data);
                                    deferred.resolve(response);
                                })
                                .catch(function (error) {
                                    deferred.reject(error);
                                });
                            return response;
                        })
                        .catch(function (error) {
                            deferred.reject(error);
                        });

                    return deferred.promise;
                },
                authenticate: function (name) {

                    var thisService = this;
                    var provider = config.providers[name].type === '1.0' ? new Oauth1() : new Oauth2();
                    var deferred = $q.defer();
                    var opts = {headers: {}};
                    opts.headers[config.authHeader] = config.authClientToken;

                    provider.open(appConfig.apiBaseUrl + '/user/auth/' + name, config.providers[name], {}, opts)
                        .then(function (response) {
                            shared.setToken(response.data.access_token, false);
                            thisService.getMe()
                                .then(function (response) {
                                    shared.setCurrentUser(response.data);
                                    deferred.resolve(response);
                                })
                                .catch(function (error) {
                                    deferred.reject(error);
                                });
                            return response;
                        })
                        .catch(function (error) {
                            deferred.reject(error);
                        });
                    return deferred.promise;
                },
                link: function (name) {
                    var thisService = this;
                    var provider = config.providers[name].type === '1.0' ? new Oauth1() : new Oauth2();
                    var deferred = $q.defer();
                    provider.open(appConfig.apiBaseUrl + '/user/link/' + name, config.providers[name])
                        .then(function (response) {
                            thisService.getMe()
                                .then(function (response) {
                                    shared.setCurrentUser(response.data);
                                    deferred.resolve(response);
                                })
                                .catch(function (error) {
                                    deferred.reject(error);
                                });
                            return response;
                        })
                        .catch(function (error) {
                            deferred.reject(error);
                        });
                    return deferred.promise;
                },
                unlink: function (name) {
                    var thisService = this;
                    var deferred = $q.defer();
                    $http.post(appConfig.apiBaseUrl + '/user/unlink/' + name)
                        .then(function (response) {
                            thisService.getMe()
                                .then(function (response) {
                                    shared.setCurrentUser(response.data);
                                    deferred.resolve(response);
                                })
                                .catch(function (error) {
                                    deferred.reject(error);
                                });
                            return response;
                        })
                        .catch(function (error) {
                            deferred.reject(error);
                        });
                    return deferred.promise;
                },
                signup: function (user) {
                    var thisService = this;
                    var requestData = {user: {emailAddress: user.email}, password: user.password};
                    var deferred = $q.defer();
                    var opts = {headers: {}};
                    opts.headers[config.authHeader] = config.authClientToken;

                    $http.post(appConfig.apiBaseUrl + '/user/signup', requestData, opts)
                        .then(function (response) {
                            shared.setToken(response.data.oauth2AccessToken.access_token);
                            thisService.getMe()
                                .then(function (response) {
                                    shared.setCurrentUser(response.data);
                                    deferred.resolve(response);
                                })
                                .catch(function (error) {
                                    deferred.reject(error);
                                });
                            return response;
                        })
                        .catch(function (error) {
                            deferred.reject(error);
                        });

                    return deferred.promise;
                },
                getMe: function () {
                    return $http.get(appConfig.apiBaseUrl + '/user/me');
                },
                updateMe: function (profileData) {
                    var thisService = this;
                    var deferred = $q.defer();
                    $http.put(appConfig.apiBaseUrl + '/user/me', profileData)
                        .then(function (response) {
                            thisService.getMe()
                                .then(function (response) {
                                    shared.setCurrentUser(response.data);
                                    deferred.resolve(response);
                                })
                                .catch(function (error) {
                                    deferred.reject(error);
                                });
                            return response;
                        })
                        .catch(function (error) {
                            deferred.reject(error);
                        });
                    return deferred.promise;
                }
            };
        }])
    .constant('AccountConfig', {
        httpInterceptor: true,
        withCredentials: true,
        tokenRoot: null,
        cordova: false,
        baseUrl: '/',
        loginUrl: '/auth/login',
        signupUrl: '/auth/signup',
        unlinkUrl: '/auth/unlink/',
        tokenName: 'access_token',
        tokenPrefix: 'satellizer',
        authHeader: 'Authorization',
        authToken: 'Bearer',
        authClientToken: 'Basic MzUzYjMwMmM0NDU3NGY1NjUwNDU2ODdlNTM0ZTdkNmE6Mjg2OTI0Njk3ZTYxNWE2NzJhNjQ2YTQ5MzU0NTY0NmM=',
        storageType: 'localStorage',
        providers: {
            facebook: {
                name: 'facebook',
                //url: '/auth/facebook',
                authorizationEndpoint: 'https://www.facebook.com/v2.3/dialog/oauth',
                redirectUri: (window.location.origin || window.location.protocol + '//' + window.location.host) + '/',
                requiredUrlParams: ['display', 'scope'],
                scope: ['email'],
                scopeDelimiter: ',',
                display: 'popup',
                type: '2.0',
                popupOptions: { width: 580, height: 400 }
            },
            google: {
                name: 'google',
                //url: '/auth/google',
                clientId: '764000492832-d0i8jecn22i8mfrn7gfsf76b1erbfo9b.apps.googleusercontent.com',
                authorizationEndpoint: 'https://accounts.google.com/o/oauth2/auth',
                redirectUri: window.location.origin || window.location.protocol + '//' + window.location.host,
                requiredUrlParams: ['scope'],
                optionalUrlParams: ['display'],
                scope: ['profile', 'email'],
                scopePrefix: 'openid',
                scopeDelimiter: ' ',
                display: 'popup',
                type: '2.0',
                popupOptions: { width: 452, height: 633 }
            },
            github: {
                name: 'github',
                //url: '/auth/github',
                authorizationEndpoint: 'https://github.com/login/oauth/authorize',
                redirectUri: window.location.origin || window.location.protocol + '//' + window.location.host,
                optionalUrlParams: ['scope'],
                scope: ['user:email'],
                scopeDelimiter: ' ',
                type: '2.0',
                popupOptions: { width: 1020, height: 618 }
            },
            instagram: {
                name: 'instagram',
                //url: '/auth/instagram',
                redirectUri: window.location.origin || window.location.protocol + '//' + window.location.host,
                requiredUrlParams: ['scope'],
                scope: ['basic'],
                scopeDelimiter: '+',
                authorizationEndpoint: 'https://api.instagram.com/oauth/authorize'
            },
            linkedin: {
                name: 'linkedin',
                //url: '/auth/linkedin',
                authorizationEndpoint: 'https://www.linkedin.com/uas/oauth2/authorization',
                redirectUri: window.location.origin || window.location.protocol + '//' + window.location.host,
                requiredUrlParams: ['state'],
                scope: ['r_emailaddress'],
                scopeDelimiter: ' ',
                state: 'STATE',
                type: '2.0',
                popupOptions: { width: 527, height: 582 }
            },
            twitter: {
                name: 'twitter',
                //url: '/auth/twitter',
                authorizationEndpoint: 'https://api.twitter.com/oauth/authenticate',
                redirectUri: window.location.origin || window.location.protocol + '//' + window.location.host,
                type: '1.0',
                popupOptions: { width: 495, height: 645 }
            },
            twitch: {
                name: 'twitch',
                //url: '/auth/twitch',
                authorizationEndpoint: 'https://api.twitch.tv/kraken/oauth2/authorize',
                redirectUri: window.location.origin || window.location.protocol + '//' + window.location.host,
                requiredUrlParams: ['scope'],
                scope: ['user_read'],
                scopeDelimiter: ' ',
                display: 'popup',
                type: '2.0',
                popupOptions: { width: 500, height: 560 }
            },
            live: {
                name: 'live',
                //url: '/auth/live',
                authorizationEndpoint: 'https://login.live.com/oauth20_authorize.srf',
                redirectUri: window.location.origin || window.location.protocol + '//' + window.location.host,
                requiredUrlParams: ['display', 'scope'],
                scope: ['wl.emails'],
                scopeDelimiter: ' ',
                display: 'popup',
                type: '2.0',
                popupOptions: { width: 500, height: 560 }
            },
            yahoo: {
                name: 'yahoo',
                //url: '/auth/yahoo',
                authorizationEndpoint: 'https://api.login.yahoo.com/oauth2/request_auth',
                redirectUri: window.location.origin || window.location.protocol + '//' + window.location.host,
                scope: [],
                scopeDelimiter: ',',
                type: '2.0',
                popupOptions: { width: 559, height: 519 }
            }
        }
    })
    .factory('AccountShared', [
        '$q',
        '$window',
        'AccountConfig',
        'AccountStorage',
        function ($q, $window, config, storage) {
            var Shared = {};

            var tokenName = config.tokenPrefix ? [config.tokenPrefix, config.tokenName].join('_') : config.tokenName;

            Shared.getToken = function () {
                var data = storage.get(tokenName);
                if (data) {
                    return data.token;
                }
                return null;
            };

            Shared.getCurrentUser = function () {
                if (!Shared.isAuthenticated) {
                    return null;
                }
                var data = storage.get(tokenName);
                if (data) {
                    return data.user;
                }
                return null;
            };
            Shared.getPayload = function () {
                var data = storage.get(tokenName);
                if (data) {
                    var token = data.token;
                    if (token && token.split('.').length === 3) {
                        var base64Url = token.split('.')[1];
                        var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
                        return JSON.parse(decodeURIComponent(escape(window.atob(base64))));
                    }
                }
            };
            Shared.setCurrentUser = function (user) {
                var data = storage.get(tokenName);
                if (data) {
                    data.user = user;
                    storage.set(tokenName, data);
                }
            };

            Shared.setToken = function (response) {

                var data = {token: response};
                storage.set(tokenName, data);
            };

            Shared.removeToken = function () {
                storage.remove(tokenName);
            };

            /**
             * @returns {boolean}
             */
            Shared.isAuthenticated = function () {
                var data = storage.get(tokenName);

                if (data) {
                    try {
                        var token = data.token;
                        if (token.split('.').length === 3) {
                            var base64Url = token.split('.')[1];
                            var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
                            var exp = JSON.parse($window.atob(base64)).exp;

                            if (exp) {
                                var isExpired = Math.round(new Date().getTime() / 1000) >= exp;

                                if (isExpired) {
                                    storage.remove(tokenName);
                                    return false;
                                } else {
                                    return true;
                                }
                            }
                            return true;
                        }
                        return true;
                    } catch (e) {
                        storage.remove(tokenName);
                        return false;
                    }
                }
                return false;
            };

            Shared.logout = function () {
                storage.remove(tokenName);
                return $q.when();
            };

            Shared.setStorageType = function (type) {
                config.storageType = type;
            };

            return Shared;
        }])
    .factory('AccountOauth2', [
        '$q',
        '$http',
        '$window',
        'AccountPopup',
        'AccountUtils',
        'AccountConfig',
        'AccountStorage',
        function ($q, $http, $window, popup, utils, config, storage) {
            return function () {
                var Oauth2 = {};

                var defaults = {
                    defaultUrlParams: ['response_type', 'client_id', 'redirect_uri'],
                    responseType: 'code',
                    responseParams: {
                        code: 'code',
                        clientId: 'clientId',
                        redirectUri: 'redirectUri'
                    }
                };

                Oauth2.open = function (exchangeForTokenUrl, options, userData, exchangeForTokenOptions) {
                    defaults = utils.merge(options, defaults);

                    var url;
                    var openPopup;
                    var stateName = defaults.name + '_state';

                    if (angular.isFunction(defaults.state)) {
                        storage.set(stateName, defaults.state());
                    } else if (angular.isString(defaults.state)) {
                        storage.set(stateName, defaults.state);
                    }

                    url = [defaults.authorizationEndpoint, Oauth2.buildQueryString()].join('?');

                    if (config.cordova) {
                        openPopup = popup.open(url, defaults.name, defaults.popupOptions, defaults.redirectUri).eventListener(defaults.redirectUri);
                    } else {
                        openPopup = popup.open(url, defaults.name, defaults.popupOptions, defaults.redirectUri).pollPopup();
                    }

                    return openPopup
                        .then(function (oauthData) {
                            if (defaults.responseType === 'token') {
                                return oauthData;
                            }

                            if (oauthData.state && oauthData.state !== storage.get(stateName)) {
                                return $q.reject('OAuth "state" mismatch');
                            }

                            return Oauth2.exchangeForToken(exchangeForTokenUrl, oauthData, userData, exchangeForTokenOptions);
                        });
                };

                Oauth2.exchangeForToken = function (exchangeForTokenUrl, oauthData, userData, exchangeForTokenOptions) {
                    var data = angular.extend({}, userData);

                    angular.forEach(defaults.responseParams, function (value, key) {
                        switch (key) {
                            case 'code':
                                data[value] = oauthData.code;
                                break;
                            case 'clientId':
                                data[value] = defaults.clientId;
                                break;
                            case 'redirectUri':
                                data[value] = defaults.redirectUri;
                                break;
                            default:
                                data[value] = oauthData[key]
                        }
                    });

                    if (oauthData.state) {
                        data.state = oauthData.state;
                    }

                    return $http.post(exchangeForTokenUrl, data, exchangeForTokenOptions);
                };

                Oauth2.buildQueryString = function () {
                    var keyValuePairs = [];
                    var urlParams = ['defaultUrlParams', 'requiredUrlParams', 'optionalUrlParams'];

                    angular.forEach(urlParams, function (params) {

                        angular.forEach(defaults[params], function (paramName) {
                            var camelizedName = utils.camelCase(paramName);
                            var paramValue = angular.isFunction(defaults[paramName]) ? defaults[paramName]() : defaults[camelizedName];

                            if (paramName === 'state') {
                                var stateName = defaults.name + '_state';
                                paramValue = encodeURIComponent(storage.get(stateName));
                            }

                            if (paramName === 'scope' && Array.isArray(paramValue)) {
                                paramValue = paramValue.join(defaults.scopeDelimiter);

                                if (defaults.scopePrefix) {
                                    paramValue = [defaults.scopePrefix, paramValue].join(defaults.scopeDelimiter);
                                }
                            }

                            keyValuePairs.push([paramName, paramValue]);
                        });
                    });

                    return keyValuePairs.map(function (pair) {
                        return pair.join('=');
                    }).join('&');
                };

                return Oauth2;
            };
        }])
    .factory('AccountOauth1', [
        '$q',
        '$http',
        'AccountPopup',
        'AccountConfig',
        'AccountUtils',
        function ($q, $http, popup, config, utils) {
            return function () {
                var Oauth1 = {};

                var defaults = {
                    url: null,
                    name: null,
                    popupOptions: null,
                    redirectUri: null,
                    authorizationEndpoint: null
                };

                Oauth1.open = function (exchangeForTokenUrl, options, userData, exchangeForTokenOptions) {
                    angular.extend(defaults, options);
                    var popupWindow;
                    var serverUrl = config.baseUrl ? utils.joinUrl(config.baseUrl, defaults.url) : defaults.url;

                    if (!config.cordova) {
                        popupWindow = popup.open('', defaults.name, defaults.popupOptions, defaults.redirectUri);
                    }

                    return $http.post(serverUrl, defaults)
                        .then(function (response) {
                            if (config.cordova) {
                                popupWindow = popup.open([defaults.authorizationEndpoint, Oauth1.buildQueryString(response.data)].join('?'), defaults.name, defaults.popupOptions, defaults.redirectUri);
                            } else {
                                popupWindow.popupWindow.location = [defaults.authorizationEndpoint, Oauth1.buildQueryString(response.data)].join('?');
                            }

                            var popupListener = config.cordova ? popupWindow.eventListener(defaults.redirectUri) : popupWindow.pollPopup();

                            return popupListener
                                .then(function (response) {
                                    return Oauth1.exchangeForToken(exchangeForTokenUrl, response, userData, exchangeForTokenOptions);
                                });
                        });

                };

                Oauth1.exchangeForToken = function (exchangeForTokenUrl, oauthData, userData, exchangeForTokenOptions) {
                    var data = angular.extend({}, userData, oauthData);
                    return $http.post(exchangeForTokenUrl, data, exchangeForTokenOptions);
                };

                Oauth1.buildQueryString = function (obj) {
                    var str = [];

                    angular.forEach(obj, function (value, key) {
                        str.push(encodeURIComponent(key) + '=' + encodeURIComponent(value));
                    });

                    return str.join('&');
                };

                return Oauth1;
            };
        }])
    .factory('AccountPopup', [
        '$q',
        '$interval',
        '$window',
        'AccountConfig',
        'AccountUtils',
        function ($q, $interval, $window, config, utils) {
            var Popup = {};

            Popup.url = '';
            Popup.popupWindow = null;

            Popup.open = function (url, name, options) {
                Popup.url = url;

                var stringifiedOptions = Popup.stringifyOptions(Popup.prepareOptions(options));
                var windowName = config.cordova ? '_blank' : name;

                Popup.popupWindow = window.open(url, windowName, stringifiedOptions);

                window.popup = Popup.popupWindow;

                if (Popup.popupWindow && Popup.popupWindow.focus) {
                    Popup.popupWindow.focus();
                }

                return Popup;
            };

            Popup.eventListener = function (redirectUri) {
                var deferred = $q.defer();

                Popup.popupWindow.addEventListener('loadstart', function (event) {
                    if (event.url.indexOf(redirectUri) !== 0) {
                        return;
                    }

                    var parser = document.createElement('a');
                    parser.href = event.url;

                    if (parser.search || parser.hash) {
                        var queryParams = parser.search.substring(1).replace(/\/$/, '');
                        var hashParams = parser.hash.substring(1).replace(/\/$/, '');
                        var hash = utils.parseQueryString(hashParams);
                        var qs = utils.parseQueryString(queryParams);

                        angular.extend(qs, hash);

                        if (!qs.error) {
                            deferred.resolve(qs);
                        }

                        Popup.popupWindow.close();
                    }
                });

                Popup.popupWindow.addEventListener('loaderror', function () {
                    deferred.reject('Authorization Failed');
                });

                return deferred.promise;
            };

            Popup.pollPopup = function () {
                var deferred = $q.defer();

                var polling = $interval(function () {
                    try {
                        var documentOrigin = document.location.host;
                        var popupWindowOrigin = Popup.popupWindow.location.host;

                        if (popupWindowOrigin === documentOrigin && (Popup.popupWindow.location.search || Popup.popupWindow.location.hash)) {
                            var queryParams = Popup.popupWindow.location.search.substring(1).replace(/\/$/, '');
                            var hashParams = Popup.popupWindow.location.hash.substring(1).replace(/[\/$]/, '');
                            var hash = utils.parseQueryString(hashParams);
                            var qs = utils.parseQueryString(queryParams);

                            angular.extend(qs, hash);

                            if (!qs.error) {
                                deferred.resolve(qs);
                            }

                            Popup.popupWindow.close();

                            $interval.cancel(polling);
                        }
                    } catch (error) {
                        // Ignore DOMException: Blocked a frame with origin from accessing a cross-origin frame.
                    }

                    if (!Popup.popupWindow || Popup.popupWindow.closed || Popup.popupWindow.closed === undefined) {
                        $interval.cancel(polling);
                    }
                }, 50);

                return deferred.promise;
            };

            Popup.prepareOptions = function (options) {
                options = options || {};
                var width = options.width || 500;
                var height = options.height || 500;

                return angular.extend({
                    width: width,
                    height: height,
                    left: $window.screenX + (($window.outerWidth - width) / 2),
                    top: $window.screenY + (($window.outerHeight - height) / 2.5)
                }, options);
            };

            Popup.stringifyOptions = function (options) {
                var parts = [];
                angular.forEach(options, function (value, key) {
                    parts.push(key + '=' + value);
                });
                return parts.join(',');
            };

            return Popup;
        }])
    .service('AccountUtils', function () {
        this.camelCase = function (name) {
            return name.replace(/([\:\-\_]+(.))/g, function (_, separator, letter, offset) {
                return offset ? letter.toUpperCase() : letter;
            });
        };

        this.parseQueryString = function (keyValue) {
            var obj = {}, key, value;
            angular.forEach((keyValue || '').split('&'), function (keyValue) {
                if (keyValue) {
                    value = keyValue.split('=');
                    key = decodeURIComponent(value[0]);
                    obj[key] = angular.isDefined(value[1]) ? decodeURIComponent(value[1]) : true;
                }
            });
            return obj;
        };

        this.joinUrl = function (baseUrl, url) {
            if (/^(?:[a-z]+:)?\/\//i.test(url)) {
                return url;
            }

            var joined = [baseUrl, url].join('/');

            var normalize = function (str) {
                return str
                    .replace(/[\/]+/g, '/')
                    .replace(/\/\?/g, '?')
                    .replace(/\/\#/g, '#')
                    .replace(/\:\//g, '://');
            };

            return normalize(joined);
        };

        this.merge = function (obj1, obj2) {
            var result = {};
            for (var i in obj1) {
                if (obj1.hasOwnProperty(i)) {
                    if ((i in obj2) && (typeof obj1[i] === 'object') && (i !== null)) {
                        result[i] = this.merge(obj1[i], obj2[i]);
                    } else {
                        result[i] = obj1[i];
                    }
                }
            }
            for (i in obj2) {
                if (obj2.hasOwnProperty(i)) {
                    if (i in result) {
                        continue;
                    }
                    result[i] = obj2[i];
                }

            }
            return result;
        }
    })
    .factory('AccountStorage', ['$window', 'AccountConfig', function ($window, config) {
        var isStorageAvailable = (function () {
            try {
                var supported = config.storageType in $window && $window[config.storageType] !== null;

                if (supported) {
                    var key = Math.random().toString(36).substring(7);
                    $window[config.storageType].setItem(key, '');
                    $window[config.storageType].removeItem(key);
                }

                return supported;
            } catch (e) {
                return false;
            }
        })();

        if (!isStorageAvailable) {
            console.warn('Account Warning: ' + config.storageType + ' is not available.');
        }

        return {
            get: function (key) {
                return isStorageAvailable ? angular.fromJson($window[config.storageType].getItem(key)) : undefined;
            },
            set: function (key, value) {
                var value = angular.toJson(value);
                return isStorageAvailable ? $window[config.storageType].setItem(key, value) : undefined;
            },
            remove: function (key) {
                return isStorageAvailable ? $window[config.storageType].removeItem(key) : undefined;
            }
        };
    }])
    .factory('AccountInterceptor', [
        '$q',
        'AccountConfig',
        'AccountStorage',
        'AccountShared',
        function ($q, config, storage, shared) {
            return {
                request: function (request) {
                    if (request.skipAuthorization) {
                        return request;
                    }

                    if (shared.isAuthenticated() && config.httpInterceptor) {
                        var token = shared.getToken();

                        if (config.authHeader && config.authToken) {
                            token = config.authToken + ' ' + token;
                        }

                        request.headers[config.authHeader] = token;
                    }

                    return request;
                },
                responseError: function (response) {
                    return $q.reject(response);
                }
            };
        }])
    .config(['$httpProvider', function ($httpProvider) {
        $httpProvider.interceptors.push('AccountInterceptor');
    }]);