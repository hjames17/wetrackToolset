package studio.wetrack.base.utils;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by chen on 16/10/5.
 */
public class HttpUtil {

    public static String get(String url, String charset) throws IOException {
        OutputStream output = null;
        InputStream input = null;
        URLConnection connection = new URL(url).openConnection();
        connection.setConnectTimeout(30000);
        connection.setReadTimeout(30000);
        try {
            input = connection.getInputStream();
            return IOUtils.toString(input, charset);
        }finally {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(output);
        }

    }
}
