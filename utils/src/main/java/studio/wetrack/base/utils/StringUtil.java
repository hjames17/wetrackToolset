package studio.wetrack.base.utils;

/**
 * Created by zhanghong on 17/3/3.
 */
public class StringUtil {

    public static String firstLetterLowerCase(String text){
        if(text == null || text.length() == 0){
            return text;
        }

        String output  = Character.toLowerCase(text.charAt(0)) +
                (text.length() > 1 ? text.substring(1) : "");

        return output;
    }

}
