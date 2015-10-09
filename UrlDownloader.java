package assets;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class UrlDownloader extends AsyncTask<String, Void, String> {

    public static final String TAG = "assets.UrlDownloader";

    public enum RequestType {
        GET, POST;
    }

    private RequestType type;

    private RecieveListener onReceive;
    private RequestListener requestListener;
    private ErrorListener onErrorListener;
    private List<NameValuePair> postData;

    public UrlDownloader(RequestType type) {
        this.type = type;
        this.postData = new ArrayList<NameValuePair>();

        this.onReceive = null;
        this.requestListener = null;
        this.onErrorListener = null;
    }


    @Deprecated
    public UrlDownloader addPostData(BasicNameValuePair data) {
        // Add your data
        postData.add(data);
        return this;
    }

    public UrlDownloader addPostData(String name, String value) {
        postData.add(new BasicNameValuePair(name, value));
        return this;
    }

    public UrlDownloader addAllPostData(List<NameValuePair> data) {
        postData.addAll(data);
        return this;
    }

    @Override
    protected String doInBackground(String... params) {
        HttpParams httpParams = new BasicHttpParams();
        httpParams.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        httpParams.setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 5000);
        httpParams.setParameter(HttpConnectionParams.SO_TIMEOUT, 6000);
        HttpConnectionParams.setTcpNoDelay(httpParams, true);


        HttpClient httpClient = new DefaultHttpClient(httpParams);
        HttpUriRequest request;
        HttpResponse response;


        Log.d("UrlDownloader", "Reuqested URL is " + params[0]);

        if (type == RequestType.GET) {
            request = new HttpGet(params[0]);
        } else {
            request = new HttpPost(params[0]);
            try {
                ((HttpPost) request).setEntity(new UrlEncodedFormEntity(postData, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "Error in encdoing, POST request");
            }
        }

        if(this.requestListener != null)
            this.requestListener.setRequest(request);

        try {
            response = httpClient.execute(request);
        } catch (Exception e) {
            if(this.onErrorListener != null)
                this.onErrorListener.onError(e);
            return null;
        }

        try {
            String content = EntityUtils.toString(response.getEntity());

            Log.d("UrlDownloader", "Requested URL " + params[0] + " downloaded.");

            if(this.onReceive != null)
                this.onReceive.onReceive(content);

            return null;
        } catch (Exception e) {
            if(this.onErrorListener != null)
                this.onErrorListener.onError(e);

            return null;
        }
    }

    public UrlDownloader setOnReceiveListener(RecieveListener onReceive) {
        this.onReceive = onReceive;
        return this;
    }

    public UrlDownloader setRequestListener(RequestListener requestListener) {
        this.requestListener = requestListener;
        return this;
    }

    public UrlDownloader setOnErrorListener(ErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
        return this;
    }

    @Override
    protected void onPostExecute(String response) {

    }

    public interface RecieveListener {
        public abstract void onReceive(String content);
    }

    public interface ErrorListener {
        public abstract void onError(Exception e);
    }

    public interface RequestListener {
        public abstract void setRequest(HttpUriRequest request);
    }
}
