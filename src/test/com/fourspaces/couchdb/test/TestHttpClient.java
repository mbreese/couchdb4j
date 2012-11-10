package com.fourspaces.couchdb.test;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * Date: 11/10/12
 *
 * @author lreeder
 */
public class TestHttpClient implements HttpClient
{

   private IOException exceptionToThrow = null;

   public TestHttpClient()
   {
   }

   public TestHttpClient(IOException exceptionToThrow)
   {
      this.exceptionToThrow = exceptionToThrow;
   }

   public HttpParams getParams()
   {
      throw new UnsupportedOperationException("Method getParams not implemented.");
   }

   public ClientConnectionManager getConnectionManager()
   {
      throw new UnsupportedOperationException("Method getConnectionManager not implemented.");
   }

   public HttpResponse execute(HttpUriRequest httpUriRequestIn) throws IOException, ClientProtocolException
   {

      if(exceptionToThrow != null)
      {
         throw exceptionToThrow;
      }

      throw new UnsupportedOperationException("Method execute not implemented.");
   }

   public HttpResponse execute(HttpUriRequest httpUriRequestIn,
      HttpContext httpContextIn) throws IOException, ClientProtocolException
   {
      throw new UnsupportedOperationException("Method execute not implemented.");
   }

   public HttpResponse execute(HttpHost httpHostIn,
      HttpRequest httpRequestIn) throws IOException, ClientProtocolException
   {
      throw new UnsupportedOperationException("Method execute not implemented.");
   }

   public HttpResponse execute(HttpHost httpHostIn, HttpRequest httpRequestIn,
      HttpContext httpContextIn) throws IOException, ClientProtocolException
   {
      throw new UnsupportedOperationException("Method execute not implemented.");
   }

   public <T> T execute(HttpUriRequest httpUriRequestIn,
      ResponseHandler<? extends T> responseHandlerIn) throws IOException, ClientProtocolException
   {
      throw new UnsupportedOperationException("Method execute not implemented.");
   }

   public <T> T execute(HttpUriRequest httpUriRequestIn, ResponseHandler<? extends T> responseHandlerIn,
      HttpContext httpContextIn) throws IOException, ClientProtocolException
   {
      throw new UnsupportedOperationException("Method execute not implemented.");
   }

   public <T> T execute(HttpHost httpHostIn, HttpRequest httpRequestIn,
      ResponseHandler<? extends T> responseHandlerIn) throws IOException, ClientProtocolException
   {
      throw new UnsupportedOperationException("Method execute not implemented.");
   }

   public <T> T execute(HttpHost httpHostIn, HttpRequest httpRequestIn, ResponseHandler<? extends T> responseHandlerIn,
      HttpContext httpContextIn) throws IOException, ClientProtocolException
   {
      throw new UnsupportedOperationException("Method execute not implemented.");
   }
}
