package com.example.guest.popularmovies;

import com.example.guest.popularmovies.mvp.presenter.MoviesPresenter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private MoviesPresenter presenter;

    @Before
    public void setUp() throws Exception {
        presenter = new MoviesPresenter();
    }

    @Test
    public void presenterTest(){
        assertNotNull(presenter);
    }

    @After
    public void tearDown(){
        presenter = null;
    }
}