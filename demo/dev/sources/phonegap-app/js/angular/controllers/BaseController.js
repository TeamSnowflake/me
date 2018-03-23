var BaseController = function ($rootScope, $scope, $q, $attrs, $state, $timeout) {
    $rootScope.$state = $scope.$state = $state;
    $rootScope.$thisState = $scope.$thisState = $state.current;

    window.handleOpenURL = function(url_string) {
        setTimeout(function() {
            var url = new URL(url_string);
            var request = url.searchParams.get("request");

            if (request == 'StemApp') {
                $state.go('share-data-stempas', {
                    data: {
                        requested: {
                            items: [
                                'Stempas'
                            ]
                        }
                    }
                }, {
                    title: 'StemAPP',
                    subtitle: 'Share your voting pass',
                });
            }
        }, 0);
    };

    var checkConnection = function () {
        if (navigator.connection.type == window.Connection.NONE) {
            alert('No internet connection!');
            $timeout(checkConnection, 1000);
        }
    };

    $timeout(function () {
        checkConnection();

        document.addEventListener("offline", function () {
            $timeout(checkConnection, 0);
        }, false);

        document.addEventListener("online", function () {
            $state.reload();
        }, false);
    }, 1000);
};

module.exports = ['$rootScope', '$scope', '$q', '$attrs', '$state', '$timeout', BaseController];