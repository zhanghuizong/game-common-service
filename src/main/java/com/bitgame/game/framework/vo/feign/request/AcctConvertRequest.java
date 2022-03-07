package com.bitgame.game.framework.vo.feign.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class AcctConvertRequest extends CommonGameApiRequest {
    /**
     * 用户id
     */
    private String openId;

    /**
     * 货币名称
     */
    private String currency;

    /**
     * 游戏订单编号
     */
    private String outOrderNo;

    /**
     * true不扣减账户（捕鱼专用）
     */
    private Boolean decrFlag;

    /**
     * 扣减金额
     */
    protected String amount;

    /**
     * 兑换金额
     */
    protected String converAmount;

    /**
     * 语言配置
     */
    protected List<LangueRow> langueList;

    @Data
    public static class LangueRow {
        /**
         * 语言:zh-Hans中文 en英文 ja日语 ko韩语zh-Hant繁体
         */
        private String langue;

        /**
         * 游戏兑换货币名称
         */
        private String convertName;
    }
}
