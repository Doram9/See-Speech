package poly.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import poly.service.IQuizService;
import poly.util.CmmUtil;
import poly.util.EncryptUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class QuizController {

    private final Logger log = Logger.getLogger(this.getClass());

    @Resource(name = "QuizService")
    private IQuizService quizService;

    @RequestMapping(value = "toDayQuiz.do")
    public String getToDayQuiz(HttpSession session, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".toDayQuiz start!");

        List<String> rQuizContList = quizService.getToDayQuiz();
        session.setAttribute("SS_QUIZ_CONT_LIST", rQuizContList);

        model.addAttribute("quizContTitle", "오늘의 단어");

        log.info(this.getClass().getName() + ".toDayQuiz end!");

        return "/quiz/quizListPage";
    }

    @RequestMapping(value = "wordGame.do")
    public String wordGame() throws Exception {
        return "/quiz/wordGame";
    }

    @RequestMapping(value = "index.do")
    public String index(ModelMap model, HttpSession session) throws Exception {

        log.info(this.getClass().getName() + ".index start!");

        try {
            String SS_USER_EMAIL = CmmUtil.nvl((String) session.getAttribute("SS_USER_EMAIL"));
            String user_email = EncryptUtil.encAES128CBC(SS_USER_EMAIL);

            List<String> rUserQuizTitleList = new ArrayList<>();
            // 유저가 로그인 했을 경우, 자신의 퀴즈 리스트 가져옴
            if (!SS_USER_EMAIL.equals("")) {
                rUserQuizTitleList = quizService.getUserQuizTitleList(user_email);
            }

            List<Map<String, String>> rQuizList = quizService.getQuizList();

            model.addAttribute("rUserQuizTitleList", rUserQuizTitleList);
            model.addAttribute("rQuizList", rQuizList);
        } catch (Exception e) {
            log.info(e.toString());
            e.printStackTrace();
        }

        log.info(this.getClass().getName() + ".index end!");

        return "/index";
    }

    @RequestMapping(value = "listPage.do")
    public String listPage(HttpServletRequest request, ModelMap model, HttpSession session) throws Exception {

        log.info(this.getClass().getName() + ".listPage start!");

        try {
            String quizTitle = CmmUtil.nvl(request.getParameter("quizTitle"));
            String quizSort = CmmUtil.nvl(request.getParameter("quizSort"));
            String SS_USER_EMAIL = CmmUtil.nvl((String) session.getAttribute("SS_USER_EMAIL"));

            log.info("quizTitle : " + quizTitle);
            log.info("quizSort : " + quizSort);
            log.info("SS_USER_EMAIL : " + SS_USER_EMAIL);

            List<String> rQuizContList;

            if (quizSort.equals("2") && !SS_USER_EMAIL.equals("")) {
                String user_email = EncryptUtil.encAES128CBC(SS_USER_EMAIL);
                rQuizContList = quizService.getUserQuizContList(user_email, quizTitle);
            } else {
                rQuizContList = quizService.getQuizContList(quizTitle, quizSort);
            }

            session.setAttribute("SS_QUIZ_CONT_LIST", rQuizContList);

            model.addAttribute("quizTitle", quizTitle);
        } catch (Exception e) {
            log.info(e.toString());
            e.printStackTrace();
        }

        log.info(this.getClass().getName() + ".listPage end!");

        return "/quiz/quizListPage";
    }

    @RequestMapping(value = "play.do")
    public String play(HttpServletRequest request, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".play start!");

        String quizContTitle = CmmUtil.nvl(request.getParameter("quizContTitle"));

        log.info("quizContTitle : " + quizContTitle);

        model.addAttribute("quizContTitle", quizContTitle);

        log.info(this.getClass().getName() + ".play end!");

        return "/quiz/quizPlay";
    }

    @RequestMapping(value = "userQuizList.do")
    public String userQuizList(HttpSession session, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".userQuizList start!");

        try {

            String SS_USER_EMAIL = CmmUtil.nvl((String) session.getAttribute("SS_USER_EMAIL"));
            log.info("SS_USER_EMAIL : " + SS_USER_EMAIL);

            if (SS_USER_EMAIL.equals("")) {
                model.addAttribute("msg", "로그인이 필요합니다.");
                model.addAttribute("url", "/loginPage.do");
                return "/redirect";
            }

            String user_email = EncryptUtil.encAES128CBC(SS_USER_EMAIL);

            List<String> rUserQuizTitleList = quizService.getUserQuizTitleList(user_email);

            model.addAttribute("rUserQuizTitleList", rUserQuizTitleList);

        } catch (Exception e) {
            log.info(e.toString());
            e.printStackTrace();
        }

        log.info(this.getClass().getName() + ".userQuizList end!");

        return "/user/userQuizList";
    }

    @RequestMapping(value = "create.do")
    public String create(HttpSession session, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".create start!");

        String SS_USER_EMAIL = CmmUtil.nvl((String) session.getAttribute("SS_USER_EMAIL"));
        log.info("SS_USER_EMAIL : " + SS_USER_EMAIL);

        if (SS_USER_EMAIL.equals("")) {
            model.addAttribute("msg", "로그인이 필요합니다.");
            model.addAttribute("url", "/loginPage.do");
            return "/redirect";
        }

        log.info(this.getClass().getName() + ".create end!");

        return "/quiz/quizCreate";
    }

    @RequestMapping(value = "insertQuiz.do", method = RequestMethod.POST)
    public String insertQuiz(HttpServletRequest request, HttpSession session, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".insertQuiz start!");

        try {

            String strQuizList = CmmUtil.nvl(request.getParameter("quizListHiddenInput"));
            String quiz_title = CmmUtil.nvl(request.getParameter("quiz_title"));
            String SS_USER_EMAIL = CmmUtil.nvl((String) session.getAttribute("SS_USER_EMAIL"));

            log.info("strQuizList : " + strQuizList);
            log.info("quiz_title : " + quiz_title);
            log.info("SS_USER_EMAIL : " + SS_USER_EMAIL);

            if (SS_USER_EMAIL.equals("admin@email.com")) {

                String quiz_sort = CmmUtil.nvl(request.getParameter("quiz_sort"));
                log.info("quiz_sort : " + quiz_sort);

                Map<String, String> pMap = new HashMap<>();
                pMap.put("quiz_sort", quiz_sort);
                pMap.put("strQuizList", strQuizList);
                pMap.put("quiz_title", quiz_title);

                quizService.insertAdminQuiz(pMap);

                model.addAttribute("msg", "관리자 퀴즈 저장 성공!");
                model.addAttribute("url", "/index.do");

            } else {
                Map<String, String> pMap = new HashMap<>();
                pMap.put("user_email", SS_USER_EMAIL);
                pMap.put("strQuizList", strQuizList);
                pMap.put("quiz_title", quiz_title);

                quizService.insertUserQuiz(pMap);

                model.addAttribute("msg", "내 퀴즈 저장 성공!");
                model.addAttribute("url", "/userQuizList.do");

            }


        } catch (Exception e) {
            log.info(e.toString());
            e.printStackTrace();
        }

        log.info(this.getClass().getName() + ".insertQuiz end!");

        return "/redirect";

    }

    @RequestMapping(value = "isQuizTitleExistForAJAX.do", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> isQuizTitleExistForAJAX(HttpServletRequest request, HttpSession session) throws Exception {

        log.info(this.getClass().getName() + ".isQuizTitleExistForAJAX start!");

        Map<String, String> rMap = new HashMap<>();

        try {
            String SS_USER_EMAIL = CmmUtil.nvl((String) session.getAttribute("SS_USER_EMAIL"));
            String quiz_title = CmmUtil.nvl(request.getParameter("quiz_title"));
            String user_email = EncryptUtil.encAES128CBC(SS_USER_EMAIL);

            log.info("user_email : " + SS_USER_EMAIL);
            log.info("quiz_title : " + quiz_title);

            boolean res = quizService.isQuizTitleExistForAJAX(user_email, quiz_title);

            // true - 존재, false - 없음
            if (res) {
                rMap.put("res", "true");
            } else {
                rMap.put("res", "false");
            }
        } catch (Exception e) {
            log.info(e.toString());
            e.printStackTrace();
        }

        log.info(this.getClass().getName() + ".isQuizTitleExistForAJAX end!");

        return rMap;
    }

    @RequestMapping(value = "deleteOneQuiz.do")
    public String deleteOneQuiz(HttpServletRequest request, HttpSession session, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".deleteOneQuiz start!");

        String url = "/userQuizList.do";
        String msg = "";

        try {

            String SS_USER_EMAIL = CmmUtil.nvl((String) session.getAttribute("SS_USER_EMAIL"));
            String user_email = EncryptUtil.encAES128CBC(SS_USER_EMAIL);
            String quiz_title = CmmUtil.nvl(request.getParameter("quiz_title"));

            log.info("user_email : " + SS_USER_EMAIL);
            log.info("quiz_title : " + quiz_title);

            Map<String, Object> pMap = new HashMap<>();
            pMap.put("user_email", user_email);
            pMap.put("quiz_title", quiz_title);

            int res = quizService.deleteOneQuiz(pMap);

            if (res == 1) {
                msg = "퀴즈 삭제 성공";
            } else {
                msg = "퀴즈 삭제 실패";
            }

        } catch (Exception e) {
            msg = "서버 오류 입니다.";
            log.info(e.toString());
            e.printStackTrace();
        }

        model.addAttribute("msg", msg);
        model.addAttribute("url", url);

        log.info(this.getClass().getName() + ".deleteOneQuiz end!");

        return "/redirect";
    }

    @RequestMapping(value = "updateUserQuizPage.do")
    public String updateUserQuizPage(HttpServletRequest request, HttpSession session, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".updateUserQuizPage start!");

        try {
            String quiz_title = CmmUtil.nvl(request.getParameter("quiz_title"));
            String SS_USER_EMAIL = CmmUtil.nvl((String) session.getAttribute("SS_USER_EMAIL"));
            String user_email = EncryptUtil.encAES128CBC(SS_USER_EMAIL);

            log.info("quiz_title : " + quiz_title);
            log.info("user_email : " + SS_USER_EMAIL);

            List<String> rUserQuizContList = quizService.getUserQuizContList(user_email, quiz_title);

            model.addAttribute("rUserQuizContList", rUserQuizContList);
            model.addAttribute("quiz_title", quiz_title);

        } catch (Exception e) {
            log.info(e.toString());
            e.printStackTrace();
        }

        log.info(this.getClass().getName() + ".updateUserQuizPage end!");

        return "/quiz/quizCreate";
    }

    @RequestMapping(value = "updateUserQuiz.do", method = RequestMethod.POST)
    public String updateUserQuiz(HttpServletRequest request, HttpSession session, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".updateUserQuiz start!");

        String msg = "";
        String url = "/userQuizList.do";

        try {

            String SS_USER_TYPE = CmmUtil.nvl((String) session.getAttribute("SS_USER_TYPE"));

            String strQuizList = CmmUtil.nvl(request.getParameter("quizListHiddenInput"));
            String quiz_title = CmmUtil.nvl(request.getParameter("quiz_title"));

            log.info("strQuizList : " + strQuizList);
            log.info("quiz_title : " + quiz_title);

            if (SS_USER_TYPE.equals("ADMIN")) {

                Map<String, String> pMap = new HashMap<>();
                pMap.put("strQuizList", strQuizList);
                pMap.put("quiz_title", quiz_title);

                int res = quizService.updateAdminQuiz(pMap);

                if (res > 0) {
                    msg = "관리자 퀴즈 업데이트 성공!";
                    url = "/adminQuizList.do";
                } else {
                    msg = "관리자 퀴즈 업데이트 실패!";
                    url = "/adminQuizList.do";
                }

            } else {

                String SS_USER_EMAIL = CmmUtil.nvl((String) session.getAttribute("SS_USER_EMAIL"));

                log.info("SS_USER_EMAIL : " + SS_USER_EMAIL);

                Map<String, String> pMap = new HashMap<>();
                pMap.put("user_email", SS_USER_EMAIL);
                pMap.put("strQuizList", strQuizList);
                pMap.put("quiz_title", quiz_title);

                int res = quizService.updateUserQuiz(pMap);

                if (res > 0) {
                    msg = "내 퀴즈 업데이트 성공!";
                } else {
                    msg = "내 퀴즈 업데이트 실패!";
                }
            }

        } catch (Exception e) {
            msg = "서버 오류입니다.";
            log.info(e.toString());
            e.printStackTrace();
        }

        model.addAttribute("msg", msg);
        model.addAttribute("url", url);

        log.info(this.getClass().getName() + ".updateUserQuiz end!");

        return "/redirect";
    }

    @RequestMapping(value = "adminQuizList.do")
    public String adminQuizList(HttpSession session, ModelMap model) {

        log.info(this.getClass().getName() + ".adminQuizList start!");

        try {

            String SS_USER_EMAIL = CmmUtil.nvl((String) session.getAttribute("SS_USER_EMAIL"));
            log.info("SS_USER_EMAIL : " + SS_USER_EMAIL);

            if (SS_USER_EMAIL.equals("")) {
                model.addAttribute("msg", "로그인이 필요합니다.");
                model.addAttribute("url", "/loginPage.do");
                return "/redirect";
            }

            List<Map<String, String>> rQuizList = quizService.getQuizList();

            model.addAttribute("rQuizList", rQuizList);

        } catch (Exception e) {
            log.info(e.toString());
            e.printStackTrace();
        }

        log.info(this.getClass().getName() + ".adminQuizList end!");

        return "/user/userQuizList";
    }

    @RequestMapping(value = "deleteOneAdminQuiz.do")
    public String deleteOneAdminQuiz(HttpServletRequest request, HttpSession session, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".deleteOneAdminQuiz start!");

        String url = "/adminQuizList.do";
        String msg = "";

        try {

            String quiz_title = CmmUtil.nvl(request.getParameter("quiz_title"));

            log.info("quiz_title : " + quiz_title);

            Map<String, Object> pMap = new HashMap<>();
            pMap.put("quiz_title", quiz_title);

            int res = quizService.deleteOneAdminQuiz(pMap);

            if (res == 1) {
                msg = "퀴즈 삭제 성공";
            } else {
                msg = "퀴즈 삭제 실패";
            }

        } catch (Exception e) {
            msg = "서버 오류 입니다.";
            log.info(e.toString());
            e.printStackTrace();
        }

        model.addAttribute("msg", msg);
        model.addAttribute("url", url);

        log.info(this.getClass().getName() + ".deleteOneAdminQuiz end!");

        return "/redirect";
    }

    @RequestMapping(value = "updateAdminQuizPage.do")
    public String updateAdminQuizPage(HttpServletRequest request, HttpSession session, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".updateAdminQuizPage start!");

        try {
            String quiz_title = CmmUtil.nvl(request.getParameter("quiz_title"));
            String quiz_sort = CmmUtil.nvl(request.getParameter("quiz_sort"));

            log.info("quiz_title : " + quiz_title);

            List<String> rUserQuizContList = quizService.getQuizContList(quiz_title, quiz_sort);

            model.addAttribute("rUserQuizContList", rUserQuizContList);
            model.addAttribute("quiz_title", quiz_title);

        } catch (Exception e) {
            log.info(e.toString());
            e.printStackTrace();
        }

        log.info(this.getClass().getName() + ".updateAdminQuizPage end!");

        return "/quiz/quizCreate";
    }

}
