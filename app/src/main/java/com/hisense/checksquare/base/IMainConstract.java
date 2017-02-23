package com.hisense.checksquare.base;

import com.hisense.checksquare.entity.CheckEntity;
import com.hisense.checksquare.entity.CheckEntityGroup;
import com.hisense.checksquare.vpresenter.MainPresenter;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * Created by yanglijun.ex on 2017/2/15.
 */

public interface IMainConstract {

    interface IMainView{
        void onDatasReceived(List<CheckEntity> entities);
        void onCheckItemStatus(String status, CheckEntity entity);
        void onCheckAllResults();

        void onAddCheckCount(int size);
    }

    interface IMainDomain{
        Observable<List<CheckEntity>> getCheckDatas();
        Flowable<CheckEntity> toCheckProperty(CheckEntity entity);
    }
}
