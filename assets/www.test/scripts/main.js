var app = {
    initialize: function() {
        this.bind();
        // if (!window.plugins) {
        //     $(document).ready(function() {
        //         app.main();
        //     })
        // }
    },
    bind: function() {
        document.addEventListener('deviceready', this.deviceready, false);
    },
    deviceready: function() {
        console.log("====================================> Main Enter");
        app.urlPrefix = "http://voice.alignd.io:3000/api/assets/";
        app.main();
    },
    main: function() {
        console.log("Main Enter");
        $("#start-chord").click(function() {
            console.log("Chord start pressed");
            if (window.plugins) {
                var Chord = window.plugins.Chord;
                Chord.start();    
                console.log("***** Chord started");
            }
            
            // if (!$(this).hasClass('recording')) {
            //     // Start recording
            //     app.assetId = (new Date()).getTime();
            // 
            //     if (window.plugins) {
            //         var AudioRecorder = window.plugins.AudioRecorder;
            //         AudioRecorder.record(app.assetId);    
            //         console.log("***** Recording started");
            //     }
            // 
            //     $(this).addClass("recording").text("Stop Recording");
            //     $("#play").show().text("Start Playing "+app.assetId);
            //     $("#play-repeat").show().text("Play Repeat "+app.assetId);
            // } else {
            //     // Stop recording
            //     if (window.plugins) {
            //         var AudioRecorder = window.plugins.AudioRecorder;
            //         AudioRecorder.stop();
            //         console.log("***** Recording stopped");
            //     }                                
            // 
            //     $(this).removeClass("recording").text("Start Recording");
            // }
        });

        $("#stop-chord").click(function() {
            console.log("Chord stop pressed");
            if (window.plugins) {
                var Chord = window.plugins.Chord;
                Chord.stop();    
                console.log("***** Chord stoped");
            }            
        });
          
        $("#send-message").click(function() {
            console.log("Send message pressed");
            if (window.plugins) {
                var Chord = window.plugins.Chord;
                Chord.sendMessageToAll("Success!!!!!!!!");    
            }            
        });

        $("#join-channel").click(function() {
            console.log("Join channel pressed");
            if (window.plugins) {
                var Chord = window.plugins.Chord;
                Chord.joinChannel("com.gabiq.Channel1");    
            }            
        });
          
        $("#leave-channel").click(function() {
            console.log("Leave channel pressed");
            if (window.plugins) {
                var Chord = window.plugins.Chord;
                Chord.leaveChannel();    
            }            
        });
          
        $("#play-haptic").click(function() {
          console.log("Play Haptic pressed");
            if (window.plugins) {
                var Chord = window.plugins.Chord;
                Chord.playHaptic(82);    
            }            
        });
          

        $("#play").click(function() {
            console.log("Play button pressed");
            if (!$(this).hasClass('playing')) {
                // Start playing
                if (window.plugins) {
                    var AudioPlayer = window.plugins.AudioPlayer;
                    AudioPlayer.play(app.urlPrefix+app.assetId);    
                    console.log("***** Playing started");
                }

                $(this).addClass("playing").text("Stop Playing");
            } else {
                // Stop playing
                if (window.plugins) {
                    var AudioPlayer = window.plugins.AudioPlayer;
                    AudioPlayer.stop();
                    console.log("***** Playing stopped");
                }                                

                $(this).removeClass("playing").text("Start Playing "+app.assetId);
                $("#pause").hide();
            }
        });
        $("#play-repeat").click(function() {
            console.log("Play Repeat button pressed");
            if (!$(this).hasClass('playing')) {
                // Start playing
                if (window.plugins) {
                    var AudioPlayer = window.plugins.AudioPlayer;
                    AudioPlayer.play(app.urlPrefix+app.assetId);    
                    console.log("***** Playing started");
                }

                $(this).addClass("playing").text("Stop Playing");
            } else {
                // Stop playing
                if (window.plugins) {
                    var AudioPlayer = window.plugins.AudioPlayer;
                    AudioPlayer.stop();
                    console.log("***** Playing stopped");
                }                                

                $(this).removeClass("playing").text("Start Playing "+app.assetId);
                $("#pause").hide();
            }
        });
        $("#pause").click(function() {
            console.log("Pause button pressed");
            if (!$(this).hasClass('paused')) {
                // Pause
                if (window.plugins) {
                    var AudioPlayer = window.plugins.AudioPlayer;
                    AudioPlayer.pause();    
                }

                $(this).addClass("paused").text("Resume");
            } else {
                // Resume
                if (window.plugins) {
                    var AudioPlayer = window.plugins.AudioPlayer;
                    AudioPlayer.resume();
                }                                

                $(this).removeClass("paused").text("Pause");
            }
        });

        app.callback = function(status) {
            console.log('@@@@ ' + status);
            if (status === 'loading') {
            } else if (status === 'started') {
                $("#pause").show().text("Pause");
            } else if (status === 'paused') {
                    console.log("***** Paused");
            } else if (status === 'resumed') {
                    console.log("***** Resumed");
            } else if (status === 'stopped') {
                if ($("#play").hasClass("playing")) {
                    $("#play").removeClass("playing").text("Start Playing "+app.assetId);
                    console.log("***** Playing stopped");
                    $("#pause").hide();
                } else if ($("#play-repeat").hasClass("playing")) {
                    $("#play-repeat").removeClass("playing").text("Play Repeat "+app.assetId);
                    console.log("***** Playing stopped");

                    var AudioPlayer = window.plugins.AudioPlayer;
                    AudioPlayer.play(app.urlPrefix+app.assetId);    
                    console.log("***** Playing started");

                    $("#play-repeat").addClass("playing").text("Stop Playing");
                }
            }
        }
        var AudioPlayer = window.plugins.AudioPlayer;
        AudioPlayer.onStatusChange(app.callback);
    }
};
