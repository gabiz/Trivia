
function AudioRecorder() {
    this.status = "stopped";
    this.callbacks = {
        onStatusChanged: [],
    };
}

AudioRecorder.prototype.record = function(url) {
    if (this.status != "stopped") {
        this.setStatus("stopped");
    }
    cordova.exec(function(winParam) {}, function(error) {}, "AudioRecorder", "record", [ url ]);
    this.setStatus("loading");
};

AudioRecorder.prototype.stop = function() {
    cordova.exec(function(winParam) {}, function(error) {}, "AudioRecorder", "stop", []);
};

AudioRecorder.prototype.getStatus = function() {
    return this.status;
}


AudioRecorder.prototype.onStatusChange = function(successCallback, errorCallback, options) {
    this.callbacks.onStatusChanged.push(successCallback);
};

AudioRecorder.prototype.setStatus = function(status) {
    this.status = status;
    for (var i = 0; i < this.callbacks.onStatusChanged.length; i++) {
        var f = this.callbacks.onStatusChanged[i];
        f(status);
    }
};

cordova.addConstructor(function() {
    if (!window.plugins) {
        window.plugins = {};
    }
    if (AudioRecorder) {
        window.plugins.AudioRecorder = new AudioRecorder();
    }
}

);
