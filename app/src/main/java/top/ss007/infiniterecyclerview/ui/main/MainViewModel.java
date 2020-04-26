package top.ss007.infiniterecyclerview.ui.main;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    public final List<Beauty> dataSource = new ArrayList<>();
    private DataRepository mDataRepository;
    private MutableLiveData<List<Beauty>> beautyLd;

    public MutableLiveData<List<Beauty>> getBeautyLd() {
        if (beautyLd == null) {
            beautyLd = new MutableLiveData<>();
        }
        return beautyLd;
    }


    public void getBeautyListByPage(int startIndex, int pageSize) {
        if (mDataRepository == null) {
            mDataRepository = new DataRepository();
        }
        getBeautyLd().setValue(mDataRepository.getBeautiesByPage(startIndex, pageSize));
    }
}