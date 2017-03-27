package com.zone.forgithubproject.contract;

import android.graphics.Bitmap;

import com.zone.forgithubproject.base.IBaseView;
import com.zone.forgithubproject.model.bean.LoginCheckBean;

/**
 * Created by zone on 2017/3/26.
 */

public class AdContract {
public interface View extends IBaseView{
    void setAdTime(int count);

    void setLoginCheckBean(LoginCheckBean loginCheckBean);

    void setAdImg(Bitmap bitmap);
}

public interface Presenter {
}

public interface Model{
}


}