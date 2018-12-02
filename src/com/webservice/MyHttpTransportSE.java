package com.webservice;

import java.io.IOException;
import java.net.Proxy;

import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.ServiceConnection;
import org.ksoap2.transport.ServiceConnectionSE;

public class MyHttpTransportSE extends HttpTransportSE
{
	public MyHttpTransportSE(Proxy proxy,String url)
	{
		super(proxy, url);
		// TODO Auto-generated constructor stub
	}
	
	public MyHttpTransportSE(String url,int timeout)
	{
		super(url);
		this.timeout=timeout;
	}
	
	protected ServiceConnection getServiceConnection() throws IOException
	{
		ServiceConnectionSE serviceConnection=new ServiceConnectionSE(this.url,timeout);
		return serviceConnection;
	}

}
