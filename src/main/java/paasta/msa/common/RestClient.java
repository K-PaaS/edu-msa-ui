package paasta.msa.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component("restClient")
public class RestClient {
	
	@Resource(name = "apiProperties")
	private Properties apiProperties;

	private Map<String, Object> call(HttpUriRequest request) throws Exception {
        //http client 생성
        CloseableHttpClient httpClient = HttpClients.custom()
                .setRedirectStrategy(new LaxRedirectStrategy())
                .build();

        StringBuffer response = null;
        try {
            //get 요청
            CloseableHttpResponse httpResponse = httpClient.execute(request);
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            if(statusCode == 200) {
                BufferedReader reader = null;
         
                try {
                	reader = new BufferedReader(new InputStreamReader(
                            httpResponse.getEntity().getContent(), "UTF-8"));
                    String inputLine;
                    response = new StringBuffer();
             
                    while ((inputLine = reader.readLine()) != null) {
                        response.append(inputLine);
                    }
                } catch (Exception e) {
                	throw e;
                } finally {
                	try {reader.close();} catch (Exception e) {}
                }
                
            } else if (statusCode == 301 || statusCode == 302) {
            	
            	for(Header header : httpResponse.getHeaders("Location")) {
                	throw new RedirectException(statusCode, header.getValue());
            	}

            } else {
            	throw new Exception("http 접속이 정상 종료되지 않았습니다. : " + statusCode);
            }
        } catch (Exception e) {
        	throw e;
        } finally {
            httpClient.close();
        }
        Map<String, Object> result = new ObjectMapper().readValue(response.toString(), new TypeReference<Map<String, Object>>() { });

        return result;
	}

	public Map<String, Object> get(String url, Map<String, String> headerMap, Map<String, String> paramMap) throws Exception {
 
        Set<String> keySet = paramMap.keySet();
        Iterator<String> it = keySet.iterator();
        StringBuffer paramStringBf = new StringBuffer();

        while(it.hasNext()) {
        	String key = it.next();
        	paramStringBf.append(key);
        	paramStringBf.append("=");
        	paramStringBf.append(paramMap.get(key));
        	paramStringBf.append("&");
        }
		String paramString = paramStringBf.toString();
		if(!"".equals(paramString)) {
			paramString = "?" + paramString.substring(0, paramString.length() -1);
		}
        //get 메서드와 URL 설정
        HttpGet httpGet = new HttpGet(url + paramString);
 
        keySet = headerMap.keySet();
        it = keySet.iterator();
        
        // 암호화 키 추가
        String timestamp = new Timestamp(System.currentTimeMillis()).toString();
        String apiKey = makeKey(timestamp);

        httpGet.addHeader("Content-Type", "application/json;charset=UTF-8");
        httpGet.addHeader("apiKey", apiKey);
        httpGet.addHeader("timestamp", timestamp);

        while(it.hasNext()) {
        	String key = it.next();
            httpGet.addHeader(key, headerMap.get(key));
        }
        
        
        return call(httpGet);
	}
	
	public Map<String, Object> post(String url, Map<String, String> headerMap, Map<String, String> paramMap) throws Exception {
		 
        //get 메서드와 URL 설정
		HttpPost httpPost = new HttpPost(url);
        Map<String, Object> result = null;
		ObjectMapper mapper = new ObjectMapper();
        
        try {
        	String json = mapper.writeValueAsString(paramMap);
        	System.out.println(url);
        	System.out.println(json);
        	httpPost.setEntity(new StringEntity(json, "UTF-8"));
        	
	        Set<String> keySet = headerMap.keySet();
	        Iterator<String> it = keySet.iterator();
	        
	        // 암호화 키 추가
	        String timestamp = new Timestamp(System.currentTimeMillis()).toString();
	        String apiKey = makeKey(timestamp);
	        httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
	        httpPost.addHeader("apiKey", apiKey);
	        httpPost.addHeader("timestamp", timestamp);
	
	        while(it.hasNext()) {
	        	String key = it.next();
	        	httpPost.addHeader(key, headerMap.get(key));
	        }
	        
	        result = call(httpPost);
	        
	        System.out.println(result);
        } catch (RedirectException e) {
        	result = post(e.url, headerMap, paramMap);
        } catch (Exception e) {
        	throw e;
        }
        return result;
	}
	
	public Map<String, Object> put(String url, Map<String, String> headerMap, Map<String, String> paramMap) throws Exception {
		 
        //get 메서드와 URL 설정
		HttpPut httpPut = new HttpPut(url);
        Map<String, Object> result = null;
		ObjectMapper mapper = new ObjectMapper();
        
        try {
        	String json = mapper.writeValueAsString(paramMap);
        	httpPut.setEntity(new StringEntity(json, "UTF-8"));
        	
	        Set<String> keySet = headerMap.keySet();
	        Iterator<String> it = keySet.iterator();
	        
	        // 암호화 키 추가
	        String timestamp = new Timestamp(System.currentTimeMillis()).toString();
	        String apiKey = makeKey(timestamp);
	        httpPut.addHeader("Content-Type", "application/json;charset=UTF-8");
	        httpPut.addHeader("apiKey", apiKey);
	        httpPut.addHeader("timestamp", timestamp);
	
	        while(it.hasNext()) {
	        	String key = it.next();
	        	httpPut.addHeader(key, headerMap.get(key));
	        }
	        
	        result = call(httpPut);
        } catch (RedirectException e) {
        	result = put(e.url, headerMap, paramMap);
        } catch (Exception e) {
        	throw e;
        }
        return result;
	}
	
	public Map<String, Object> delete(String url, Map<String, String> headerMap) throws Exception {
		 
        //get 메서드와 URL 설정
		HttpDelete httpDelete = new HttpDelete(url);
        Map<String, Object> result = null;
        
        try {
	        Set<String> keySet = headerMap.keySet();
	        Iterator<String> it = keySet.iterator();
	        
	        // 암호화 키 추가
	        String timestamp = new Timestamp(System.currentTimeMillis()).toString();
	        String apiKey = makeKey(timestamp);
	
	        httpDelete.addHeader("Content-Type", "text/plain;charset=UTF-8");
	        httpDelete.addHeader("apiKey", apiKey);
	        httpDelete.addHeader("timestamp", timestamp);
	
	        while(it.hasNext()) {
	        	String key = it.next();
	        	httpDelete.addHeader(key, headerMap.get(key));
	        }
	        
	        result = call(httpDelete);
        } catch (RedirectException e) {
        	result = delete(e.url, headerMap);
        } catch (Exception e) {
        	throw e;
        }
        
        return result;
	}
	
	private String makeKey(String timestamp) {
		String makeKey = null;
		String salt = apiProperties.getProperty("ApiKeySalt");

		if (timestamp != null && !"".equals(timestamp)) {
			MessageDigest md = null;
			try {
				md = MessageDigest.getInstance("SHA-256");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			md.update(salt.getBytes());
			md.update(timestamp.getBytes());
			makeKey = String.format("%064x", new BigInteger(1, md.digest()));
		}

		return makeKey;
	}
}
