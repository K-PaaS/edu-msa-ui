package paasta.msa.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import paasta.msa.common.RestClient;
import paasta.msa.service.UiService;

@Service("uiService")
public class UiServiceImpl implements UiService {

	@Resource(name = "restClient")
	private RestClient restClient;

	public Map<String, Object> getBoardList(Map<String, String> paramMap) throws Exception {
		
		Map<String, Object> result = restClient.get("http://localhost:8080/board", new HashMap<String, String>(), paramMap);
		return result;
	}

	public Map<String, Object> getBoardDetail(Map<String, String> paramMap) throws Exception {
		
		Map<String, Object> result = restClient.get("http://localhost:8080/board/" + paramMap.get("boardSeq"), new HashMap<String, String>(), paramMap);
		return result;
	}

	public Map<String, Object> getBoardCreate(Map<String, String> paramMap) throws Exception {
		
		Map<String, Object> result = restClient.post("http://localhost:8080/board/" + paramMap.get("userId"), new HashMap<String, String>(), paramMap);
		return result;
	}

	public Map<String, Object> getBoardUpdate(Map<String, String> paramMap) throws Exception {
		
		Map<String, Object> result = restClient.put("http://localhost:8080/board/" + paramMap.get("boardSeq"), new HashMap<String, String>(), paramMap);
		return result;
	}

	public Map<String, Object> getBoardDelete(Map<String, String> paramMap) throws Exception {
		
		Map<String, Object> result = restClient.delete("http://localhost:8080/board/" + paramMap.get("boardSeq") + "/" + paramMap.get("userId"), new HashMap<String, String>());
		return result;
	}

//	public List<Object> getBoard(Map<String, Object> paramMap) throws Exception {
//		return null;
//	}
//
//	public int postBoard(Map<String, Object> paramMap) throws Exception {
//		return 0;
//	}
//
//	public int putBoard(Map<String, Object> paramMap) throws Exception {
//		return 0;
//	}
//
//	public int deleteBoard(Map<String, Object> paramMap) throws Exception {
//		return 0;
//	}
//
//	@SuppressWarnings("unchecked")
//	public Map<String, Object> getCommentList(Map<String, Object> paramMap) throws Exception {
//
//		return null;
//	}

}
