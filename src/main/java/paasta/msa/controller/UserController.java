package paasta.msa.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import paasta.msa.common.CookieUtils;
import paasta.msa.service.UserService;

/**
 * @author Jae Young Im
 *
 */
@Controller
public class UserController {

	@Resource(name = "userService")
	private UserService userService;
	
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	private static String SUCCESS = "SUCCESS";
	private static String ERROR = "ERROR";
	private static String SESSION_ID = "session_id";
	private static String USER_ID = "user_id";
	private static String USER_NAME = "user_name";
	
	@RequestMapping(value = "/user/join")	
	public String getJoinPage() throws Exception {
		return "join";
	}
	
	@RequestMapping(value = "/user/login")	
	public String getLoginPage() throws Exception {
		ValueOperations<String, String> ops = redisTemplate.opsForValue();
		String test = "test1";
		if(!this.redisTemplate.hasKey(test)) {
			ops.set(test,"foo");
		}
		return "login";
	}

	@RequestMapping(value = "/user/createUser")
	@ResponseBody
	public Map<String, Object> createUser(@RequestParam Map<String, String> paramMap) throws Exception {
		Map<String, Object> result = userService.createUser(paramMap);
		return result;
	}

	
	@RequestMapping(value = "/user/getUser")	
	public String getUser(ModelMap model, @RequestParam Map<String, String> paramMap, @RequestParam(required = false) String userId) throws Exception {
		
		
		if (userId != null) {
			Map<String, Object> result = userService.getUser(paramMap);
			model.put("resultData", result.get("resultData"));
		}
		
		return "userDetail";
	}
	
	@RequestMapping(value = "/user/loginUser", method = RequestMethod.POST)	
	@ResponseBody
	public  Map<String, Object> loginUser(@RequestParam Map<String, String> paramMap
							, HttpServletRequest request
							, HttpServletResponse response) throws Exception {
		String sessionId = CookieUtils.getCookie(request, SESSION_ID).toString();
		ValueOperations<String, String> pos = redisTemplate.opsForValue();
		String userId = paramMap.get("userId");
		
		Map<String, Object> retMap = new HashMap<String, Object>();
		
		retMap.put("result", this.ERROR);
		
		if (userId != null) {
			if( sessionId == null || "".equals(sessionId) || !sessionId.equals(pos.get(userId))) {
				Map<String, Object> result = userService.checkLogin(paramMap);
				Map<String, Object> userMap = (Map<String,Object>) result.get("resultData");
				String newSessionId = this.getUniqueSessionId();
				
				if(SUCCESS.equals(result.get("result"))) {
					pos.set(userId, newSessionId);
					CookieUtils.addCookie(response, SESSION_ID, newSessionId, -1);
					CookieUtils.addCookie(response, USER_ID, userId, -1);
					CookieUtils.addCookie(response, USER_NAME, (String)userMap.get("userName"), -1);
				}
				
				retMap.put("result", result.get("result"));
				retMap.put("resultData", result.get("resultData"));
				retMap.put("errMsg", result.get("errMsg"));
			} else {
				retMap.put("result", this.ERROR);
				retMap.put("errMsg", "현재 로그인 중입니다.");
				
			}
		}
		return retMap;
	}
	@RequestMapping(value = "/user/logout", method = RequestMethod.GET)	
	public  String logout(HttpServletRequest request
										, HttpServletResponse response) throws Exception {
		String sessionId = CookieUtils.getCookie(request, SESSION_ID).toString();
		String userId = CookieUtils.getCookie(request, USER_ID).toString();
		ValueOperations<String, String> pos = redisTemplate.opsForValue();
		
		if(sessionId != null) {
			CookieUtils.deleteCookie(request, response, SESSION_ID);
		}
		
		if(userId != null) {
			CookieUtils.deleteCookie(request, response, USER_ID);
		}
		
		if(pos.get(userId) != null) {
			redisTemplate.opsForValue().getOperations().delete(String.valueOf(userId));
		}
		
		return "redirect:/board";
	}

	@RequestMapping(value = "/user/updateUser")
	@ResponseBody
	public Map<String, Object> updateUser(@RequestParam Map<String, String> paramMap) throws Exception {
		
		Map<String, Object> result = userService.updateUser(paramMap);
		
		return result;
	}


	@RequestMapping(value = "/user/deleteUser")
	@ResponseBody
	public Map<String, Object> deletUser(@RequestParam Map<String, String> paramMap) throws Exception {
		
		Map<String, Object> result = userService.deleteUser(paramMap);

		return result;
	}
	
	public String getUniqueSessionId() {
		String sessionId = "";
		String uuid = UUID.randomUUID().toString();
		String[] jsessionIdArray = uuid.split("-");
		StringBuilder jsessionIdBuilder = new StringBuilder();
		for (String str : jsessionIdArray) {
			jsessionIdBuilder.append(str);
		}
		sessionId = jsessionIdBuilder.toString();
		return sessionId;
	}

}