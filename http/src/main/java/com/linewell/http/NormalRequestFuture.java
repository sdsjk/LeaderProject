package com.linewell.http;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by caicai on 2016-08-31.
 */
public class NormalRequestFuture<T> extends ListenerHandler<T> implements Future<T>{

    private Request<?> mRequest;
    private boolean mResultReceived = false;
    private T mResult;
    private VolleyError mException;

    public static <E> NormalRequestFuture<E> newFuture() {
        return new NormalRequestFuture<E>();
    }

    private NormalRequestFuture() {

    }

    public void setRequest(Request<?> request) {
        mRequest = request;
    }

    @Override
    public synchronized boolean cancel(boolean mayInterruptIfRunning) {
        if (mRequest == null) {
            return false;
        }

        if (!isDone()) {
            mRequest.cancel();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        try {
            return doGet(null);
        } catch (TimeoutException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public T get(long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        return doGet(TimeUnit.MILLISECONDS.convert(timeout, unit));
    }

    private synchronized T doGet(Long timeoutMs)
            throws InterruptedException, ExecutionException, TimeoutException {
        if (mException != null) {
            throw new ExecutionException(mException);
        }


        if (timeoutMs == null) {
            timeoutMs = 500l;
        }

        int time = (int) (timeoutMs/50);

        do {

            if (mResultReceived) {
                return mResult;
            }
            time--;
            Thread.sleep(50l);

        }while (time>0);

        if (mException != null) {
            throw new ExecutionException(mException);
        }

        if (!mResultReceived) {
            throw new TimeoutException();
        }

        return mResult;
    }

    @Override
    public boolean isCancelled() {
        if (mRequest == null) {
            return false;
        }
        return mRequest.isCanceled();
    }

    @Override
    public synchronized boolean isDone() {
        return mResultReceived || mException != null || isCancelled();
    }

    @Override
    public synchronized void onResponse(T response) {
        mResultReceived = true;
        mResult = response;
    }

    @Override
    public synchronized void onErrorResponse(VolleyError error) {
        mException = error;
    }

}
