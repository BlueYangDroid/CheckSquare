package com.hisense.checksquare.condition;

import com.hisense.checksquare.entity.CheckEntity;

/**
 * Created by yanglijun.ex on 2017/2/24.
 */

public interface IConditioner {

    /**
     *  do real compare with String
     */
    boolean compare(String target, String result);

    /**
     *  do real compare with float
     */
    boolean compareConditionsFloat(CheckEntity.ConditionMap conditions, float... actual);

}
