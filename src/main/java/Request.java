import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.nio.charset.Charset;
import java.util.List;

public class Request {

    private final String method;
    private final String path;
    private final String protocolVersion;
    private final List<String> headers;
    private final byte[] body;
    private List<NameValuePair> dataQueryStr;
    private final String queryParams;

    String textToOut = null;

    public Request(String method, String path, String protocolVersion, List<String> headers, byte[] body, String queryParams) {
        this.method = method;
        this.path = path;
        this.protocolVersion = protocolVersion;
        this.headers = headers;
        this.body = body;
        this.queryParams = queryParams;
    }

    public String getMethod() {
        return method;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    public List<NameValuePair> getDataQueryStr() {
        dataQueryStr = URLEncodedUtils.parse(queryParams, Charset.defaultCharset());
        if (dataQueryStr.isEmpty()) return null;
        return dataQueryStr;
    }

    public String getPath() {
        if (!path.contains("?")) {
            return path;
        }
        int beginQueryStr = path.indexOf('?');
        String clearPath = path.substring(0, beginQueryStr);
        return clearPath;
    }


    public String getQueryParam(String name) {

        if (queryParams == null) {
            return null;
        }
        for (NameValuePair value : getDataQueryStr()) {
            if (name.equals(value.getName())) {
                return value.getValue();
            }
        }
        return "null";
    }

    public List<NameValuePair> getQueryParams() {
        if (getDataQueryStr() == null) return null;
        return getDataQueryStr();
    }

    public String showQueryToScreen(List<NameValuePair> dataQueryStr) {

        if (dataQueryStr == null) return "-->Not found query<---";

        for (NameValuePair params : dataQueryStr) {
            if (textToOut == null) {
                textToOut = params.getName() + " = " + params.getValue();
            } else {
                textToOut = textToOut + " " + params.getName() + " = " + params.getValue();
            }
        }
        return textToOut;
    }

}
