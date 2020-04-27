package com.android.dubaicovid19.view.activities;

import android.app.Activity;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.databinding.DataBindingUtil;

import com.android.dubaicovid19.CovidTrackerApp;
import com.android.dubaicovid19.R;
import com.android.dubaicovid19.data.model.PostGetNearByCovidCenter;
import com.android.dubaicovid19.data.model.PostNearByCovidRegisteredDevice;
import com.android.dubaicovid19.data.model.ResponseNearByCovidRegisteredDevice;
import com.android.dubaicovid19.data.network.ApiFactory;
import com.android.dubaicovid19.data.network.NetworkConnectionInterceptor;
import com.android.dubaicovid19.data.repositories.BluetoothRepository;
import com.android.dubaicovid19.databinding.FragmentRiskStatusBinding;
import com.android.dubaicovid19.utility.Global;
import com.android.dubaicovid19.utility.constant.AppConstants;
import com.android.dubaicovid19.view.custom.bluetooth.Config;
import com.android.dubaicovid19.view.custom.bluetooth.IBluetooth;
import com.android.dubaicovid19.view.custom.bluetooth.IDetected;
import com.android.dubaicovid19.view.custom.bluetooth.controller.BluetoothController;
import com.android.dubaicovid19.view.factories.BluetoothFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class BluetoothDetection extends AppCompatActivity implements IBluetooth {

    private String TAG = BluetoothDetection.class.getCanonicalName();
    private FragmentRiskStatusBinding mainBinding;
    private BluetoothController controller;
    private Config config;
    private IDetected listener = null;
    ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();
    ArrayList<String> strUUID;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.fragment_risk_status);
        mDeviceList = new ArrayList<BluetoothDevice>();
        controller = new BluetoothController(this, this, receiver);
        config = getIntent().getParcelableExtra(Config.EXTRA_CONFIG);
        if (getIntent().hasExtra(Config.EXTRA_Listener))
            listener = (IDetected) getIntent().getSerializableExtra(Config.EXTRA_Listener);

        //Initializing data from config
        //mainBinding.title.setText(TextUtils.isEmpty(config.getTitle()) ? getString(R.string.title) : config.getTitle());
        //mainBinding.backgroundcolor.setBackgroundColor(config.getBackgroundcolor() == 0 ? getResources().getColor(R.color.backgroundcolor) : config.getBackgroundcolor());
        mainBinding.pulsator.setColor(config.getPulsecolor() == 0 ? getResources().getColor(R.color.pulsecolor) : config.getPulsecolor());
        mainBinding.pulsator.setAvators(config.getAvatars());

        // let us check bluetoothSupports or not if not then finish the activity
        if (controller.checkIfDeviceSupports()) {
            Toast.makeText(getApplicationContext(), "Device not Support Bluetooth", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        controller.setName();

        //Check the bluetooth persmission
        controller.enableAllPermission();

        //Start Animation
        startPulse();

        handler.postDelayed(apiRunnable, 10000);
    }

    private Runnable apiRunnable = new Runnable() {
        @Override
        public void run() {
            //getUUID();
            //apiNearByDevices(strUUID.toArray(new String[strUUID.size()]));
            apiNearByDevices();

            /*if(strUUID != null && strUUID.size() > 0){
                String[] finalList = strUUID.toArray(new String[strUUID.size()]);
                apiNearByDevices(finalList);

            } else {
                Global.isBluetoothScan = true;
                Global.safeDevice = -1;
                Global.riskDevice = -1;
                finish();
            }*/
        }
    };

    public String getUUID(){
        if(strUUID == null) {
            strUUID = new ArrayList<>();
        }
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        try {
            Method getUuidsMethod = BluetoothAdapter.class.getDeclaredMethod("getUuids", null);
            ParcelUuid[] uuids = (ParcelUuid[]) getUuidsMethod.invoke(adapter, null);
            //return uuids[0].getUuid().toString();
            //generateUUID(uuidExtra);
            for (int i = 0; i < uuids.length; i++){
                String UUID = uuids[i].getUuid().toString();
                if(!strUUID.contains(UUID)) {
                    strUUID.add(UUID);
                }
            }
        } catch (Exception ex){

        }
        return "";
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Global.isBluetoothScan = true;
        Global.safeDevice = -1;
        Global.riskDevice = -1;
        controller.stopDiscovery();
        handler.removeCallbacks(apiRunnable);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BluetoothController.REQUEST_ENABLE_BT && resultCode == Activity.RESULT_OK) {
            controller.startDiscoveryDelay();
        } else
            finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (requestCode == BluetoothController.MY_PERMISSIONS_REQUEST_Location) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                controller.enableBluetooth();
            } else {
                finish();
            }
        }
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                mainBinding.pulsator.clearedDetectedDevices();
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mainBinding.pulsator.addDetecteddevice(device);
                if (device != null && !TextUtils.isEmpty(device.getAddress())) {
                    if(strUUID == null) {
                        strUUID = new ArrayList<>();
                    }
                    if(!strUUID.contains(device.getAddress())) {
                        strUUID.add(device.getAddress());
                    }
                }
                mDeviceList.add(device);
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                controller.startDiscovery();
                Iterator<BluetoothDevice> itr = mDeviceList.iterator();
                while (itr.hasNext()) {
                    // Get Services for paired devices
                    BluetoothDevice device = itr.next();
                    boolean result = device.fetchUuidsWithSdp();
                    /* if (!device.fetchUuidsWithSdp()) {
                        out.append("\nSDP Failed for " + device.getName());
                    }*/
                }
            } else if (BluetoothDevice.ACTION_UUID.equals(action)) {
                BluetoothDevice deviceExtra = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Parcelable[] uuidExtra = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);
                System.out.println("DeviceExtra address - " + deviceExtra.getAddress());
                //apiNearByDevices(generateUUID(uuidExtra));
                //generateUUID(uuidExtra);
                if (!mDeviceList.isEmpty()) {
                    BluetoothDevice device = mDeviceList.remove(0);
                    boolean result = device.fetchUuidsWithSdp();
                }
            }
        }
    };

    private void generateUUID(Parcelable[] uuidExtra){
        if(strUUID == null) {
            strUUID = new ArrayList<>();
        }
        if (uuidExtra != null) {
            for (Parcelable p : uuidExtra) {
                String UUID = ((ParcelUuid) uuidExtra[0]).getUuid().toString();
                if(!strUUID.contains(UUID)) {
                    strUUID.add(UUID);
                }
            }
        } else {
            System.out.println("uuidExtra is still null");
        }
        //String[] finalList = strUUID.toArray(new String[strUUID.size()]);
    }



    public void apiNearByDevices(){
        handler.removeCallbacks(apiRunnable);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        //String url = AppConstants.NEAR_BY_COVID;
        BluetoothRepository repository = null;
        BluetoothFactory factory;
        try {
            repository = new BluetoothRepository(ApiFactory.getClient(new NetworkConnectionInterceptor(getApplicationContext())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        factory = new BluetoothFactory(getApplicationContext(), repository);
        CovidTrackerApp covidTrackerApp = CovidTrackerApp.create(getApplicationContext());

        String url = AppConstants.NearByDevicesByLoc;

        /*PostNearByCovidRegisteredDevice model = new PostNearByCovidRegisteredDevice();
        model.setListUUID(uuid);*/

        PostGetNearByCovidCenter model = new PostGetNearByCovidCenter();
        model.setLat(Double.parseDouble(getSharedPreferences("MyPref", 0).getString("lat","")));
        model.setLon(Double.parseDouble(getSharedPreferences("MyPref", 0).getString("lon","")));
        model.setUserId(getSharedPreferences("MyPref", 0).getInt(AppConstants.USER_ID_KEY,-1));

        Disposable disposable = nearByCovidRegisteredDevice(url, model)
                .subscribeOn(covidTrackerApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseNearByCovidRegisteredDevice>() {
                    @Override public void accept(ResponseNearByCovidRegisteredDevice response) throws Exception {
                        if(response != null){
                            if(response.Status.toLowerCase().equals("success")){
                                Global.isBluetoothScan = true;
                                Global.riskDevice = response.riskDeviceCount;
                                Global.safeDevice = response.safeDeviceCount;
                                finish();
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(Throwable throwable) throws Exception {
                        Global.isBluetoothScan = true;
                        Global.riskDevice = -1;
                        Global.safeDevice = -1;
                        finish();
                    }
                });

        /*Disposable disposable = nearByCovidRegisteredDevice(url, model)
                .subscribeOn(covidTrackerApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseNearByCovidRegisteredDevice>() {
                    @Override public void accept(ResponseNearByCovidRegisteredDevice response) throws Exception {
                        if(response != null){
                            if(response.Status.toLowerCase().equals("success")){
                                Global.isBluetoothScan = true;
                                Global.riskDevice = response.riskDevice;
                                Global.safeDevice = response.safeDevice;
                                finish();
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(Throwable throwable) throws Exception {
                        Global.isBluetoothScan = true;
                        Global.riskDevice = -1;
                        Global.safeDevice = -1;
                        finish();
                    }
                });*/

        compositeDisposable.add(disposable);
    }

    public Observable<ResponseNearByCovidRegisteredDevice> nearByCovidRegisteredDevice(String url, PostGetNearByCovidCenter model){
        try {
            return ApiFactory.getClient(new NetworkConnectionInterceptor(getApplicationContext())).nearByDevicesByLoc(url, model);
        } catch (Exception ex){

        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Global.isBluetoothScan = true;
        Global.safeDevice = -1;
        Global.riskDevice = -1;
        controller.stopDiscovery();
        handler.removeCallbacks(apiRunnable);
        unregisterReceiver(receiver);
    }

    @Override
    public void startPulse() {

        mainBinding.pulsator.post(new Runnable() {
            @Override
            public void run() {
                mainBinding.pulsator.setListener(listener);
                mainBinding.pulsator.start();
            }
        });
    }

    @Override
    public void setName(final String displayname) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainBinding.name.setText(displayname);
            }
        });
    }

}
