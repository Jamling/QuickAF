package cn.ieclipse.af.demo.sample.appui;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.databinding.SampleAfActivityBinding;
import cn.ieclipse.af.demo.sample.SampleBaseActivity;

public class SampleAfActivityActivity extends SampleBaseActivity {

    public LiveData<String> status = new MutableLiveData<>();

    @Override
    protected int getContentLayout() {
        return R.layout.sample_af_activity;
    }

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        mTitleTextView.setText("AfActivity demo");
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        btn3.setVisibility(View.GONE);

        viewModel =
                new ViewModelProvider(
                                this,
                                new ViewModelProvider.AndroidViewModelFactory(getApplication()))
                        .get(AfActivityViewModel.class);

        binding = SampleAfActivityBinding.inflate(getLayoutInflater());
        binding.setVm(this);
    }

    private AfActivityViewModel viewModel;
    private SampleAfActivityBinding binding;

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (btn1 == v) {
            toogleOverlay();
        } else if (btn2 == v) {
            toogleTitleBar(v);
        } else if (btn3 == v) {
            toogleImmersiveMode(v);
        }
    }

    public void toogleOverlay() {
        setOverlay(!isOverlay());
    }

    public void toogleTitleBar(View v) {
        setShowTitleBar(!isShowTitleBar());
    }

    public void toogleImmersiveMode(View v) {
        setImmersiveMode(!isImmersiveMode());
    }

    public static class AfActivityViewModel extends AndroidViewModel {
        private MutableLiveData<String> status; // edit text

        public AfActivityViewModel(@NonNull Application application) {
            super(application);
        }

        public MutableLiveData<String> getStatus() {
            if (status == null) {
                status = new MutableLiveData<>();
                status.setValue(""); // init in main thread

                // set value in sub thread
                // status.postValue("");
            }
            return status;
        }

        public void toggleOverlay() {}
    }
}
