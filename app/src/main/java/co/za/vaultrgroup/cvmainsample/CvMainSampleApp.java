package co.za.vaultrgroup.cvmainsample;

import android.app.Application;

import co.za.vaultrgroup.cvmainrs.CvRunner;

public class CvMainSampleApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CvRunner cvRunner = CvRunner.getInstance();
        cvRunner.startProcess(getApplicationContext());
    }
}
