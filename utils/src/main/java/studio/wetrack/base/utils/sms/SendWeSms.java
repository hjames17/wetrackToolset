package studio.wetrack.base.utils.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhangsong on 16/1/14.
 */
public class SendWeSms {
	private final static Logger LOGGER = LoggerFactory.getLogger(SendWeSms.class);
	private final static String URL = "http://www.106551.com/ws/Send.aspx";
	private final static String Mobile = "Mobile";
	private final static String Content = "Content";


	String corpId ;//= "YX03439";
	String pwd ;//= "kingair";
	public SendWeSms(String corpId, String pwd){
		this.corpId = corpId;
		this.pwd = pwd;
	}

	public boolean send(String phone, String content) {
//		String result = Utils.get(HttpExecutor.class).post(URL).addFormParam("CorpID", corpId).addFormParam("Pwd", pwd)
//		     .addFormParam(Mobile,phone).addFormParam(Content, content).executeAsString();
//		LOGGER.info("发送短信手机号:{},内容:{},结果:{}",phone,content,result);
		return true;
	}
}
