package com.linewell.utils;

import android.test.AndroidTestCase;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import junit.framework.Assert;

import org.json.JSONObject;

/**
 * Created by aaron on 2016-07-15.
 */
public class HttpTest extends AndroidTestCase {
    private static final String PATH = "https://m.innochina.com/common";

    public void testGet() throws InterruptedException {
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET,"https://m.innochina.com/common/testConnection",
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Assert.assertNotNull(jsonObject);
                        Assert.assertEquals(jsonObject, "GET_SUCCESS");
                        Log.e("volley",jsonObject.toString());
                    }
                },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    System.out.println("erro");
                    Log.e("volleyerror","erro");
                }
        });
        //将StringRequest对象添加进RequestQueue请求队列中
        // VolleySingleton.getVolleySingleton(this.getApplicationContext()).addToRequestQueue(stringRequest);

    }

//    public void testGet() throws Exception {
//        HttpClient client = new DefaultHttpClient();
//        HttpGet get = new HttpGet(PATH + "/TestServlet?id=1001&name=john&age=60");
//        HttpResponse response = client.execute(get);
//        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//            InputStream is = response.getEntity().getContent();
//            String result = inStream2String(is);
//            Assert.assertEquals(result, "GET_SUCCESS");
//        }
//    }
//
//    //将输入流转换成字符串
//    private String inStream2String(InputStream is) throws Exception {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        byte[] buf = new byte[1024];
//        int len = -1;
//        while ((len = is.read(buf)) != -1) {
//            baos.write(buf, 0, len);
//        }
//        return new String(baos.toByteArray());
//    }

}
