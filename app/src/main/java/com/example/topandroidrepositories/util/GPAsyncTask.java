package com.example.topandroidrepositories.util;

import androidx.annotation.MainThread;
import androidx.annotation.WorkerThread;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public abstract class GPAsyncTask<Params, Progress, Result> {
    private static final String LOG_TAG = "STAsyncTask";
    private Disposable disposable;
    private boolean mCancelled = false;
    private volatile Status mStatus;

    public GPAsyncTask() {
        mCancelled = false;
        this.mStatus = Status.PENDING;
    }

    @WorkerThread
    protected abstract Result doInBackground(Params... var1);

    @MainThread
    protected void onPreExecute() {
    }

    @MainThread
    protected void onPostExecute(Result var1) {
    }

    @SafeVarargs
    @MainThread
    public final GPAsyncTask<Params, Progress, Result> execute(final Params... var1) {
        this.onPreExecute();
        disposable = Observable.create(new ObservableOnSubscribe<Result>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Result> emitter) {
                try {
                    GPAsyncTask.this.mStatus = Status.RUNNING;
                    Result result = doInBackground(var1);
                    if (result != null) {
                        emitter.onNext(result);
                    } else {
                        emitter.onNext((Result) "null");
                    }
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Result>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        GPAsyncTask.this.mStatus = Status.FINISHED;
                    }

                    @Override
                    public void onNext(@NonNull Result result) {
                        if (result.equals("null")) {
                            if(isCancelled()){
                                GPAsyncTask.this.onCancelled(null);
                            }else{
                                GPAsyncTask.this.onPostExecute(null);
                            }
                        } else {
                            if(isCancelled()){
                                GPAsyncTask.this.onCancelled(result);
                            }else {
                                GPAsyncTask.this.onPostExecute(result);
                            }
                        }
                    }
                });

        return this;
    }

    @MainThread
    protected void onCancelled(Result result) {
        this.onCancelled();
    }

    @MainThread
    protected void onCancelled() {
    }

    public boolean cancel(boolean var1) {
        if (var1) {
            if (unsubscribe()) {
                mCancelled = true;
                this.onCancelled();
                return true;
            }

        }
        mCancelled = false;
        return false;
    }

    public boolean unsubscribe() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            return true;
        }
        return false;
    }

    public boolean isCancelled() {
        return mCancelled;
    }

    public final Status getStatus() {
        return this.mStatus;
    }

    public enum Status {
        PENDING,
        RUNNING,
        FINISHED;

        Status() {
        }
    }
}