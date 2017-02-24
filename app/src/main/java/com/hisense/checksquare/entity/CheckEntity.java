package com.hisense.checksquare.entity;

import com.bluelinelabs.logansquare.LoganSquare;
import com.bluelinelabs.logansquare.ParameterizedType;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.hisense.checksquare.widget.LogUtil;
import com.hisense.checksquare.widget.ParseUtil;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.io.IOException;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by yanglijun.ex on 2017/2/9.
 * 非private属性都会接受解析和序列化，即使属性没有写@JsonFields注解，但要先配置 fieldDetectionPolicy
 */
@Entity // greenDao annotation
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
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
    public String checkResult;
    @Convert(converter = ServiceConverter.class, columnType = String.class)
    public List<CheckService> checkServices;
    public int type;    // 1 硬件类型，2 接口类型

    public CheckEntity() {
    }

    public CheckEntity(int type) {
        this.type = type;
    }

    @Generated(hash = 1032982739)
    public CheckEntity(String checkId, String checkName, String checkDesc, String checkStatus,
            String checkResult, List<CheckService> checkServices, int type) {
        this.checkId = checkId;
        this.checkName = checkName;
        this.checkDesc = checkDesc;
        this.checkStatus = checkStatus;
        this.checkResult = checkResult;
        this.checkServices = checkServices;
        this.type = type;
    }

    @Override
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
                .append(", checkResult='" ).append( checkResult ).append("\'")
                .append(", checkServices='" ).append( checkServices ).append("\'")
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

    public String getCheckResult() {
        return this.checkResult;
    }

    public void setCheckResult(String checkResult) {
        this.checkResult = checkResult;
    }

    public List<CheckService> getCheckServices() {
        return this.checkServices;
    }

    public void setCheckServices(List<CheckService> checkServices) {
        this.checkServices = checkServices;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static class ServiceConverter implements PropertyConverter<List<CheckService>, String> {
        @Override
        public List<CheckService> convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }

            return ParseUtil.parseList(databaseValue, CheckService.class);

        }

        @Override
        public String convertToDatabaseValue(List<CheckService> entityProperties) {
            return ParseUtil.serialize(entityProperties, CheckService.class);
        }
    }













    /*    *//*
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
    }*/
}
