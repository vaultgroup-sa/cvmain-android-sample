package co.za.vaultrgroup.cvmainsample;

import android.app.Application;

import co.za.vaultgroup.cvmain_android.CvMainConfiguration;
import co.za.vaultgroup.cvmain_android.CvMainService;
import co.za.vaultgroup.cvmain_android.CvMasterConfiguration;

public class CvMainSampleApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CvMainConfiguration configuration = new CvMainConfiguration.Builder()
                .localServer(new CvMainConfiguration.LocalServer("127.0.0.1:8888"))
                .mapping(new int[]{4, 6, 8})
                .useCvLocks(false)
                .useMultistateSlave(false)
                .useKeypad(false)
                .build();

        CvMasterConfiguration cvMasterConfiguration = new CvMasterConfiguration.Builder()
                .setTcp485Passthrough("192.168.8.3:2320")
                .build();

        CvMainService.configure(getApplicationContext(), configuration, cvMasterConfiguration);

        CvMainService.start(getApplicationContext());

//        uncomment to stop
//        CvMainService.stop();
    }
}
