package com.bitgame.game.framework.util;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.alibaba.fastjson.JSON;
import com.bitgame.game.framework.config.game.JavaConfig;
import com.bitgame.game.framework.constant.GameApiCode;
import com.bitgame.game.framework.feign.GameServiceClient;
import com.bitgame.game.framework.vo.feign.request.*;
import com.bitgame.game.framework.vo.feign.response.BalanceResponse;
import com.bitgame.game.framework.vo.feign.response.CurrencyResponse;
import com.bitgame.game.framework.vo.feign.response.GameResponse;
import com.bitgame.game.framework.vo.feign.response.PriceLegalConvertResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * 游戏 API 接口 工具类
 */
@Slf4j
public class GameApiUtil {
    private final static GameServiceClient gameServiceClient = BeanUtil.getBean(GameServiceClient.class);

    /**
     * 构造请求密文数据
     *
     * @param request 请求数据
     * @return SignRequest
     */
    private static SignRequest getSign(Object request) {
        JavaConfig javaConfig = (JavaConfig) request;
        String publicKey = javaConfig.getPublicKey();

        RSA rsa = SecureUtil.rsa(null, publicKey);
        byte[] encrypt = rsa.encrypt(JSON.toJSON(request).toString().getBytes(), KeyType.PublicKey);

        SignRequest signRequest = new SignRequest();
        signRequest.setSign(Base64.getEncoder().encodeToString(encrypt));
        return signRequest;
    }

    /**
     * 接口返回参数检验
     *
     * @param gameResponse GameResponse
     * @return boolean
     */
    private static boolean checkResponse(GameResponse<?> gameResponse) {
        if (gameResponse == null) {
            return true;
        }

        return !GameApiCode.OK.getCode().equals(gameResponse.getRspCode());
    }

    /**
     * 查询货币配置列表
     *
     * @param request 请求参数
     * @return List<CurrencyResponse>
     */
    public static List<CurrencyResponse> getCurrencyList(GetCurrencyListRequest request) {

        try {
            GameResponse<List<CurrencyResponse>> data = getCurrencyListOrigin(request);
            if (checkResponse(data)) {
                log.error("查询货币配置列表. 返回数据异常. data:{}, request:{}", JSON.toJSON(data), JSON.toJSON(request));
                return null;
            }

            return data.getData();
        } catch (Exception e) {
            log.error("查询货币配置列表异常. request:{}", JSON.toJSON(request), e);
        }

        return null;
    }

    /**
     * 查询货币配置列表
     *
     * @param request 请求参数
     * @return GameResponse
     */
    public static GameResponse<List<CurrencyResponse>> getCurrencyListOrigin(GetCurrencyListRequest request) {
        String gameId = request.getGameNo();

        try {
            log.info("请求:/currency/getCurrencyList. request:{}", JSON.toJSON(request));
            GameResponse<List<CurrencyResponse>> response = gameServiceClient.getCurrencyList(request.getApiKey(), getSign(request));
            log.info("返回:/currency/getCurrencyList. data:{}", JSON.toJSON(response));
            isCheckSend(gameId, "/currency/getCurrencyList", request, response);

            return response;
        } catch (Exception e) {
            log.error("查询货币配置列表异常. request:{}", JSON.toJSON(request), e);
            TelegramUtil.sendMsg(gameId, "game_api_util:get_currency_list_origin", "查询货币配置列表异常");
        }

        return null;
    }

    /**
     * 批量查询用户账户余额
     *
     * @param request 请求参数
     * @return Map<String, BalanceResponse>
     */
    public static Map<String, BalanceResponse> getBalanceList(GetBalanceListRequest request) {
        try {
            GameResponse<Map<String, BalanceResponse>> data = getBalanceListOrigin(request);
            if (checkResponse(data)) {
                log.error("批量查询用户账户余额. 返回数据异常. data:{}, request:{}", JSON.toJSON(data), JSON.toJSON(request));
                return null;
            }

            return data.getData();
        } catch (Exception e) {
            log.error("批量查询用户账户余额异常. request:{}", JSON.toJSON(request), e);
        }

        return null;
    }


    /**
     * 批量查询用户账户余额
     *
     * @param request 请求参数
     * @return GameResponse
     */
    public static GameResponse<Map<String, BalanceResponse>> getBalanceListOrigin(GetBalanceListRequest request) {
        String gameId = request.getGameNo();

        try {
            log.info("请求:/acct/getBalanceList. request:{}", JSON.toJSON(request));
            GameResponse<Map<String, BalanceResponse>> response = gameServiceClient.getBalanceList(request.getApiKey(), getSign(request));
            log.info("返回:/acct/getBalanceList. data:{}", JSON.toJSON(response));
            isCheckSend(gameId, "/acct/getBalanceList", request, response);

            return response;
        } catch (Exception e) {
            log.error("批量查询用户账户余额异常. request:{}", JSON.toJSON(request), e);
            TelegramUtil.sendMsg(gameId, "game_api_util:get_balance_list_origin", "批量查询用户账户余额异常");
        }

        return null;
    }


