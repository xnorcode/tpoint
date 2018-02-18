package com.tpoint.io.internet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xnorcode on 18/02/2018.
 */

public class InternetConnectivityCheck {

    // context ref
    private Context mContext;
    // RxJava Observable ref
    private Disposable mDisposable;
    // Internet Connectivity callback
    private InternetConnectivityCheckCallback mResponse;


    /*
    Constructor
     */
    public InternetConnectivityCheck(Context context, InternetConnectivityCheckCallback response) {
        this.mContext = context;
        this.mResponse = response;
    }


    /*
    Public method to initiate the check
     */
    public void check() {
        mDisposable = Observable.create(
                (ObservableOnSubscribe<Boolean>) emitter -> {
                    try {
                        emitter.onNext(connect());
                        emitter.onComplete();
                    } catch (Exception e) {
                        emitter.onError(e);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> mResponse.onInternetCheckCompleted(t));
    }


    /*
    Private method to connect to www.google.com
     */
    private boolean connect() {
        boolean result = false;
        // Checking internet connectivity
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        // if network is connected then i need to check internet connection as well.
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            // check internet connection against google server
            HttpURLConnection urlc = null;
            try {
                urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                if (urlc.getResponseCode() == 200) {
                    result = true;
                }
            } catch (IOException e) {
                result = false;
            } finally {
                if (urlc != null) {
                    urlc.disconnect();
                }
            }
        }

        Log.v("NETWORK", "internet connectivity status: " + result);
        return result;
    }


    /*
    Release memory refs
     */
    public void destroy() {
        mContext = null;
        if (mDisposable != null) {
            mDisposable.dispose();
        }
        mDisposable = null;
        mResponse = null;
    }
}
