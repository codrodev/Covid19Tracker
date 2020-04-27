package com.android.dubaicovid19.view.viewModels;


import androidx.lifecycle.ViewModel;
import java.lang.ref.WeakReference;

public abstract class BaseViewModel<N> extends ViewModel {

    private WeakReference<N> mNavigator;

    public BaseViewModel() {
        /*this.mDataManager = dataManager;
        this.mSchedulerProvider = schedulerProvider;
        this.mCompositeDisposable = new CompositeDisposable();*/
    }

    @Override
    protected void onCleared() {
        //mCompositeDisposable.dispose();
        super.onCleared();
    }


    public N getNavigator() {
        return mNavigator.get();
    }

    public void setNavigator(N navigator) {
        this.mNavigator = new WeakReference<>(navigator);
    }
}
