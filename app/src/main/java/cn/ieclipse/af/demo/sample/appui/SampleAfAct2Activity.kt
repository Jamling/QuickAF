package cn.ieclipse.af.demo.sample.appui

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import cn.ieclipse.af.demo.databinding.SampleAfActivityBinding

class SampleAfAct2Activity : AppCompatActivity() {

    var binding: SampleAfActivityBinding? = null
    var viewModel: SampleAfAct2ViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(SampleAfAct2ViewModel::class.java);
    }
}

class SampleAfAct2ViewModel(application: Application) : AndroidViewModel(application) {

    private val status: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    init {
        status.value = "";
    }

    var context: Context = application

    fun toogleOverylay() {

    }
}
