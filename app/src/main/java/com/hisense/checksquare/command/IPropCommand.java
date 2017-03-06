package com.hisense.checksquare.command;

import com.hisense.checksquare.entity.CheckEntity;

/**
 * Created by yanglijun.ex on 2017/2/23.
 */

public interface IPropCommand {

    /**
     * do real check
     * @param entity
     * @return
     */
    CheckEntity checkProp(CheckEntity entity);
}