    /**
     * 单币中用户账户余额查询
     *
     * @param request 请求参数
     * @return BalanceResponse
     */
    public static BalanceResponse getBalance(GetBalanceRequest request) {

        try {
            GameResponse<BalanceResponse> data = getBalanceOrigin(request);
            if (checkResponse(data)) {
                log.error("单币中用户账户余额查询. 返回数据异常. data:{}, request:{}", JSON.toJSON(data), JSON.toJSON(request));
                return null;
            }

            return data.getData();
        } catch (Exception e) {
            log.error("单币中用户账户余额查询异常. request:{}", JSON.toJSON(request), e);
        }

        return null;
    }

    /**
     * 单币中用户账户余额查询
     *
     * @param request 请求参数
     * @return GameResponse
     */
    public static GameResponse<BalanceResponse> getBalanceOrigin(GetBalanceRequest request) {
        String gameId = request.getGameNo();

        try {
            log.info("请求:/acct/getBalance. request:{}", JSON.toJSON(request));
            GameResponse<BalanceResponse> response = gameServiceClient.getBalance(request.getApiKey(), getSign(request));
            log.info("返回:/acct/getBalance. data:{}", JSON.toJSON(response));
            isCheckSend(gameId, "/acct/getBalance", request, response);

            return response;
        } catch (Exception e) {
            log.error("单币中用户账户余额查询异常. request:{}", JSON.toJSON(request), e);
            TelegramUtil.sendMsg(gameId, "game_api_util:get_balance_origin", "单币中用户账户余额查询异常");
        }

        return null;
    }

    /**
     * 用户账户扣减
     *
     * @param request 请求参数
     * @return String
     */
    public static String deduct(DeductRequest request) {

        try {
            GameResponse<String> data = deductOrigin(request);
            if (checkResponse(data)) {
                log.error("用户账户扣减. 返回数据异常. data:{}, request:{}", JSON.toJSON(data), JSON.toJSON(request));
                return null;
            }

            return data.getData();
        } catch (Exception e) {
            log.error("用户账户扣减异常. request:{}", JSON.toJSON(request), e);
        }

        return null;
    }

    /**
     * 用户账户扣减
     *
     * @param request 请求参数
     * @return GameResponse
     */
    public static GameResponse<String> deductOrigin(DeductRequest request) {
        String gameId = request.getGameNo();

        try {
            log.info("请求:/acct/deduct. request:{}", JSON.toJSON(request));
            GameResponse<String> response = gameServiceClient.deduct(request.getApiKey(), getSign(request));
            log.info("返回:/acct/deduct. data:{}", JSON.toJSON(response));
            isCheckSend(gameId, "/acct/deduct", request, response);

            return response;
        } catch (Exception e) {
            log.error("用户账户扣减异常. request:{}", JSON.toJSON(request), e);
            TelegramUtil.sendMsg(gameId, "game_api_util:deduct_origin", "用户账户扣减异常");
        }

        return null;
    }

    /**
     * 用户账户增加
     *
     * @param request 请求参数
     * @return boolean
     */
    public static boolean increase(IncreaseRequest request) {

        try {
            GameResponse<String> data = increaseOrigin(request);
            if (checkResponse(data)) {
                log.error("用户账户增加. 返回数据异常. data:{}, request:{}", JSON.toJSON(data), JSON.toJSON(request));
                return false;
            }

            return true;
        } catch (Exception e) {
            log.error("用户账户增加异常. request:{}", JSON.toJSON(request), e);
        }

        return false;
    }

    /**
     * 用户账户增加
     *
     * @param request 请求参数
     * @return GameResponse
     */
    public static GameResponse<String> increaseOrigin(IncreaseRequest request) {
        String gameId = request.getGameNo();

        try {
            log.info("请求:/acct/increase. request:{}", JSON.toJSON(request));
            GameResponse<String> response = gameServiceClient.increase(request.getApiKey(), getSign(request));
            log.info("返回:/acct/increase. data:{}", JSON.toJSON(response));
            isCheckSend(gameId, "/acct/increase", request, response);

            return response;
        } catch (Exception e) {
            log.error("用户账户增加异常. request:{}", JSON.toJSON(request), e);
            TelegramUtil.sendMsg(gameId, "game_api_util:increase_origin", "用户账户增加异常");
        }

        return null;
    }

    /**
     * 数字货币兑换法币汇率查询
     *
     * @param request 请求参数
     * @return GameResponse
     */
    public static List<PriceLegalConvertResponse> priceLegalConvert(PriceLegalConvertRequest request) {

        try {
            GameResponse<List<PriceLegalConvertResponse>> data = priceLegalConvertOrigin(request);
            if (checkResponse(data)) {
                log.error("数字货币兑换法币汇率查询. 返回数据异常. data:{}, request:{}", JSON.toJSON(data), JSON.toJSON(request));
                return null;
            }

            return data.getData();
        } catch (Exception e) {
            log.error("数字货币兑换法币汇率查询异常. request:{}", JSON.toJSON(request), e);
        }

        return null;
    }

