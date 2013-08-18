package org.apache.cordova.plugin.chord;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaInterface;
import org.json.JSONArray;
import org.json.JSONException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import android.widget.Toast;

import com.gabiq.trivia.Trivia;

import com.samsung.chord.samples.apidemo.service.ChordApiDemoService;
import com.samsung.chord.samples.apidemo.service.ChordApiDemoService.IChordServiceListener;
import com.samsung.chord.samples.apidemo.service.ChordApiDemoService.ChordServiceBinder;
import com.samsung.chord.ChordManager;

import com.immersion.uhl.Launcher;

public class ChordPlugin extends CordovaPlugin implements IChordServiceListener {

    protected static final String LOG_TAG = ChordPlugin.class.getSimpleName();
    private static final String TAGClass = "ChordApiTestActivity : ";

    private Thread mThread;

    /**
     * Executes the request and returns PluginResult.
     *
     * @param action        The action to execute.
     * @param args          JSONArry of arguments for the plugin.
     * @param callbackId    The callback id used when calling back into JavaScript.
     * @return              A PluginResult object with a status and message.
     */
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        boolean status = true;
        
        Log.d(LOG_TAG, "Received command:" + action);

        try {
            if (action.equals("start")) {
                try {
                    Trivia.mChordService.initialize(ChordPlugin.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                startChord();
            }
            else if (action.equals("stop")) {
                stopChord();
            }
            else if (action.equals("sendMessage")) {
                String msg = args.getString(0);
                String toNode = args.getString(1);
                Trivia.mChordService.sendData(Trivia.mChordService.CHORD_APITEST_CHANNEL, msg.getBytes(), toNode);

            }
            else if (action.equals("sendMessageToAll")) {
                String msg = args.getString(0);
                boolean res = Trivia.mChordService.sendDataToAll(Trivia.mChordService.CHORD_APITEST_CHANNEL, msg.getBytes());
                Log.d(LOG_TAG, "Sent message to all :" + msg + res);
            }
            else if (action.equals("join")) {
                String channel = args.getString(0);
                Trivia.mChordService.joinChannel(channel);
            }
            else if (action.equals("leave")) {
                // boolean isStop = args.getString(0).equals("true");
                Trivia.mChordService.leaveChannel();
            }
            else if (action.equals("haptic")) {
                try {
                  String effect = args.getString(0);
                  Trivia.mLauncher.play(Integer.parseInt(effect));
                }
                catch (RuntimeException e) {}
            }
            else {
                return false;
            }
            callbackContext.success();
            return true;
        } catch (Exception e) {
            callbackContext.error("Error");
            return true;
        }
    }

    // /**
    //  * Start Playing
    //  *
    //  * @param url to upload recording
    //  */
    // private void startPlaying(final String url) {
    //  // Log.d(LOG_TAG, "startPlaying calling native");
    //  // AligndChatterNativeLib.startPlay(url);
    //     if (mThread != null) {
    //      this.stopPlaying();
    //     }
    //     
    //     mThread = new Thread() {
    //         public void run() {
    //             // setPriority(Thread.MAX_PRIORITY);
    //             // AligndChatterNativeLib.startRecord(url);
    //             Log.d(LOG_TAG, "^^^^^^^^^^^^^^^^^^^^^ startPlaying calling native");
    //             AligndChatterNativeLib.startPlay(url);
    //         }
    //     };
    //     mThread.start();
    // }
    // 
    // /**
    //  * Stop Playing
    //  *
    //  */
    // private void stopPlaying() {
    //  
    //  Log.d(LOG_TAG, "^^^^^^^^^^^^^^^^^^^^ stopPlaying calling native");
    //  AligndChatterNativeLib.stopPlay();
    // 
    //     if (mThread == null) return;
    //     
    //     try {
    //         mThread.join();
    //     } catch (InterruptedException e) {
    //         e.printStackTrace();
    //     }
    //     
    //     mThread = null;
    // }
    
    /**
     * Define Javascript callback interface
     */
    public void notifyRecvMessage(final String msg, final String onChannel, final String fromNode) {
    	Trivia.sMe.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
            	Trivia.webView.sendJavascript(String.format("window.plugins.Chord.recvMessage( '%s', '%s', '%s' );", msg, onChannel, fromNode));
            }
        });
    }    
    
    public void notifyNodeEvent(final boolean joined, final String onChannel, final String fromNode) {
    	Trivia.sMe.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
              if (joined) {
              	Trivia.webView.sendJavascript(String.format("window.plugins.Chord.recvMessage( 'joinedChannel', '%s', '%s' );", onChannel, fromNode));                
              } else {
                Trivia.webView.sendJavascript(String.format("window.plugins.Chord.recvMessage( 'leftChannel', '%s', '%s' );", onChannel, fromNode));
              }
            }
        });
    }    
    
    // **********************************************************************
    // IChordServiceListener
    // **********************************************************************
    @Override
    public void onReceiveMessage(String node, String channel, String message) {
        Log.i(LOG_TAG, TAGClass + "onReceiveMessage()");
        notifyRecvMessage(message, channel, node);
        // mDataTestFragment.onMessageReceived(node, channel, message);
    }

    @Override
    public void onFileWillReceive(String node, String channel, String fileName, String exchangeId) {
        Log.i(LOG_TAG, TAGClass + "onFileWillReceive()");
        // mDataTestFragment.onFileNotify(node, channel, fileName, exchangeId);
    }

    @Override
    public void onFileProgress(boolean bSend, String node, String channel, int progress,
            String exchangeId) {
        Log.i(LOG_TAG, TAGClass + "onFileProgress()");
        // mDataTestFragment.onFileProgress(bSend, node, channel, progress, exchangeId);

    }

    @Override
    public void onFileCompleted(int reason, String node, String channel, String exchangeId,
            String fileName) {
        Log.i(LOG_TAG, TAGClass + "onFileCompleted()");
        // mDataTestFragment.onFileCompleted(reason, node, channel, fileName, exchangeId);
    }

    @Override
    public void onNodeEvent(String node, String channel, boolean bJoined) {
      Log.i(LOG_TAG, TAGClass + "onNodeEvent()");
      notifyNodeEvent(bJoined, channel, node);
        // if (bJoined) {
        //     if (channel.equals(mChordService.getPublicChannel())) {
        //         mChannelTestFragment.onPublicChannelNodeJoined(node);
        //         mDataTestFragment.onNodeJoined(node, channel);
        //     } else {
        //         mChannelTestFragment.onJoinedChannelNodeJoined(node);
        //         mDataTestFragment.onNodeJoined(node, channel);
        //     }
        //     return;
        // }
        // 
        // if (channel.equals(mChordService.getPublicChannel())) {
        //     mChannelTestFragment.onPublicChannelNodeLeaved(node);
        //     mDataTestFragment.onNodeLeaved(node, channel);
        // } else {
        //     mChannelTestFragment.onJoinedChannelNodeLeaved(node);
        //     mDataTestFragment.onNodeLeaved(node, channel);
        // }
    }

    @Override
    public void onNetworkDisconnected() {
      Log.i(LOG_TAG, TAGClass + "onNetworkDisconnected()");
        // mChannelTestFragment.onNetworkDisconnected();
        // mDataTestFragment.onNetworkDisconnected();
    }

    @Override
    public void onUpdateNodeInfo(String nodeName, String ipAddress) {
      Log.i(LOG_TAG, TAGClass + "onUpdateNodeInfo()");
        // mChannelTestFragment.setMyNodeInfo(nodeName, ipAddress);
        // mDataTestFragment.setMyNodeInfo(nodeName, ipAddress);
    }

    @Override
    public void onConnectivityChanged() {
      Log.i(LOG_TAG, TAGClass + "onConnectivityChanged()");
        // refreshInterfaceType();
    }

    // Chord Actions
    private boolean bStartedChord = false;
    
    public void startChord() {
        int nError = Trivia.mChordService.start(ChordManager.INTERFACE_TYPE_WIFI);
        if (ChordManager.ERROR_NONE == nError) {
            bStartedChord = true;
            Log.i(LOG_TAG, TAGClass + "====================> stated chord successfully");
        } else if (ChordManager.ERROR_INVALID_INTERFACE == nError) {
            Toast.makeText(Trivia.sMe.getActivity(), "Invalid connection", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Trivia.sMe.getActivity(), "Fail to start", Toast.LENGTH_SHORT).show();
        }
    }
    
    public void stopChord() {
        if (!bStartedChord)
            return;
        // leaveChannel(true);
        Trivia.mChordService.stop();
        bStartedChord = false;
    }
    

}
