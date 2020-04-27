package com.android.dubaicovid19.view.fragments;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.dubaicovid19.R;
import com.android.dubaicovid19.databinding.FragmentRiskStatusBinding;
import com.android.dubaicovid19.view.custom.bluetooth.Config;
import com.android.dubaicovid19.view.custom.bluetooth.IBluetooth;
import com.android.dubaicovid19.view.custom.bluetooth.IDetected;
import com.android.dubaicovid19.view.custom.bluetooth.controller.BluetoothController;
import com.android.dubaicovid19.view.viewModels.RiskStatusViewModel;

import java.util.ArrayList;

public class RiskStatusFragment extends Fragment implements IBluetooth, IDetected {
    FragmentRiskStatusBinding binding;
    RiskStatusViewModel model;
    private View mRootView;
    private BluetoothController controller;
    private Config config;
    private IDetected listener = null;
    ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();


    public static RiskStatusFragment newInstance() {
        Bundle args = new Bundle();
        RiskStatusFragment fragment = new RiskStatusFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(RiskStatusViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_risk_status, container, false);
        binding.setVmFragmentRiskStatus(model);
        mRootView = binding.getRoot();
        mDeviceList = new ArrayList<BluetoothDevice>();
        controller = new BluetoothController(getActivity(), this, receiver);
        //config = getActivity().getIntent().getParcelableExtra(Config.EXTRA_CONFIG);
        config = new Config();
        config.setPulsecolor(Color.parseColor("#ffffff"));
        //listener = this;
        /*if (getActivity().getIntent().hasExtra(Config.EXTRA_Listener))
            listener = (IDetected) getActivity().getIntent().getSerializableExtra(Config.EXTRA_Listener);*/
        //listener = this;
        //Initializing data from config
        binding.pulsator.setColor(config.getPulsecolor() == 0 ? getResources().getColor(R.color.white) : config.getPulsecolor());
        binding.pulsator.setAvators(config.getAvatars());

        // let us check bluetoothSupports or not if not then finish the activity
        if (controller.checkIfDeviceSupports()) {
            //finish();
            onDestroy();
        }

        controller.setName();

        controller.enableAllPermission();

        //Start Animation
        startPulse();
        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BluetoothController.REQUEST_ENABLE_BT && resultCode == Activity.RESULT_OK) {
            controller.startDiscoveryDelay();
        } else
            onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (requestCode == BluetoothController.MY_PERMISSIONS_REQUEST_Location) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                controller.enableBluetooth();
            } else {
                onDestroy();
            }
        }
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                binding.pulsator.clearedDetectedDevices();
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                binding.pulsator.addDetecteddevice(device);
                mDeviceList.add(device);
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                controller.startDiscovery();
                if (!mDeviceList.isEmpty()) {
                    BluetoothDevice device = mDeviceList.remove(0);
                    boolean result = device.fetchUuidsWithSdp();
                }
            } else if (BluetoothDevice.ACTION_UUID.equals(action)) {
                // This is when we can be assured that fetchUuidsWithSdp has completed.
                // So get the uuids and call fetchUuidsWithSdp on another device in list

                BluetoothDevice deviceExtra = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Parcelable[] uuidExtra = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);
                System.out.println("DeviceExtra address - " + deviceExtra.getAddress());
                if (uuidExtra != null) {
                    for (Parcelable p : uuidExtra) {
                        String UUID = ((ParcelUuid) uuidExtra[0]).getUuid().toString();
                        System.out.println("uuidExtra - " + p);
                    }
                } else {
                    System.out.println("uuidExtra is still null");
                }
                if (!mDeviceList.isEmpty()) {
                    BluetoothDevice device = mDeviceList.remove(0);
                    boolean result = device.fetchUuidsWithSdp();
                }
            }
        }
    };

    private void initializePage(){

    }

    @Override
    public void startPulse() {

        binding.pulsator.post(new Runnable() {
            @Override
            public void run() {
                //binding.pulsator.setListener(this);
                binding.pulsator.start();
            }
        });
    }

    @Override
    public void setName(final String displayname) {
        /*runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.name.setText(displayname);
            }
        });*/
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSelectedDevice(BluetoothDevice device) {

    }
}