package com.hisense.checksquare.condition.impl;

import com.hisense.checksquare.condition.IConditioner;
import com.hisense.checksquare.entity.CheckEntity;
import com.hisense.checksquare.widget.log.LogUtil;
import com.hisense.checksquare.widget.log.StringUtil;

import java.text.DecimalFormat;
import java.util.StringTokenizer;

/**
 * Created by yanglijun.ex on 2017/2/24.
 */

public class CommonConditioner implements IConditioner {

    public CommonConditioner() {
    }

    /**
     * implement IConditioner
     * compare with float
     *
     * @return result
     */
    @Override
    public boolean compare(String target, String actual) {
        if (!StringUtil.isEmpty(target, actual)) {
            if (Float.parseFloat(actual) > Float.parseFloat(target)) {
                return true;
            }
        }
        return false;
    }

    /**
     * implement IConditioner
     * compare with float type, conditions split with "," syntax
     *
     * @return result
     */
    @Override
    public boolean compareConditionsFloat(CheckEntity.ConditionMap conditions, float... actual) {
        if (actual == null || actual.length == 0) {
            return false;
        }
        DecimalFormat decimalFormat = new DecimalFormat(".00");         //构造方法的字符格式这里如果小数不足2位,会以0补足.
        try {
            if (conditions.gt != null && !conditions.gt.contains(",")) {
                if (actual[0] >= Float.parseFloat(conditions.gt)) {
                    return true;
                }
            } else if (conditions.just != null && !conditions.just.contains(",")) {
                String actStr = decimalFormat.format(actual[0]);                //format 返回的是字符串
                String justStr = decimalFormat.format(Float.parseFloat(conditions.just));//format 返回的是字符串

                if (actStr.equals(justStr)) {
                    return true;
                }
            } else if (conditions.oneOf != null && conditions.oneOf.contains(",")) {
                String oneOf = conditions.oneOf.trim();
                String toSt = oneOf.substring(oneOf.indexOf("{") + 1, oneOf.lastIndexOf("}"));
                StringTokenizer st = new StringTokenizer(toSt, ",");  //把","作为分割标志，然后把分割好的字符赋予StringTokenizer对象。
                String formatAct = decimalFormat.format(actual[0]);
                while (st.hasMoreTokens()) {                                          //看看此 tokenizer 的字符串中是否还有更多的可用标记。
                    String token = st.nextToken();                                  //返回此 string tokenizer 的下一个标记。
                    if (formatAct.equals(decimalFormat.format(Float.parseFloat(token)))) {
                        return true;
                    }
                }
            } else if (conditions.contain != null && conditions.contain.contains(",")) {
                // 即实际项比配置要求的元素更多，每个元素呈现更优异, etc: {4,5,6,7} compare to {1,2,3}, return true
                String contain = conditions.contain.trim();
                String toSt = contain.substring(contain.indexOf("{") + 1, contain.lastIndexOf("}"));
                int i = 0;
                StringTokenizer st = new StringTokenizer(toSt, ",");
                boolean hasBreak = false;
                while (st.hasMoreTokens()) {
                    String token = st.nextToken();
                    if (i > actual.length || actual[i++] < Float.parseFloat(token)) {
                        hasBreak = true;
                        break;
                    }
                }
                if (!hasBreak) {
                    return true;
                }
            } else if (conditions.gtlt != null && conditions.gtlt.contains(",")) {
                String gtlt = conditions.gtlt.trim();
                String toSp = gtlt.substring(gtlt.indexOf("{") + 1, gtlt.lastIndexOf("}"));
                String[] split = toSp.split(",");
                if (split.length == 2) {
                    boolean hasBreak = false;
                    float low = Float.parseFloat(split[0]);
                    float high = Float.parseFloat(split[1]);
                    for (float a : actual) {
                        if (a < low || a > high) {
                            hasBreak = true;
                            break;
                        }
                    }
                    if (!hasBreak) {
                        return true;
                    }
                }
            }
        } catch (NumberFormatException e) {
            LogUtil.e("compareConditionsFloat(): NumberFormatException = %s", e.toString());
        }
        return false;
    }
}
