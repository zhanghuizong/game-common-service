package com.bitgame.game.framework.feign;

import com.bitgame.game.framework.vo.feign.request.SignRequest;
import com.bitgame.game.framework.vo.feign.response.BalanceResponse;
import com.bitgame.game.framework.vo.feign.response.CurrencyResponse;
import com.bitgame.game.framework.vo.feign.response.GameResponse;
import com.bitgame.game.framework.vo.feign.response.PriceLegalConvertResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.Map;

/**
 * 游戏平台账户对接
 */
@FeignClient(name = "game-service", path = "/game")
public interface GameServiceClient {

    /**
     * 查询货币配置列表
     *
     * @param apiKey      秘钥
     * @param signRequest 请求参数
     * @return GameResponse
     */
    @PostMapping("/currency/getCurrencyList")
    GameResponse<List<CurrencyResponse>> getCurrencyList(
            @RequestHeader("apiKey") String apiKey,
            @RequestBody SignRequest signRequest
    );

    /**
     * 批量查询用户账户余额
     *
     * @param apiKey      秘钥
     * @param signRequest 请求参数
     * @return GameResponse
     */
    @PostMapping("/acct/getBalanceList")
    GameResponse<Map<String, BalanceResponse>> getBalanceList(
            @RequestHeader("apiKey") String apiKey,
            @RequestBody SignRequest signRequest
    );

    /**
     * 单币中用户账户余额查询
     *
     * @param apiKey      秘钥
     * @param signRequest 请求参数
     * @return GameResponse
     */
    @PostMapping("/acct/getBalance")
    GameResponse<BalanceResponse> getBalance(
            @RequestHeader("apiKey") String apiKey,
            @RequestBody SignRequest signRequest
    );

    /**
     * 用户账户扣减
     *
     * @param apiKey      秘钥
     * @param signRequest 请求参数
     * @return GameResponse
     */
    @PostMapping("/acct/deduct")
    GameResponse<String> deduct(
            @RequestHeader("apiKey") String apiKey,
            @RequestBody SignRequest signRequest
    );

    /**
     * 用户账户增加
     *
     * @param apiKey      秘钥
     * @param signRequest 请求参数
     * @return GameResponse
     */
    @PostMapping("/acct/increase")
    GameResponse<String> increase(
            @RequestHeader("apiKey") String apiKey,
            @RequestBody SignRequest signRequest
    );

    /**
     * 数字货币兑换法币汇率查询
     *
     * @param apiKey      秘钥
     * @param signRequest 请求参数
     * @return GameResponse
     */
    @PostMapping("/price/legal/convert")
    GameResponse<List<PriceLegalConvertResponse>> priceLegalConvert(
            @RequestHeader("apiKey") String apiKey,
            @RequestBody SignRequest signRequest
    );


    /**
     * 用户账户扣减回滚
     *
     * @param apiKey      秘钥
     * @param signRequest 请求参数
     * @return GameResponse
     */
    @PostMapping("/acct/rollBackDecr")
    GameResponse<String> rollBackDecr(
            @RequestHeader("apiKey") String apiKey,
            @RequestBody SignRequest signRequest
    );

    /**
     * 用户账户兑换（游戏充值）
     *
     * @param apiKey      秘钥
     * @param signRequest 请求参数
     * @return GameResponse
     */
    @PostMapping("/acct/convert")
    GameResponse<String> acctConvert(
            @RequestHeader("apiKey") String apiKey,
            @RequestBody SignRequest signRequest
    );
}
