
function Chord() {
    this.status = "stopped";
    this.callbacks = {
        msgRecivedCallbacks: [],
    };
}

Chord.prototype.start = function() {
    cordova.exec(function(winParam) {}, function(error) {}, "ChordPlugin", "start", [ "foo" ]);
};

Chord.prototype.stop = function() {
    cordova.exec(function(winParam) {}, function(error) {}, "ChordPlugin", "stop", []);
};

Chord.prototype.sendMessage = function(msg, toNode) {
    cordova.exec(function(winParam) {}, function(error) {}, "ChordPlugin", "sendMessage", [ msg, toNode ]);
};

Chord.prototype.sendMessageToAll = function(msg) {
    cordova.exec(function(winParam) {}, function(error) {}, "ChordPlugin", "sendMessageToAll", [ msg ]);
};

Chord.prototype.joinChannel = function(channel) {
    cordova.exec(function(winParam) {}, function(error) {}, "ChordPlugin", "join", [ channel ]);
};

Chord.prototype.leaveChannel = function() {
    cordova.exec(function(winParam) {}, function(error) {}, "ChordPlugin", "leave", [ ]);
};

Chord.prototype.playHaptic = function(effect) {
    cordova.exec(function(winParam) {}, function(error) {}, "ChordPlugin", "haptic", [ effect ]);
};

Chord.prototype.onMsgReceived = function(callback) {
    this.callbacks.msgRecivedCallbacks.push(callback);
};

Chord.prototype.offMessageReceived = function(callback) {
    var index = this.callbacks.msgRecivedCallbacks.indexOf(callback);
    if (callback != -1) {
        this.callbacks.msgRecivedCallbacks.splice(index, 1);
    }
}

Chord.prototype.recvMessage = function(message, onChannel, fromNode) {
    console.log("=============================>"+message + " onChannel:"+onChannel+ " fromNode"+fromNode);
    for (var i = 0; i < this.callbacks.msgRecivedCallbacks.length; i++) {
        var f = this.callbacks.msgRecivedCallbacks[i];
        f(message, onChannel, fromNode);
    }
};


cordova.addConstructor(function() {
    if(!window.plugins) {
        window.plugins = {};
    }
    if (!window.plugins.Chord) {
        window.plugins.Chord = new Chord();
    }
}

);
