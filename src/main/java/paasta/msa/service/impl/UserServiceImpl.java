package paasta.msa.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import paasta.msa.common.RestClient;
import paasta.msa.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService{

	@Resource(name = "restClient")
	private RestClient restClient;
	
	@Value("#{apiProperties['ApiEndpoint']}")
	private String apiEndpoint;

	
	public Map<String, Object> getUserList(Map<String, String> paramMap) throws Exception {
		Map<String, Object> result = restClient.get(apiEndpoint + "/user/", new HashMap<String, String>(), paramMap);
		return result;
	}
	
	public Map<String, Object> getUser(Map<String, String> paramMap) throws Exception {
		Map<String, Object> result = restClient.get(apiEndpoint + "/user/" + paramMap.get("userId"), new HashMap<String, String>(), paramMap);
		return result;
	}
	
	public Map<String, Object> checkLogin(Map<String, String> paramMap) throws Exception {
		Map<String, Object> result = restClient.post(apiEndpoint + "/user/checkLogin/", new HashMap<String, String>(), paramMap);
		return result;
	}

	public Map<String, Object> createUser(Map<String, String> paramMap) throws Exception {
		Map<String, Object> result = restClient.post(apiEndpoint + "/user/", new HashMap<String, String>(), paramMap);
		return result;
	}

	public Map<String, Object> updateUser(Map<String, String> paramMap) throws Exception {
		Map<String, Object> result = restClient.put(apiEndpoint + "/user/"  + paramMap.get("userId"), new HashMap<String, String>(), paramMap);
		return result;
	}

	public Map<String, Object> deleteUser(Map<String, String> paramMap) throws Exception{
		Map<String, Object> result = restClient.delete(apiEndpoint + "/user/"  + paramMap.get("userId"), new HashMap<String, String>());
		return result;
	}

}
