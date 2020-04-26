package top.ss007.infiniterecyclerview.ui.main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import top.ss007.infiniterecyclerview.R;

/**
 * Created by Ben.Wang
 *
 * @author Ben.Wang
 * @modifier
 * @createDate 2020/4/9 17:39
 * @description
 */
public class DataRepository {

    public List<Beauty> getBeautiesByPage(int startIndex, int pageSize) {
        int[] beautyDrawables = new int[]{R.drawable.beauty1, R.drawable.beauty2};

        List<Beauty> beauties = new ArrayList<>();
        //模拟列表数据全部拉取完毕
        if (startIndex > 5 * pageSize) {
            return beauties;
        }
        for (int i = 0; i < pageSize; i++) {
            beauties.add(new Beauty((startIndex + i) + "号", beautyDrawables[i % 2], "关关雎鸠，在河之洲。\n" +
                    "窈窕淑女，君子好逑。\n" +
                    "参差荇菜，左右流之。\n" +
                    "窈窕淑女，寤寐求之。"));
        }
        return beauties;

    }
}
