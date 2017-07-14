package studio.wetrack.messageService.support.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Created by zhanghong on 17/7/4.
 */
public class WebSocketInmemorySessionStore implements WebSocketSessionStore {

    static Logger log = LoggerFactory.getLogger(WebSocketInmemorySessionStore.class);

    static final String UN_GROUP_NAME = "_UN_GROUP_";

    private Map<String, List<WebSocketSession>> sessionsMapForId = new ConcurrentHashMap<>();
    private Map<String, List<String>> idListForGroup = new ConcurrentHashMap<>();

    @Override
    public void addGroup(String groupName) {

        List<String> ids = idListForGroup.get(groupName);
        if (ids == null) {
            ids = new CopyOnWriteArrayList<>();
            idListForGroup.put(groupName, ids);
        }
    }

    @Override
    public void put(List<String> groupName, String id, WebSocketSession session) {
        if(groupName == null || groupName.size() == 0){
            put((String)null, id, session);
        }
        for (String s : groupName) {
            put(s, id, session);
        }
    }

    @Override
    public void put(String groupName, String id, WebSocketSession session) {

        if(groupName == null){
            groupName = UN_GROUP_NAME;
        }

        //加入分组索引列表
        List<String> ids = idListForGroup.get(groupName);
        if (ids == null) {
            ids = new CopyOnWriteArrayList<>();
            idListForGroup.put(groupName, ids);
        }
        if(!ids.contains(id)){
            ids.add(id);
        }

        //加入session列表
        List<WebSocketSession> sessions = sessionsMapForId.get(id);
        if(sessions == null){
            sessions = new CopyOnWriteArrayList<>();
            sessionsMapForId.put(id, sessions);
        }
        if(!sessions.contains(session)){
            log.info("webSocketKey {};{}", groupName, id);
            sessions.add(session);
        }

        log.debug("webSocketKey {};{}", groupName, id);
    }

    @Override
    public void remove(String id) {

        //从分组中去除id索引
        for (List<String> ids : idListForGroup.values()) {
            if (ids != null) {
                ids.remove(id);
            }
        }
        //删除id下的session列表
        sessionsMapForId.remove(id);
    }

    @Override
    public void remove(String groupName, String id) {
        List<String> ids = idListForGroup.get(groupName);
        if(ids != null){
            ids.remove(id);
        }
    }

    @Override
    public List<WebSocketSession> getByGroup(String groupName) {

        List<String> ids = idListForGroup.get(groupName);

        //遍历分组中所有的用户，
        if(ids != null){
            return ids.stream().flatMap(id->{
                List<WebSocketSession> sessions = getById(id);
                if(sessions == null){
                    sessions = new CopyOnWriteArrayList<>();
                }
                return sessions.stream();
            }).collect(Collectors.toList());

        }

        return null;

    }

    @Override
    public List<WebSocketSession> getById(String id) {

        //取出所有的session，也顺便清除那些已经关闭的session
        List<WebSocketSession> sessions = sessionsMapForId.get(id);
        if (sessions != null) {
            List<WebSocketSession> closedList = new CopyOnWriteArrayList<>();
            for (WebSocketSession session : sessions) {
                if (!session.isOpen()) {
                    closedList.add(session);
                }
            }
            sessions.removeAll(closedList);

        }
        return sessions;

    }
}
