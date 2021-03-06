package crawlers.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import crawlers.modules.DNSResolution;

public class MakeRequest {
	
    private static  CloseableHttpClient httpClient;

	
	public static String getContentOf(String domainName, String path) {
		
		String address = DNSResolution.resolveHostnameToIP("www.google.com").getHostAddress();
		
		URI uri = buildUri(address, path);
		
		if(uri == null)
			return null;

		return get(uri);
	}
	
	public static URI buildUri(String ip, String path) {
		URIBuilder uri = null;
		try {
			uri = new URIBuilder();
			return uri.setScheme("http").setHost(ip).setPath(path).build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static boolean head(URI uri) {
	    httpClient = HttpClients.createDefault();

		HttpHead request = null;
        
        try{
			request = new HttpHead(uri);
			request.addHeader(HttpHeaders.USER_AGENT, "Googlebot");
			
			CloseableHttpResponse response = httpClient.execute(request);
			if(response.getStatusLine().getStatusCode() == 200)
				return true;
        }
        catch(ClientProtocolException cpe) {cpe.printStackTrace();}
        catch (IOException ie) {ie.printStackTrace();}
        
        return false;
	}
	
	//Sends head request
	public static boolean isFound(String domainName, String path) {
		String address = DNSResolution.resolveHostnameToIP("www.google.com").getHostAddress();
		
		URI uri = buildUri(address, path);
		
		return head(uri);
	}

	//Gets host-name's as IntetAddress format then makes get request and returns it's body as String
	public static String get(URI uri) {
		//getHostAddress convert InetAddress to string presentation
		HttpGet request = new HttpGet(uri);
		httpClient = HttpClients.createDefault();
		HttpEntity entity = null;
		String content = "";
		
		request.addHeader(HttpHeaders.USER_AGENT, "Googlebot");
		
		try (CloseableHttpResponse response = httpClient.execute(request)) {
			
			System.out.println("HTTP request has been sent to " + uri.toURL());
			
			response.getAllHeaders().toString();
			
			if(response.getStatusLine().getStatusCode() == 200){
				entity = response.getEntity();
				content = response.getAllHeaders().toString();
			}
				
			if(entity != null)
				content += EntityUtils.toString(entity);
		}
		catch(ClientProtocolException cpe) { cpe.printStackTrace(); }
		catch (IOException ie) { ie.printStackTrace(); }
		
		return content;
	}
}
