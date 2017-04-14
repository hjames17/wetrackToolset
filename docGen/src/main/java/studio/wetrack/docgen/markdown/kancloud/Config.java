package studio.wetrack.docgen.markdown.kancloud;

import studio.wetrack.base.utils.jackson.Jackson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhanghong on 17/3/24.
 */
public class Config {

    List<String> plugins;
    Map<String, Object> pluginsConfig;


    public static Config DefaultApiConfig(String apiHost, Map<String, String> headers){
        Config config = new Config();
        config.setPlugins(Arrays.asList("api", "highlight"));

        config.pluginsConfig = new HashMap<>();

        Map<String, Object> apiConfig = new HashMap<>();
        apiConfig.put("url", apiHost);
        apiConfig.put("headers", headers);
        apiConfig.put("explore", true);
        config.pluginsConfig.put("api", apiConfig);

        return config;
    }

    public List<String> getPlugins() {
        return plugins;
    }

    public void setPlugins(List<String> plugins) {
        this.plugins = plugins;
    }

    public Map<String, Object> getPluginsConfig() {
        return pluginsConfig;
    }

    public void setPluginsConfig(Map<String, Object> pluginsConfig) {
        this.pluginsConfig = pluginsConfig;
    }

    public String toJson(){
        return Jackson.base().writeValueAsString(this);
    }

    public static void main(String[] args){
        System.out.println(Config.DefaultApiConfig("", null).toJson());
    }
}
