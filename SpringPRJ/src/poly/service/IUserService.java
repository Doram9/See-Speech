package poly.service;

import java.util.List;
import java.util.Map;

public interface IUserService {
    // 유저 회원가입
    void insertUser(Map<String, Object> pMap) throws Exception;

    // 유저 회원가입 여부, 이메일
    Map<String, String> getUserExistForAJAX(String user_email) throws Exception;

    // 유저 로그인
    Map<String, String> getUser(Map<String, String> pMap) throws Exception;

    // 유저 비밀번호 변경
    int updateUserPw(Map<String, Object> pMap) throws Exception;

    // 유저 정보 삭제
    int deleteUser(Map<String, Object> pMap) throws Exception;

    int saveUserRate(Map<String, Object> pMap) throws Exception;

    List<Map<String, String>> getUserList() throws Exception;
}
