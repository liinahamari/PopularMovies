package com.example.guest.popularmovies;

import android.os.Looper;
import android.support.v7.widget.RecyclerView;

import com.example.guest.popularmovies.api.MovDbApi;
import com.example.guest.popularmovies.mvp.model.MoviesArray;
import com.example.guest.popularmovies.mvp.model.SingleMovie;
import com.example.guest.popularmovies.mvp.presenter.MoviesPresenter;
import com.example.guest.popularmovies.mvp.view.MainView;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.reactivestreams.Subscriber;

import java.util.ArrayList;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.intThat;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Observable.class, AndroidSchedulers.class, Looper.class, MoviesArray.class})
public class PresenterTest {

    public static final String TEST_ERROR_MESSAGE = "error_message";

    @InjectMocks
    private MoviesPresenter presenter;
    @Mock
    private MovDbApi apiService;
    @Mock
    private MainView mView;
    @Mock
    private Observable<MoviesArray> observable;
    private Random random;
    private int page;
    RecyclerView recyclerView = Mockito.mock(RecyclerView.class);

    @Captor
    private ArgumentCaptor<Subscriber<MoviesArray>> captor;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        random = new Random();
        page = random.nextInt(992);
        ArrayList<SingleMovie> movies = new ArrayList<>();
        movies.add(new SingleMovie());
//        when(mStorage.getSavedCakes()).thenReturn(movies);
    }

    @Test
    public void getCakes() throws Exception {
        PowerMockito.mockStatic(Looper.class);
//        when(AndroidSchedulers.mainThread()).thenReturn(mRxJavaSchedulersHook.getComputationScheduler());

        when(apiService.getPopular(page)).thenReturn(observable);
        when(observable.subscribeOn(Schedulers.io())).thenReturn(observable);
        when(observable.observeOn(AndroidSchedulers.mainThread())).thenReturn(observable);

//        presenter.getPopular(recyclerView);
//        verify(mView, atLeastOnce()).onShowDialog("Loading cakes....");
    }

/*    @Test
    public void onCompleted() throws Exception {
        presenter.unsubscribe();
        verify(mView, times(1)).onHideDialog();
        verify(mView, times(1)).onShowToast("Cakes loading complete!");
    }

    @Test
    public void onError() throws Exception {
        presenter.onError(new Throwable(TEST_ERROR_MESSAGE));
        verify(mView, times(1)).onHideDialog();
        verify(mView, times(1)).onShowToast("Error loading cakes " + TEST_ERROR_MESSAGE);
    }

    @Test
    public void onNext() throws Exception {
        CakesResponse response = mock(CakesResponse.class);
        CakesResponseCakes[] responseCakes = new CakesResponseCakes[1];
        when(response.getCakes()).thenReturn(responseCakes);
        presenter.onNext(response);

        verify(mCakeMapper, times(1)).mapCakes(mStorage, response);
        verify(mView, times(1)).onClearItems();
        verify(mView, times(1)).onCakeLoaded(anyList());
    }

    @Test
    public void getCakesFromDatabase() throws Exception {
        presenter.getCakesFromDatabase();
        verify(mView, times(1)).onClearItems();
        verify(mView, times(1)).onCakeLoaded(anyList());
    }*/
}