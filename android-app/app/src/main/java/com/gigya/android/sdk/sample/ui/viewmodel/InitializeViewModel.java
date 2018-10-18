package com.gigya.android.sdk.sample.ui.viewmodel;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.gigya.android.sdk.sample.managers.RegistrationManager;

public class InitializeViewModel extends AndroidViewModel {

    private static final String TAG = "InitializeViewModel";


    public InitializeViewModel(@NonNull Application application) {
        super(application);
    }



    /**
     *
     * @param apiKey
     */
    public void initialize(String apiKey, String dataCenter){
        RegistrationManager.getInstance().initialize(getApplication(), apiKey, dataCenter);
    }
}
