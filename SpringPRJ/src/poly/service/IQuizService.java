package poly.service;

import java.util.List;
import java.util.Map;

public interface IQuizService {
    // 기본 퀴즈 리스트 가져오기
    List<Map<String, String>> getQuizList() throws Exception;

    // 기본 퀴즈 컨텐츠 리스트 가져오기
    List<String> getQuizContList(String quizTitle, String quizSort) throws Exception;

    // 유저 퀴즈 생성
    void insertUserQuiz(Map<String, String> pMap) throws Exception;

    // 유저 퀴즈 리스트 가져오기
    List<String> getUserQuizTitleList(String user_email) throws Exception;

    // 유저 퀴즈 컨텐츠 리스트 가져오기
    List<String> getUserQuizContList(String user_email, String quiz_title) throws Exception;

    // 퀴즈 생성 시, 중복 퀴즈 제목 체크
    boolean isQuizTitleExistForAJAX(String user_email, String quiz_title) throws Exception;

    // 유저 삭제 시, 기존 유저 퀴즈 목록 삭제
    int deleteUserQuiz(Map<String, Object> pMap) throws Exception;

    // 퀴즈 하나 삭제
    int deleteOneQuiz(Map<String, Object> pMap) throws Exception;

    // 퀴즈 업데이트
    int updateUserQuiz(Map<String, String> pMap) throws Exception;

    // 관리자 퀴즈 생성
    void insertAdminQuiz(Map<String, String> pMap) throws Exception;

    // 관리자 퀴즈 삭제
    int deleteOneAdminQuiz(Map<String, Object> pMap) throws Exception;
    
    // 관리자 퀴즈 업데이터
    int updateAdminQuiz(Map<String, String> pMap) throws Exception;

    List<String> getToDayQuiz() throws Exception;
}
