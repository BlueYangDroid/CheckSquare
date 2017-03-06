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
        /**
         * 初始化配置检查项读取返回
         * @param entities
         */
        void onDatasReceived(List<CheckEntity> entities);

        void onCheckAllResults();

        /**
         * 单项检查的某个状态返回
         * @param status 多个状态，包括Checking，OK，Fail
         * @param entity 包含当前状态的检查项数据
         */
        void onCheckItemStatus(String status, CheckEntity entity);

        /**
         * 启动检查的数量，供UI计数等待
         * @param size
         */
        void onAddCheckCount(int size);
    }

    interface IMainDomain{
        /**
         * 获取初始化配置数据
         * @return
         */
        Observable<List<CheckEntity>> getCheckDatas();

        /**
         * 检查某项属性
         * @param entity
         * @return
         */
        Flowable<CheckEntity> toCheckProperty(CheckEntity entity);
    }
}
