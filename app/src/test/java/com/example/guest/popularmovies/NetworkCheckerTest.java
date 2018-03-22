package com.example.guest.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.guest.popularmovies.utils.NetworkChecker;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowConnectivityManager;
import org.robolectric.shadows.ShadowNetworkInfo;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, manifest = "/src/main/AndroidManifest.xml")
public class NetworkCheckerTest {

    private ConnectivityManager connectivityManager;
    private ShadowConnectivityManager shadowConnectivityManager;
    private ShadowNetworkInfo shadowOfActiveNetworkInfo;
    private NetworkChecker networkChecker;

    @Before
    public void setUp() throws IOException {
        networkChecker = new NetworkChecker();
        connectivityManager = getConnectivityManager();
        shadowConnectivityManager = Shadows.shadowOf(connectivityManager);
        shadowOfActiveNetworkInfo = Shadows.shadowOf(connectivityManager.getActiveNetworkInfo());

    }

    @Test
    public void testSimple() {
        NetworkInfo networkInfo = ShadowNetworkInfo.newInstance(NetworkInfo.DetailedState.CONNECTED,
                ConnectivityManager.TYPE_MOBILE, 0, true, true);
        // Correct API call: use setActiveNetworkInfo instead of setNetworkInfo
        shadowConnectivityManager.setActiveNetworkInfo(networkInfo);

        NetworkInfo activeInfo = connectivityManager.getActiveNetworkInfo();
        assertTrue(activeInfo != null && activeInfo.isConnected());

        // Assertion now passes: Correctly returns TYPE_WIFI
        assertEquals(ConnectivityManager.TYPE_WIFI, activeInfo.getType());


    }

    private ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager) RuntimeEnvironment.application.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @After
    public void tearDown(){

    }
}