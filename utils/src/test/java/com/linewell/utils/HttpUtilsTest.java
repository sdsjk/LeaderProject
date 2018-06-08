//package com.linewell.utils;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.util.Log;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.ExecutorDelivery;
//import com.android.volley.Network;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.ResponseDelivery;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.BasicNetwork;
//import com.android.volley.toolbox.HttpStack;
//import com.android.volley.toolbox.HurlStack;
//import com.android.volley.toolbox.NoCache;
//import com.android.volley.toolbox.StringRequest;
//import com.google.gson.JsonObject;
//
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpUriRequest;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.robolectric.RuntimeEnvironment;
//import org.robolectric.annotation.Config;
//import org.robolectric.shadows.httpclient.FakeHttp;
//
//import java.io.File;
//import java.io.IOException;
//import java.net.URI;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
//import static org.hamcrest.CoreMatchers.equalTo;
//import static org.junit.Assert.assertThat;
//
//
///**
// * Created by aaron on 2016-07-06.
// */
//@RunWith(TestRunnerWithManifest.class)
//@Config(constants = BuildConfig.class, sdk = 23)
//public class HttpUtilsTest{
//
//    private final static String TAG = "http";
//
//    /**
//     * 获取Json对象
//     * @throws InterruptedException
//     */
//    @Test
//    public void testGetJson() throws InterruptedException{
//        final CountDownLatch signal = new CountDownLatch(1);
//        Context context = RuntimeEnvironment.application.getApplicationContext();
//        FakeX509TrustManager.allowAllSSL();
//        VolleySingleton
//                .getVolleySingleton(context)
//                .fakeRequestQueue(this.getTestQequestQueue(context));
//        HttpUtils.getJson(context, "https://m.innochina.com/common/testConnection",
//                new ListenerHandler<JsonObject>(){
//
//            @Override
//            public void onResponse(JsonObject jsonObject) {
//                System.out.println(GsonUtil.getJsonStr(jsonObject));
//                Assert.assertNotNull(jsonObject);
//                Assert.assertEquals(jsonObject.get("status").getAsInt(), 1);
//                signal.countDown();
//            }
//
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                System.out.println(volleyError.getMessage());
//                signal.countDown();
//                Assert.fail();
//            }
//        });
//        signal.await(30, TimeUnit.SECONDS);
//    }
//
//    /**
//     * 测试下载文件
//     * @throws InterruptedException
//     */
//    @Test
//    public void testUpload() throws InterruptedException{
//
//        final CountDownLatch signal = new CountDownLatch(1);
//        Context context = RuntimeEnvironment.application.getApplicationContext();
//        FakeX509TrustManager.allowAllSSL();
//        VolleySingleton
//                .getVolleySingleton(context)
//                .fakeRequestQueue(this.getTestQequestQueue(context));
//        Map<String,String> formMap = new HashMap<>();
//        formMap.put("id",GsonUtil.getJsonStr(new String[]{"1"}));
//        formMap.put("name",GsonUtil.getJsonStr(new String[]{"tmpcrowdsource.log"}));
//        formMap.put("fieldName",GsonUtil.getJsonStr(new String[]{"1"}));
//        Map<String, File> map = new HashMap<>();
//        map.put("_file_1",new File("d:/tmpcrowdsource.log"));
//        HttpUtils.uploadFiles(context, "http://192.168.131.202:8080/mobile/common/upload",formMap,map,null,
//      //  HttpUtils.getJson(context, "https://m.innochina.com/common/upload",
//                new ListenerHandler<JsonObject>(){
//
//                    @Override
//                    public void onResponse(JsonObject jsonObject) {
//                        System.out.println(GsonUtil.getJsonStr(jsonObject));
//                        Assert.assertNotNull(jsonObject);
//                        Assert.assertEquals(jsonObject.get("status").getAsInt(), 1);
//                        signal.countDown();
//                    }
//
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                       volleyError.printStackTrace();
//                        signal.countDown();
//                        Assert.fail();
//                    }
//                });
//        signal.await(30, TimeUnit.SECONDS);
//    }
//
//    @Test
//    public void testDownFile() throws InterruptedException{
//        final CountDownLatch signal = new CountDownLatch(1);
//        Context context = RuntimeEnvironment.application.getApplicationContext();
//        FakeX509TrustManager.allowAllSSL();
//        VolleySingleton
//                .getVolleySingleton(context)
//                .fakeRequestQueue(this.getTestQequestQueue(context));
//        HttpUtils.getImageRequest(context, "https://s.innochina.com/common/images/logo.png", 0, 0, Bitmap.Config.RGB_565, new ListenerHandler<Bitmap>() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Assert.fail();
//                signal.countDown();
//            }
//
//            @Override
//            public void onResponse(Bitmap response) {
//                Assert.assertNotNull(response);
//                signal.countDown();
//            }
//        });
//        signal.await(30, TimeUnit.SECONDS);
//
//        // down.file是保存的文件名，这个文件只在下载成功后才存在，在下载过程中，
//// Netroid会在文件路径下创建一个临时文件，命名为：down.file.tmp，下载成功后更名为down.file。
////        FileDownloader.DownloadController controller = Netroid.getNetroid(context).addFileDownload(
////                "/sdcard/netroid/down.file", "http://server.com/res/down.file",
////                new Listener<Void>() {
////                    // 注：如果暂停或放弃了该任务，onFinish()不会回调
////                    @Override
////                    public void onFinish() {
////                        Toast.makeText("下载完成").show();
////                    }
////
////                    // 注：如果暂停或放弃了该任务，onSuccess()不会回调
////                    @Override
////                    public void onSuccess(Void response) {
////                        Toast.makeText("下载成功").show();
////                    }
////
////                    // 注：如果暂停或放弃了该任务，onError()不会回调
////                    @Override
////                    public void onError(NetroidError error) {
////                        Toast.makeText("下载失败").show();
////                    }
////
////                    // Listener添加了这个回调方法专门用于获取进度
////                    @Override
////                    public void onProgressChange(long fileSize, long downloadedSize) {
////                        // 注：downloadedSize 有可能大于 fileSize，具体原因见下面的描述
////                        Toast.makeText("下载进度：" + (downloadedSize * 1.0f / fileSize * 100) + "%").show();
////                    }
////                });
//
//// 查看该任务的状态
//      //  controller.getState();
//// 任务的状态分别是：
//       // STATUS_WAITING：         // 等待中
//      //  STATUS_DOWNLOADING：     // 下载中
//      //  STATUS_PAUSE：           // 已暂停
//      //  STATUS_SUCCESS：         // 已成功（标识下载已经正常完成并成功）
//      //  STATUS_DISCARD：         // 已取消（放弃）
//
//// 暂停该任务
//       // controller.pause();
//
//// 继续该任务
//       // controller.resume();
//
//// 放弃(删除)该任务
//      //  controller.discard();
//    }
//
//    /**
//     * 测试字符串请求
//     * @throws InterruptedException
//     */
//    @Test
//    public void testGetStringRequest() throws InterruptedException {
//
//        final CountDownLatch signal = new CountDownLatch(1);
//        Context context = RuntimeEnvironment.application.getApplicationContext();
//        FakeX509TrustManager.allowAllSSL();
//        VolleySingleton
//                .getVolleySingleton(context)
//                .fakeRequestQueue(this.getTestQequestQueue(context));
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://www.baidu.com", new Response.Listener<String>(){
//
//            @Override
//            public void onResponse(String s) {
//                Log.e("volley",s);
//                signal.countDown();
//            }
//        },new Response.ErrorListener(){
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                Log.e("volleyerror","erro2");
//                signal.countDown();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("params1", "value1");
//                map.put("params2", "value2");
//                return map;
//            }
//        };
//
//        //将StringRequest对象添加进RequestQueue请求队列中
//        // VolleySingleton.getVolleySingleton(this.getApplicationContext()).addToRequestQueue(stringRequest);
//
//    }
//
//    @Test
//    public void testPostString() throws InterruptedException {
//
//        final CountDownLatch signal = new CountDownLatch(1);
//        Context context = RuntimeEnvironment.application.getApplicationContext();
//        FakeX509TrustManager.allowAllSSL();
//        VolleySingleton
//                .getVolleySingleton(context)
//                .fakeRequestQueue(this.getTestQequestQueue(context));
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://www.baidu.com", new Response.Listener<String>(){
//
//            @Override
//            public void onResponse(String s) {
//                //打印请求返回结果
//                Log.e("volley",s);
//                signal.countDown();
//            }
//        },new Response.ErrorListener(){
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                Log.e("volleyerror","erro2");
//                signal.countDown();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("params1", "value1");
//                map.put("params2", "value2");
//                return map;
//
//
//            }
//        };
//
//        //将StringRequest对象添加进RequestQueue请求队列中
//       // VolleySingleton.getVolleySingleton(this.getApplicationContext()).addToRequestQueue(stringRequest);
//
//    }
//
//    @Test
//    public void testExecute() throws IOException {
//        FakeHttp.addPendingHttpResponse(200, "a happy response body");
//        FakeHttp.addPendingHttpResponse(200, "a happy response body");
//        FakeHttp.addPendingHttpResponse(200, "a happy response body");
//
//        DefaultHttpClient client = new DefaultHttpClient();
//        client.execute(new HttpGet("http://www.first.org"));
//        client.execute(new HttpGet("http://www.second.org"));
//        client.execute(new HttpGet("http://www.third.org"));
//
//        assertThat(((HttpUriRequest) FakeHttp.getLatestSentHttpRequest()).getURI(), equalTo(URI.create("http://www.third.org")));
//    }
//
//    /**
//     * 获取测试的内容
//     * @param context
//     * @return
//     */
//    private RequestQueue getTestQequestQueue(Context context){
//        HttpStack stack = new HurlStack();
//        Network network = new BasicNetwork(stack);
//        ResponseDelivery responseDelivery = new ExecutorDelivery(Executors.newSingleThreadExecutor());
//        RequestQueue queue = new RequestQueue(new NoCache(), network, 4, responseDelivery);
//        queue.start();
//        return queue;
//    }
//}
