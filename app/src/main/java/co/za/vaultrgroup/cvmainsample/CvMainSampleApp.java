package co.za.vaultrgroup.cvmainsample;

import android.app.Application;

import co.za.vaultgroup.cvmain_android.CvMainService;

public class CvMainSampleApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CvMainService.start(getApplicationContext());

//        uncomment to stop
//        CvMainService.stop();
    }
}
