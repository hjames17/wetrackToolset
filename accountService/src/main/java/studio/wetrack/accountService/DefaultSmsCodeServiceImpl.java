package studio.wetrack.accountService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import studio.wetrack.base.exception.SmsCodeException;
import studio.wetrack.base.utils.sms.SmsService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhanghong on 17/2/27.
 */
public class DefaultSmsCodeServiceImpl implements SmsCodeService{

    static final long VALID_TIME = 5*60; //默认5分钟

    static final String PREFIX = "SMSCODE_";
    SmsService smsService;

    RedisTemplate<String, String> codeStoreTemplate;

    Map<CodeType, Long> validTimeMap = new HashMap<>();


    public DefaultSmsCodeServiceImpl(SmsService smsService, RedisTemplate<String, String> codeStoreTemplate){
        this.smsService = smsService;
        this.codeStoreTemplate = codeStoreTemplate;
        validTimeMap.put(CodeType.LOGIN, VALID_TIME);
        validTimeMap.put(CodeType.RESET_PASS, VALID_TIME);
        validTimeMap.put(CodeType.SIGNUP, VALID_TIME);
    }

    @Override
    public void requestSmsCode(String phone, CodeType type) throws SmsCodeException {
        BoundValueOperations<String, String> op = codeStoreTemplate.boundValueOps(PREFIX + type +"_" + phone);
        String found = op.get();

        String code = smsService.sendCode(phone, found);
        if(StringUtils.isEmpty(code)){
            throw new SmsCodeException("验证码获取失败");
        }

        if(found == null) {
            op.set(code);
            op.expire(validTimeMap.get(type), TimeUnit.SECONDS);
        }
    }

    @Override
    public boolean checkSmsCode(String phone, String code, CodeType type, boolean deleteAfterMatch) {
        BoundValueOperations<String, String> op = codeStoreTemplate.boundValueOps(PREFIX + type +"_" + phone);
        String found = op.get();
        if(found == null || !code.equals(found)){
            //no match
            return false;
        }else{
            if(deleteAfterMatch){
                removeSmsCode(phone, code, type);
            }
        }
        return true;
    }

    @Override
    public boolean removeSmsCode(String phone, String code, CodeType type) {
        codeStoreTemplate.delete(PREFIX + type +"_" + phone);
        return true;
    }

    @Override
    public long getCodeValidTimeForType(CodeType type) {
        return validTimeMap.get(type);
    }

    @Override
    public long setCodeValidTimeForType(CodeType type, long time) {
        return validTimeMap.put(type, time);
    }
}
