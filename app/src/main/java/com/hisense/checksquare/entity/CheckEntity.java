package com.hisense.checksquare.entity;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.hisense.checksquare.widget.ParseUtil;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.converter.PropertyConverter;
import org.greenrobot.greendao.annotation.Generated;


/**
 * Created by yanglijun.ex on 2017/2/9.
 * 非private属性都会接受解析和序列化，即使属性没有写@JsonFields注解，但要先配置 fieldDetectionPolicy
 */
@Entity // greenDao annotation
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS) // loganSquare annotation
public class CheckEntity  implements MultiItemEntity {
    public static final int TYPE_ITEM_VIEW_HW = 1;
    public static final int TYPE_ITEM_VIEW_FUNC = 2;

    public static final String CHECK_STATUS_TOCHECK = "100";
    public static final String CHECK_STATUS_CHECKING = "101";
    public static final String CHECK_STATUS_FAIL = "110";
    public static final String CHECK_STATUS_OK = "111";
    @Id // greenDao annotation
    public String checkId;
    public String checkName;
    public String checkDesc;
    public String checkStatus;  // 100 开启待测，101测试中，110失败，111成功；000关闭待测
    public String actualValue;  // 记录实际结果值
    @Convert(converter = ConditionConverter.class, columnType = String.class)   // greenDao annotation
    public ConditionMap conditionMap;
    public String unit;
    public int type;    // 1 硬件类型，2 接口类型
    public boolean avail;

    public CheckEntity() {
    }

    public CheckEntity(int type) {
        this.type = type;
    }

    @Generated(hash = 1096645706)
    public CheckEntity(String checkId, String checkName, String checkDesc, String checkStatus, String actualValue,
            ConditionMap conditionMap, String unit, int type, boolean avail) {
        this.checkId = checkId;
        this.checkName = checkName;
        this.checkDesc = checkDesc;
        this.checkStatus = checkStatus;
        this.actualValue = actualValue;
        this.conditionMap = conditionMap;
        this.unit = unit;
        this.type = type;
        this.avail = avail;
    }


    @Override   // MultiItemEntity annotation
    public int getItemType() {
        return type;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("CheckEntity{")
                .append("checkId='").append(checkId).append("\'")
                .append(", checkName='").append(checkName).append("\'")
                .append(", checkDesc='" ).append(checkDesc).append("\'")
                .append(", checkStatus='" ).append( checkStatus ).append("\'")
                .append(", actualValue='" ).append( actualValue ).append("\'")
                .append(", checkResult='" ).append( conditionMap ).append("\'")
                .append(", checkServices='" ).append( unit ).append("\'")
                .append(", type=" ).append( type)
                .append("}").toString();
    }

    public String getCheckId() {
        return this.checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getCheckName() {
        return this.checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    public String getCheckDesc() {
        return this.checkDesc;
    }

    public void setCheckDesc(String checkDesc) {
        this.checkDesc = checkDesc;
    }

    public String getCheckStatus() {
        return this.checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getActualValue() {
        return this.actualValue;
    }

    public void setActualValue(String actualValue) {
        this.actualValue = actualValue;
    }

    public ConditionMap getConditionMap() {
        return this.conditionMap;
    }

    public void setConditionMap(ConditionMap conditionMap) {
        this.conditionMap = conditionMap;
    }

    public String getUnit() {
        return this.unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean getAvail() {
        return this.avail;
    }

    public void setAvail(boolean avail) {
        this.avail = avail;
    }

    @JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
    public static class ConditionMap {
        public String gt;
        public String just;
        public String oneOf;
        public String contain;
        public String gtlt;

        @Override
        public String toString() {
            return new StringBuilder().append("ConditionMap{")
                    .append("gt='").append(gt).append("\'")
                    .append(", just='").append(just).append("\'")
                    .append(", oneOf='" ).append(oneOf).append("\'")
                    .append(", contain='" ).append( contain ).append("\'")
                    .append(", gtlt='" ).append( gtlt ).append("\'")
                    .append("}").toString();
        }
    }

    public static class ConditionConverter implements PropertyConverter<ConditionMap, String> {
        @Override
        public ConditionMap convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }

            return ParseUtil.parse(databaseValue, ConditionMap.class);

        }

        @Override
        public String convertToDatabaseValue(ConditionMap entityProperties) {
            return ParseUtil.serialize(entityProperties);
        }
    }













    /*
    *  LoganSquare annotation samples
    * -----------------------------------------------------
    * *//*
     *普通声明的属性默认会被解析和序列化
     *//*
    public String format;

    *//*
     *重命名key还是需要注解来指出的
     *//*
    @JsonField(name = "_id")
    public int imageId;

    *//*
     * 包访问权限的处理是没问题的
     *//*
    List<Image> similarImages;

    *//*
     * 用@JsonIgnore来忽略不想被解析和序列化的属性
     *//*
    @JsonIgnore
    public int nonJsonField;

    *//*
     * 该策略下private属性默认忽略
     *//*
    private int privateInt;

    public int getPrivateInt() {
        return privateInt;
    }

    public void setPrivateInt(int i) {
        privateInt = i;
    }

    @OnJsonParseComplete void onParseComplete() {
        // 解析完成后干点什么
    }

    @OnPreJsonSerialize void onPreSerialize() {
        //序列化前干点什么
    }
    * ------------------------------------------------------
    */
}
