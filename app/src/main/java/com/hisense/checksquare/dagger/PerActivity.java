package com.hisense.checksquare.dagger;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by yanglijun.ex on 2017/2/16.
 */
@Scope
@Documented
@Retention(RetentionPolicy.RUNTIME)

public @interface PerActivity {
}
