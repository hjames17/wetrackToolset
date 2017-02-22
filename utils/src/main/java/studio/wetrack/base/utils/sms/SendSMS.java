package studio.wetrack.base.utils.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendSMS {
	private final static Logger LOGGER = LoggerFactory.getLogger(SendSMS.class);
	private final static String URL = "http://121.199.48.186:1210/Services/MsgSend.asmx/SendMsg";
	// fixme 设置用户名密码
	private final static String USER_CODE = "";
	private final static String USER_PASS = "";
	private final static String CHANNEL = "0";
	private final static String CHAR_SET = "UTF-8";

	public boolean send(String mob, String msg) {
		String str;
		try {
			// 添加所需要的post内容
//			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//			nvps.add(new BasicNameValuePair("userCode",USER_CODE));
//			nvps.add(new BasicNameValuePair("userPass", USER_PASS));
//			nvps.add(new BasicNameValuePair("DesNo", mob));
//			nvps.add(new BasicNameValuePair("Msg", msg));
//			nvps.add(new BasicNameValuePair("Channel", CHANNEL));
//			str = Utils.get(HttpExecutor.class).post(URL).setEntity(new UrlEncodedFormEntity(nvps,CHAR_SET)).executeAsString();
//			LOGGER.info("云信触发短信结果:{}",str);
//			Document doc = null;
//			doc = DocumentHelper.parseText(str); // 将字符串转为XML
//			if (doc == null)
//				return false;
//			Element rootElt = doc.getRootElement(); // 获取根节点
//			if (rootElt == null)
//				return false;
//			// 拿到根节点的名称
//			if (rootElt.getText() == null || "".equals(rootElt.getText()))
//				return false;
//			if (Long.parseLong(rootElt.getText()) > 0) {
//				return true;
//			} else {
//				return false;
//			}
			return true;
//		} catch (DocumentException e) {
//			e.printStackTrace();
//			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
