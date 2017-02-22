package studio.wetrack.messageService.support.jpush;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.common.resp.ResponseWrapper;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.PushPayload.Builder;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.wetrack.messageService.messages.JPushMessage;

import java.util.ArrayList;
import java.util.List;

public class JPusher{

	static Logger log = LoggerFactory.getLogger(JPusher.class);

	boolean dev;

	JPushClient jpushClient;

	public JPusher(JPushClient jpushClient, boolean dev){
		this.jpushClient = jpushClient;
		this.dev = dev;
	}

	
	private void sendAMessage(JPushMessage jpm, PushPayload np){
		oneTry(jpushClient, jpm, np, 0);
	}
	
	//TODO 接口调用频率超出限制的时候，把消息延后发送
	private void oneTry(JPushClient jc, JPushMessage jpm, PushPayload ppl, int count){

        try {
        	PushResult msgResult = jc.sendPush(ppl);//sendNotification(jpm.getContent(), ppl);
        	//log.info("jpush server return: " + msgResult);
        	if (msgResult != null) {
    			
    			if (msgResult.isResultOK()) {
    				log.info("success， sendNo=" + msgResult.sendno);
    			} else {
    				ResponseWrapper rw = new ResponseWrapper();
    				msgResult.setResponseWrapper(rw);
    				
    				log.info("jpush fail, sendno " + jpm.getId() + ", error code=" + rw.error.error.code
    						+ ", error msg=" + rw.error.error.message);
//    				if(msgResult.getErrorCode() != 1011){//1011是找不到别名
//    				}
    			}
    		} else {
    			log.warn("jpush return result is null, sendno  " + jpm.getId());
    		}
            
        } catch (APIConnectionException e) {
            log.warn("jpush sendno  " + jpm.getId() + ", Connection error. Should retry later. ", e);
            
        } catch (APIRequestException e) {
            log.warn("jpush error, sendno " + e.getMsgId() + ", errorCode " + e.getErrorCode() + ",Error Message: " + e.getErrorMessage());
        }
		
	}

	
	public boolean pushEvent(JPushMessage jpm) {

		Audience audience = null;
		// FIXME 线上环境把apnsProduction(true)
		Options.Builder optionBuilder = Options.newBuilder()
											.setApnsProduction(dev)
											.setSendno(jpm.getId());
		JsonObject jsonObject = new JsonObject();
		for(String key : jpm.getExtras().keySet()){
			jsonObject.addProperty(key, jpm.getExtras().get(key));
		}
		Builder builder = PushPayload.newBuilder()
									.setPlatform(Platform.android_ios())
									.setOptions(optionBuilder.build())
									.setNotification(Notification
											.newBuilder()
											.addPlatformNotification(
													IosNotification
															.newBuilder()
//																	.addExtra("sound", "alert.caf")
															.disableBadge()
//															.setSound("alert.caf")
//															.addExtras(jpm.getExtras())
															.addExtra("extras", jsonObject)
															.build())
											.addPlatformNotification(
													AndroidNotification
															.newBuilder()
															.addExtras(jpm.getExtras())
															.setTitle(jpm.getTitle()).build())
											.setAlert(jpm.getContent())
											.build());

		if(jpm.getAliasList() != null){
			audience = Audience.alias(jpm.getAliasList());
			log.info("sending jpush to alias " + jpm.getAliasList());
			sendAMessage(jpm, builder.setAudience(audience).build());
		}
		if(jpm.getTagList() != null){
			audience = Audience.tag(jpm.getTagList());
			log.info("sending jpush to tags " + jpm.getTagList());
			sendAMessage(jpm, builder.setAudience(audience).build());
		}

		
		
		return false;
	}
	
	public static void main(String[] args) {
		List<String> ss = new ArrayList<String>();
		ss.add("a");
		ss.add("b");
		System.out.println("print arrayList " + ss);
	}

}
