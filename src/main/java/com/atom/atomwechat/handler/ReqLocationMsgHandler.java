package com.atom.atomwechat.handler;

import com.atom.atomwechat.annotation.MsgType;
import com.atom.atomwechat.enums.ReqMsgTypeEnum;
import com.atom.atomwechat.model.req.ReqLocationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author atom
 */
@MsgType(type = ReqMsgTypeEnum.LOCATION, msgClass = ReqLocationMessage.class)
@Component
@Slf4j
public class ReqLocationMsgHandler implements ReqMsgHandler<ReqLocationMessage> {
    @Override
    public String process(ReqLocationMessage reqLocationMessage) {
        log.error("===========地理位置消息=============");
        log.info(reqLocationMessage.toString());

        return null;
    }

    @Override
    public ReqMsgTypeEnum getMsgType() {
        return ReqMsgTypeEnum.LOCATION;
    }
}