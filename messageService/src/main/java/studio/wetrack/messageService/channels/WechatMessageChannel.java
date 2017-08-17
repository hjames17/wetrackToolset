package studio.wetrack.messageService.channels;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpCustomMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.wetrack.base.utils.jackson.Jackson;
import studio.wetrack.messageService.base.AbstractMessageChannel;
import studio.wetrack.messageService.base.Message;
import studio.wetrack.messageService.messages.WechatMessage;

/**
 * Created by zhanghong on 16/3/1.
 */
public class WechatMessageChannel extends AbstractMessageChannel {
	private Logger LOGGER = LoggerFactory.getLogger(WechatMessageChannel.class);

	public WechatMessageChannel(WxMpService wxMpService) {
		this.wxMpService = wxMpService;
	}

	WxMpService wxMpService;

	@Override
	protected void doSend(Message message) {
		WechatMessage wechatMessage = (WechatMessage) message;
		WxMpCustomMessage.WxArticle article1 = new WxMpCustomMessage.WxArticle();
		article1.setUrl(wechatMessage.getUrl());
		article1.setPicUrl(wechatMessage.getPicUrl());
		article1.setDescription(wechatMessage.getContent());
		article1.setTitle(wechatMessage.getTitle());
		WxMpCustomMessage wxMpCustomMessage = WxMpCustomMessage.NEWS()
		                                                       .toUser(wechatMessage.getReceiver())
		                                                       .addArticle(article1)
		                                                       .build();
		LOGGER.info("发送微信通知,openId:{},内容:{}", wechatMessage.getReceiver(),
				Jackson.mobile().writeValueAsString(wxMpCustomMessage));
		try {
			wxMpService.customMessageSend(wxMpCustomMessage);
		} catch (WxErrorException e) {
			LOGGER.error("发送微信通知失败,openId:{},内容:{}, 原因:{}", wechatMessage.getReceiver(),
					Jackson.mobile().writeValueAsString(wxMpCustomMessage), e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return "wechat";
	}
}
