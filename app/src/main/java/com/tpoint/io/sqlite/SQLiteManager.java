package com.tpoint.io.sqlite;

import android.content.Context;

import com.tpoint.data.TPoint;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xnorcode on 18/02/2018.
 */

public class SQLiteManager {

    private SQLiteManagerCallback mCallback;
    private Disposable mDisposable;
    private Disposable mDisposableScheduler;


    /*
    Constructor
     */
    public SQLiteManager(SQLiteManagerCallback callback) {
        this.mCallback = callback;
    }


    /*
    Save single Point
     */
    public void savePoint(Context context, TPoint point) {
        mDisposable = Single.just(SQLiteHelper.savePoint(context, point))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> mCallback.pointSaved(t));
    }


    /*
    Save all Points
     */
    public void savePoint(Context context, ArrayList<TPoint> points) {
        mDisposable = Single.just(SQLiteHelper.savePoint(context, points))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> mCallback.allPointsSaved(t));
    }


    /*
    Get a specific point
     */
    public void getPoint(Context context, long id) {
        mDisposable = Single.just(SQLiteHelper.getPoint(context, id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> mCallback.pointGet(t));
    }


    /*
    Update point
     */
    public void updatePoint(Context context, TPoint point) {
        mDisposable = Single.just(SQLiteHelper.updatePoint(context, point))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> mCallback.pointUpdated(t));
    }


    /*
    Delete point
     */
    public void deletePoint(Context context, TPoint point) {
        mDisposable = Single.just(SQLiteHelper.deletePoint(context, point.getFirebaseID()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> mCallback.pointDeleted(t));
    }


    /*
    Delete all point
     */
    public void deleteAllPoints(Context context, boolean all) {
        mDisposable = Single.just(SQLiteHelper.deletePoint(context, all))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> mCallback.allPointsDeleted(t));
    }


    /*
    Performs a check every 10 seconds for nearby points
     */
    public void enablePointCheckScheduler(Context context, TPoint current) {
        mDisposableScheduler = Observable.interval(10, 10, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> mCallback.schedulerAlertCheck(SQLiteHelper.checkPointsDistance(context, current)));
    }


    /*
    Release memory refs
     */
    public void destroy() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
        mDisposable = null;
        if (mDisposableScheduler != null) {
            mDisposableScheduler.dispose();
        }
        mDisposableScheduler = null;
        mCallback = null;
    }

}
