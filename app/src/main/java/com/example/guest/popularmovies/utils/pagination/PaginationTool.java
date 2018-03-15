package com.example.guest.popularmovies.utils.pagination;

/**
 * Created by l1maginaire on 3/14/18.
 */

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PaginationTool<T> {

    private static final int EMPTY_LIST_ITEMS_COUNT = 0;
    private static final int MAX_ATTEMPTS_TO_RETRY_LOADING = 3;

    private RecyclerView recyclerView;
    private PagingListener<T> pagingListener;
    private int emptyListCount;
    private int retryCount;
    private boolean emptyListCountPlusToOffset;

    private PaginationTool() {
    }

    public Observable<T> getPagingObservable() {
        int startNumberOfRetryAttempt = 0;
        return getScrollObservable(recyclerView, emptyListCount)
                .subscribeOn(AndroidSchedulers.mainThread())
                .distinctUntilChanged()
                .observeOn(Schedulers.io())
                .switchMap(offset -> PaginationTool.this.getPagingObservable(pagingListener, pagingListener.onNextPage(offset), startNumberOfRetryAttempt, offset, retryCount));
    }

    private Observable<Integer> getScrollObservable(RecyclerView recyclerView, int emptyListCount) {
        return Observable.create(subscriber -> {
            final RecyclerView.OnScrollListener sl = new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (!subscriber.isDisposed()) {
                        int position = getLastVisibleItemPosition(recyclerView);
                        int updatePosition = recyclerView.getAdapter().getItemCount() - 1 - (20 / 2);
                        if (position >= updatePosition) {
                            int actualPage = (recyclerView.getAdapter().getItemCount()/20);
//                            int offset = emptyListCountPlusToOffset ? recyclerView.getAdapter().getItemCount() : recyclerView.getAdapter().getItemCount() - emptyListCount;
                            subscriber.onNext(actualPage);
                        }
                    }
                }
            };
            recyclerView.addOnScrollListener(sl);
            subscriber.setDisposable(new Disposable() {
                @Override
                public void dispose() {
                    recyclerView.removeOnScrollListener(sl);
                }

                @Override
                public boolean isDisposed() {
                    return false;
                }
            });
            if (recyclerView.getAdapter().getItemCount() == emptyListCount) {
                int offset = emptyListCountPlusToOffset ? recyclerView.getAdapter().getItemCount() : recyclerView.getAdapter().getItemCount() - emptyListCount;
                subscriber.onNext(offset);
            }
        });
    }

    private int getLastVisibleItemPosition(RecyclerView recyclerView) {
        Class recyclerViewLMClass = recyclerView.getLayoutManager().getClass();
        if (recyclerViewLMClass == LinearLayoutManager.class || LinearLayoutManager.class.isAssignableFrom(recyclerViewLMClass)) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            return linearLayoutManager.findLastVisibleItemPosition();
        } else {
            throw new PagingException("Unknown LayoutManager class: " + recyclerViewLMClass.toString());
        }
    }

    private Observable<T> getPagingObservable(PagingListener<T> listener, Observable<T> observable,
                                              int numberOfAttemptToRetry, int page, int retryCount) {
        return observable.onErrorResumeNext(throwable -> {
            if (numberOfAttemptToRetry < retryCount) {
                int attemptToRetryInc = numberOfAttemptToRetry + 1;
                return getPagingObservable(listener, listener.onNextPage(page), attemptToRetryInc, page, retryCount);
            } else {
                return Observable.empty();
            }
        });
    }

    public static <T> Builder<T> buildPagingObservable(RecyclerView recyclerView, PagingListener<T> pagingListener) {
        return new Builder<>(recyclerView, pagingListener);
    }

    public static class Builder<T> {

        private RecyclerView recyclerView;
        private PagingListener<T> pagingListener;
        private int emptyListCount = EMPTY_LIST_ITEMS_COUNT;
        private int retryCount = MAX_ATTEMPTS_TO_RETRY_LOADING;
        private boolean emptyListCountPlusToOffset = false;

        private Builder(RecyclerView recyclerView, PagingListener<T> pagingListener) {
            if (recyclerView == null) {
                throw new PagingException("null recyclerView");
            }
            if (recyclerView.getAdapter() == null) {
                throw new PagingException("null recyclerView adapter");
            }
            if (pagingListener == null) {
                throw new PagingException("null pagingListener");
            }
            this.recyclerView = recyclerView;
            this.pagingListener = pagingListener;
        }

        public Builder<T> setEmptyListCount(int emptyListCount) {
            if (emptyListCount < 0) {
                throw new PagingException("emptyListCount must be not less then 0");
            }
            this.emptyListCount = emptyListCount;
            return this;
        }

        public Builder<T> setRetryCount(int retryCount) {
            if (retryCount < 0) {
                throw new PagingException("retryCount must be not less then 0");
            }
            this.retryCount = retryCount;
            return this;
        }

        public Builder<T> setEmptyListCountPlusToOffset(boolean emptyListCountPlusToOffset) {
            this.emptyListCountPlusToOffset = emptyListCountPlusToOffset;
            return this;
        }

        public PaginationTool<T> build() {
            PaginationTool<T> paginationTool = new PaginationTool<>();
            paginationTool.recyclerView = this.recyclerView;
            paginationTool.pagingListener = pagingListener;
            paginationTool.emptyListCount = emptyListCount;
            paginationTool.retryCount = retryCount;
            paginationTool.emptyListCountPlusToOffset = emptyListCountPlusToOffset;
            return paginationTool;
        }

    }
}
