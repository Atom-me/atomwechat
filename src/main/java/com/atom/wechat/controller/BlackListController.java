package com.atom.wechat.controller;

import com.atom.wechat.helper.AccessTokenHelper;
import com.atom.wechat.model.blacklist.BlackList;
import com.atom.wechat.model.fans.Subscriber;
import com.atom.wechat.model.resp.AccessTokenResp;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.atom.wechat.constant.WeChatConstant.*;

/**
 * 黑名单管理
 *
 * @author Atom
 */
@RestController
@Slf4j
public class BlackListController {

    @Resource
    private AccessTokenHelper accessTokenHelper;

    @Resource
    private RestTemplate restTemplate;

    /**
     * 获取公众号的黑名单列表
     *
     * @return
     */
    @RequestMapping("getBlackList")
    public ResponseEntity<List<Subscriber>> getBlackList() {
        AccessTokenResp accessTokenResp = accessTokenHelper.fetchAccessToken();
        String getBlackListUrl = GET_BLACK_LIST_URL.replace("ACCESS_TOKEN", accessTokenResp.getAccessToken());
        //当 begin_openid 为空时，默认从开头拉取。
        Map<String, String> param = new HashMap<>(1);
        param.put("begin_openid", "");
        BlackList blackList = restTemplate.postForObject(getBlackListUrl, param, BlackList.class);
        log.info("==================================");
        log.info(blackList.toString());
        List<Subscriber> weChatUserInfoList = Lists.newArrayList();
        if (blackList.getCount() <= 0) {
            return ResponseEntity.ok(weChatUserInfoList);
        }
        blackList.getData().getOpenId().forEach(openId -> {
            Subscriber subscriber = restTemplate.getForObject(FANS_INFO_URL, Subscriber.class, accessTokenHelper.fetchAccessToken().getAccessToken(), openId);
            weChatUserInfoList.add(subscriber);
        });
        return ResponseEntity.ok(weChatUserInfoList);
    }

    /**
     * 拉黑用户
     *
     * @param openIds
     * @return
     */
    @PostMapping("batchblacklist")
    public ResponseEntity<String> batchBlackList(@RequestBody List<String> openIds) {
        AccessTokenResp accessTokenResp = accessTokenHelper.fetchAccessToken();
        String getBlackListUrl = BATCH_BLACK_LIST_URL.replace("ACCESS_TOKEN", accessTokenResp.getAccessToken());
        //当 begin_openid 为空时，默认从开头拉取。
        Map<String, List<String>> param = new HashMap<>(1);
        param.put("openid_list", openIds);
        String responseStr = restTemplate.postForObject(getBlackListUrl, param, String.class);
        return ResponseEntity.ok(responseStr);
    }

    /**
     * 取消拉黑用户
     * 请求参数数组形式
     * ["oSu9iw3RE6e-YnpYI8ckm12Hx8LE"]
     *
     * @param openIds
     * @return
     */
    @PostMapping(value = "batchunblacklist")
    public ResponseEntity<String> batchUnBlackList(@RequestBody List<String> openIds) {
        AccessTokenResp accessTokenResp = accessTokenHelper.fetchAccessToken();
        String getBlackListUrl = BATCH_UN_BLACK_LIST_URL.replace("ACCESS_TOKEN", accessTokenResp.getAccessToken());
        //当 begin_openid 为空时，默认从开头拉取。
        Map<String, List<String>> param = new HashMap<>(1);
        param.put("openid_list", openIds);
        String responseStr = restTemplate.postForObject(getBlackListUrl, param, String.class);
        return ResponseEntity.ok(responseStr);
    }
}
