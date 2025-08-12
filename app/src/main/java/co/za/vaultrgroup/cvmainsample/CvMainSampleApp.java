package co.za.vaultrgroup.cvmainsample;

import android.app.Application;

import co.za.vaultgroup.cvmain_android.CvMainService;

public class CvMainSampleApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CvMainService.start(getApplicationContext());

        System.out.println("Lib version " + CvMainService.getLibVersion());
//        uncomment to stop
//        CvMainService.stop();
    }
}
