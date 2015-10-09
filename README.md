# UrlDownloader
An android basic HTTP requests class



```
new UrlDownloader(UrlDownloader.RequestType.POST).
                    setOnReceiveListener(new UrlDownloader.RecieveListener() {
                        @Override
                        public void onReceive(String content) {
                                Log.d("UrlDonloader", content);
                    })
                    .addPostData("key", "value")
                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                            "http://example.com/post_endpoint");
 
new UrlDownloader(UrlDownloader.RequestType.GET)
                .setOnReceiveListener(new UrlDownloader.RecieveListener() {
                                @Override
                                public void onReceive(String content) {
                                        Log.d("UrlDonloader", content);
                                })
                .setOnErrorListener(new UrlDownloader.ErrorListener() {
                        public void onError(Exception e) {
                                Log.e("UrlDownloader", "Error cought");
                        }
                });
                 .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                            "http://example.com/get_endpoint?key=value");
  ```
