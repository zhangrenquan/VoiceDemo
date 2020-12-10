package com.demo.mediaplayer.voice.util;


import androidx.annotation.NonNull;

/**
 * @author renquan
 * @date on 2020-12-10 09:17
 * @describe 组合音频 实体类
 * @ideas 开头 + 金额 + 单位
 */

public class VoiceBuilder {
    //开头音频
    private String start;
    //播报金额
    private String money;
    //单位
    private String unit;
    //是否转成全数字。 默认人民币
    private boolean checkNum;

    public String getStart() {
        return start;
    }

    public String getMoney() {
        return money;
    }

    public String getUnit() {
        return unit;
    }

    public boolean isCheckNum() {
        return checkNum;
    }

    public static class Builder {
        private String start;
        private String money;
        private String unit;
        private boolean checkNum;

        @NonNull
        public Builder start(String start) {
            this.start = start;
            return this;
        }

        @NonNull
        public Builder money(String money) {
            this.money = StringUtils.getMoney(money);
            return this;
        }

        @NonNull
        public Builder unit(String unit) {
            this.unit = unit;
            return this;
        }

        @NonNull
        public Builder checkNum(boolean checkNum) {
            this.checkNum = checkNum;
            return this;
        }

        @NonNull
        public VoiceBuilder builder() {
            return new VoiceBuilder(this);
        }
    }

    public VoiceBuilder(@NonNull Builder builder) {
        this.start = builder.start;
        this.money = builder.money;
        this.unit = builder.unit;
        this.checkNum = builder.checkNum;
    }
}