    /**
     * 数字货币兑换法币汇率查询
     *
     * @param request 请求参数
     * @return GameResponse
     */
    public static GameResponse<List<PriceLegalConvertResponse>> priceLegalConvertOrigin(PriceLegalConvertRequest request) {
        String gameId = request.getGameNo();

        try {
            log.info("请求:/price/legal/convert. request:{}", JSON.toJSON(request));
            GameResponse<List<PriceLegalConvertResponse>> response = gameServiceClient.priceLegalConvert(request.getApiKey(), getSign(request));
            log.info("返回:/price/legal/convert. data:{}", JSON.toJSON(response));
            isCheckSend(gameId, "/price/legal/convert", request, response);

            return response;
        } catch (Exception e) {
            log.error("数字货币兑换法币汇率查询异常. request:{}", JSON.toJSON(request), e);
            TelegramUtil.sendMsg(gameId, "game_api_util:price_legal_convert_origin", "数字货币兑换法币汇率查询异常");
        }

        return null;
    }


    /**
     * 数字货币兑换法币汇率查询
     *
     * @param request 请求参数
     * @return GameResponse
     */
    public static String rollBackDecr(RollBackDecrRequest request) {

        try {
            GameResponse<String> data = rollBackDecrOrigin(request);
            if (checkResponse(data)) {
                log.error("用户账户扣减回滚. 返回数据异常. data:{}, request:{}", JSON.toJSON(data), JSON.toJSON(request));
                return null;
            }

            return data.getData();
        } catch (Exception e) {
            log.error("用户账户扣减回滚异常. request:{}", JSON.toJSON(request), e);
        }

        return null;
    }

    /**
     * 用户账户扣减回滚
     *
     * @param request 请求参数
     * @return GameResponse
     */
    public static GameResponse<String> rollBackDecrOrigin(RollBackDecrRequest request) {
        String gameId = request.getGameNo();

        try {
            log.info("请求:/acct/rollBackDecr. request:{}", JSON.toJSON(request));
            GameResponse<String> response = gameServiceClient.rollBackDecr(request.getApiKey(), getSign(request));
            log.info("返回:/acct/rollBackDecr. data:{}", JSON.toJSON(response));
            isCheckSend(gameId, "/acct/rollBackDecr", request, response);

            return response;
        } catch (Exception e) {
            log.error("用户账户扣减回滚异常. request:{}", JSON.toJSON(request), e);
            TelegramUtil.sendMsg(gameId, "game_api_util:roll_back_decr_origin", "用户账户扣减回滚异常");
        }

        return null;
    }


    /**
     * 用户账户兑换（游戏充值）
     *
     * @param request 请求参数
     * @return GameResponse
     */
    public static String acctConvert(AcctConvertRequest request) {

        try {
            GameResponse<String> data = acctConvertOrigin(request);
            if (checkResponse(data)) {
                log.error("用户账户兑换（游戏充值）. 返回数据异常. data:{}, request:{}", JSON.toJSON(data), JSON.toJSON(request));
                return null;
            }

            return data.getData();
        } catch (Exception e) {
            log.error("用户账户兑换（游戏充值）. request:{}", JSON.toJSON(request), e);
        }

        return null;
    }

    /**
     * 用户账户兑换（游戏充值）
     *
     * @param request 请求参数
     * @return GameResponse
     */
    public static GameResponse<String> acctConvertOrigin(AcctConvertRequest request) {
        String gameId = request.getGameNo();

        try {
            log.info("请求:/game/acct/convert. request:{}", JSON.toJSON(request));
            GameResponse<String> response = gameServiceClient.acctConvert(request.getApiKey(), getSign(request));
            log.info("返回:/game/acct/convert. data:{}", JSON.toJSON(response));
            isCheckSend(gameId, "/game/acct/convert", request, response);

            return response;
        } catch (Exception e) {
            log.error("用户账户兑换（游戏充值）. request:{}", JSON.toJSON(request), e);
            TelegramUtil.sendMsg(gameId, "game_api_util:acct_convert_origin", "用户账户兑换（游戏充值）");
        }

        return null;
    }


    private static void isCheckSend(String gameId, String url, Object request, GameResponse<?> gameResponse) {
        String rspCode = gameResponse.getRspCode();
        if ("0000".equals(rspCode) || "6006".equals(rspCode) || "5001".equals(rspCode)) {
            return;
        }

        List<String> list = new ArrayList<>();
        list.add("接口地址：" + url);
        list.add("请求参数：" + JSON.toJSON(request));
        TelegramUtil.sendMsg(gameId, "game_api_util:is_check_send:" + url, JSON.toJSON(gameResponse).toString(), list);
    }
}
