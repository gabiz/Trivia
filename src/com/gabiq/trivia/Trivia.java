/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package com.gabiq.trivia;

import android.os.Bundle;
import org.apache.cordova.*;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.samsung.chord.samples.apidemo.service.ChordApiDemoService;
import com.samsung.chord.samples.apidemo.service.ChordApiDemoService.IChordServiceListener;
import com.samsung.chord.samples.apidemo.service.ChordApiDemoService.ChordServiceBinder;

import com.immersion.uhl.Launcher;

public class Trivia extends DroidGap
{
    public static DroidGap sMe = null;
    public static CordovaWebView webView = null;

    protected static final String LOG_TAG = Trivia.class.getSimpleName();
    private static final String TAGClass = "TriviaActivity : ";

    public static Launcher mLauncher = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Set by <content src="index.html" /> in config.xml
        super.loadUrl(Config.getStartUrl());
        // super.loadUrl("file:///android_asset/www.test/index-android.html");

        sMe = this;
      	webView = this.appView;

        startService();
        bindChordService();
        
        try {
          mLauncher = new Launcher(this);
        }
        catch (RuntimeException e) {
          Log.e("Trivia", e.getMessage());
        }
    }


    private String mChannelType = "";


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Log.v(TAG, TAGClass + "onResume");
        // refreshInterfaceType();
    }

    // @Override
    // protected void onDestroy() {
    //     // TODO Auto-generated method stub
    //     super.onDestroy();
    //     unbindChordService();
    //     stopService();
    //     Log.v(TAG, TAGClass + "onDestroy");
    // }


    // **********************************************************************
    // Using Service
    // **********************************************************************


    public static ChordApiDemoService mChordService = null;

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            Log.d(LOG_TAG, TAGClass + "onServiceConnected():"+name);
            ChordServiceBinder binder = (ChordServiceBinder)service;
            Trivia.mChordService = binder.getService();
            // try {
            //     mChordService.initialize(Trivia.this);
            // } catch (Exception e) {
            //     e.printStackTrace();
            // }

            // refreshInterfaceType();
            // mChannelTestFragment.setService(mChordService);
            // mDataTestFragment.setService(mChordService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            Log.i(LOG_TAG, TAGClass + "onServiceDisconnected()");
            Trivia.mChordService = null;
        }
    };

    public void bindChordService() {
        Log.i(LOG_TAG, TAGClass + "bindChordService()");
        if (mChordService == null) {
            Intent intent = new Intent(
                    "com.samsung.chord.samples.apidemo.service.ChordApiDemoService.SERVICE_BIND");
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
    }

    private void unbindChordService() {
        Log.i(LOG_TAG, TAGClass + "unbindChordService()");

        if (null != Trivia.mChordService) {
            unbindService(mConnection);
        }
        mChordService = null;
    }

    private void startService() {
        Log.i(LOG_TAG, TAGClass + "startService()");
        Intent intent = new Intent("com.samsung.chord.samples.apidemo.service.ChordApiDemoService.SERVICE_START");
        startService(intent);
    }

    private void stopService() {
        Log.i(LOG_TAG, TAGClass + "stopService()");
        Intent intent = new Intent("com.samsung.chord.samples.apidemo.service.ChordApiDemoService.SERVICE_STOP");
        stopService(intent);
    }


}

