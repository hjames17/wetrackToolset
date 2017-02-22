package studio.wetrack.messageService.messages;


import studio.wetrack.messageService.base.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JPushMessage implements Message {
	private String title;
	private String content;
	private int id;
	private List<String> aliasList;
	private List<String> tagList;
	private Map<String, String> extras;
	
	public JPushMessage(){
		super();
		//ios的通知声音需要指定，否则ios的推送发不出声音
		extras = new HashMap<String, String>();
//		Map<String, String> ios = new HashMap<String, String>();
//		ios.put("sound", "alert.caf");
//		extras.put("ios", ios);
	}
	
	public void addAlias(String alias){
		if(aliasList == null){
			aliasList = new ArrayList<String>();
		}
		
		if(!aliasList.contains(alias)){
			aliasList.add(alias);
		}
	}
	
	public void addTag(String tag){
		if(tagList == null){
			tagList = new ArrayList<String>();
		}
		
		if(!tagList.contains(tag)){
			tagList.add(tag);
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<String> getAliasList() {
		return aliasList;
	}

	public void setAliasList(List<String> aliasList) {
		this.aliasList = aliasList;
	}

	public List<String> getTagList() {
		return tagList;
	}

	public void setTagList(List<String> tagList) {
		this.tagList = tagList;
	}

	public Map<String, String> getExtras() {
		return extras;
	}

	public void setExtras(Map<String, String> extras) {
		this.extras = extras;
	}
}
