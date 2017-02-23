package com.hisense.checksquare.entity;

import java.util.List;

/**
 * Created by yanglijun.ex on 2017/2/21.
 */

public class CheckEntityGroup {
    public List<CheckEntity> propEntities;
    public List<CheckEntity> funcEntities;

    public CheckEntityGroup(List<CheckEntity> propEntities, List<CheckEntity> funcEntities) {
        this.propEntities = propEntities;
        this.funcEntities = funcEntities;
    }
}
