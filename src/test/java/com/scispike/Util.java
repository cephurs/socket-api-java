package com.scispike;

import java.io.IOException;
import java.net.URLDecoder;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

public class Util {

  public static final String HTTP_HOST = "http://localhost:3000";
  
  public static Socket getSocket(){
    return getSocket(null);
  }
  public static Socket getSocket(Callback<String, String> connected) {
    Socket s = new Socket(Util.HTTP_HOST, Util.getAuth(), connected);
    return s;
  }
  public static AuthFunction getAuth() {
    return new AuthFunction() {

      @Override
      void auth(Callback<String, String> cb) {
        HttpClient client = new HttpClient();
        HttpMethod m = new GetMethod(HTTP_HOST);
        try {
          int r = client.executeMethod(m);
          Header[] hs= m.getResponseHeaders();
          Header c = m.getResponseHeader("set-cookie");
          String v = c.getValue();
          String[] split = v.split(";");
          String sessionId = URLDecoder.decode(split[0].split("=")[1]).replaceAll("s:([^\\.]*).*", "$1");;
          cb.call(null, "token", sessionId);
        } catch (HttpException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        
      }
    };
  }
}