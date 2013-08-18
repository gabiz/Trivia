
function AudioPlayer() {
    this.status = "stopped";
    this.callbacks = {
        onStatusChanged: [],
    };
}

AudioPlayer.prototype.play = function(url) {
    if (this.status != "stopped") {
        this.setStatus("stopped");
    }
    cordova.exec(function(winParam) {}, function(error) {}, "AudioPlayer", "play", [ url ]);
    this.setStatus("loading");
};
AudioPlayer.prototype.stop = function() {
    cordova.exec(function(winParam) {}, function(error) {}, "AudioPlayer", "stop", []);
};


AudioPlayer.prototype.pause = function() {
    cordova.exec(function(winParam) {}, function(error) {}, "AudioPlayer", "pause", []);
};

AudioPlayer.prototype.resume = function() {
    cordova.exec(function(winParam) {}, function(error) {}, "AudioPlayer", "resume", []);
};


AudioPlayer.prototype.getStatus = function() {
    return this.status;
}


AudioPlayer.prototype.onStatusChange = function(callback) {
    this.callbacks.onStatusChanged.push(callback);
};

AudioPlayer.prototype.offStatusChange = function(callback) {
    var index = this.callbacks.onStatusChanged.indexOf(callback);
    if (callback != -1) {
        this.callbacks.onStatusChanged.splice(index, 1);
    }
}

AudioPlayer.prototype.setStatus = function(status) {
    this.status = status;
    for (var i = 0; i < this.callbacks.onStatusChanged.length; i++) {
        var f = this.callbacks.onStatusChanged[i];
        f(status);
    }
};

cordova.addConstructor(function() {
    if(!window.plugins) {
        window.plugins = {};
    }
    if (AudioPlayer) {
        window.plugins.AudioPlayer = new AudioPlayer();
    }
}

);
