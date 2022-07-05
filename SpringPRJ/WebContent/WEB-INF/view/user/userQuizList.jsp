<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String SS_USER_TYPE = CmmUtil.nvl((String) session.getAttribute("SS_USER_TYPE"));

    List<String> rUserQuizTitleList = (List<String>) request.getAttribute("rUserQuizTitleList");

    if (rUserQuizTitleList == null) {
        rUserQuizTitleList = new ArrayList<>();
    }

    List<Map<String, String>> rQuizList = ((List<Map<String, String>>) request.getAttribute("rQuizList"));

    if (rQuizList == null) {
        rQuizList = new ArrayList<>();
    }
%>
<html>
<head>
    <!-- head Start -->
    <%@include file="/WEB-INF/view/inc/head.jsp" %>
    <!-- head end -->

    <style>
        .delete_box {
            border-radius: 15px;
            font-weight: bold;
            width: 30px;
            height: 30px;
            position: absolute;
            top: 10px;
            right: 40px;
            z-index: 2;
            font-size: large;
            text-align: center;
            color: white;
            cursor: pointer;
        }

        .quiz_title {
            text-align: center;
        }
    </style>
    <script type="text/javascript">
        function deleteButtonClickHandler(e) {
            let flag = confirm("삭제 하시겠습니까?");
            let quiz_title = e.parentNode.querySelector(".quiz_title").innerHTML;

            if (flag) {
                <% if (SS_USER_TYPE.equals("ADMIN")) { %>
                location.href = "/deleteOneAdminQuiz.do?quiz_title=" + quiz_title;
                <% } else { %>
                location.href = "/deleteOneQuiz.do?quiz_title=" + quiz_title;
                <% } %>
            }
        }
    </script>
</head>

<body>
<!-- Preloader Start -->
<%@include file="/WEB-INF/view/inc/preloader.jsp" %>
<!-- Preloader end -->

<!-- header start -->
<%@include file="/WEB-INF/view/inc/header.jsp" %>
<!-- header end -->

<main>
    <!-- slider Area Start-->
    <%@include file="/WEB-INF/view/inc/section.jsp" %>
    <!-- slider Area end-->

    <!-- Courses area start -->
    <div class="courses-area section-padding40 fix">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-xl-7 col-lg-8">
                    <div class="section-tittle text-center mb-55">
                        <% if (SS_USER_TYPE.equals("ADMIN")) { %>
                        <h2>전체 문제 리스트</h2>
                        <% } else { %>
                        <h2>나의 문제 리스트</h2>
                        <% } %>
                    </div>
                </div>
            </div>

            <div class="row">
                <% if (SS_USER_TYPE.equals("ADMIN")) {
                    int i = 0;
                    for (Map<String, String> rMap : rQuizList) {
                        i++;
                %>
                <div class="col-lg-4">
                    <div class="properties properties2 mb-30">
                        <div class="delete_box" onclick="deleteButtonClickHandler(this)">X</div>
                        <div class="properties__card">
                            <div class="properties__img overlay1">
                                <img src="${pageContext.request.contextPath}/resources/img/gallery/featured<%=(i%6)+1%>.png"
                                     alt="">
                            </div>
                            <div class="properties__caption">
                                <h3 class="quiz_title"><%=rMap.get("quiz_title") %>
                                </h3>
                                <div class="properties__footer d-flex justify-content-between align-items-center"></div>

                                <a href="${pageContext.request.contextPath}/updateAdminQuizPage.do?quiz_title=<%=rMap.get("quiz_title")%>&quiz_sort=<%=rMap.get("quiz_sort")%>"
                                   class="border-btn border-btn2 cursor_pointer">수정하기</a>
                            </div>
                        </div>
                    </div>
                </div>
                <%
                    }
                } else {
                    int i = 0;
                    for (String quizTitle : rUserQuizTitleList) {
                        i++;
                %>
                <div class="col-lg-4">
                    <div class="properties properties2 mb-30">
                        <div class="delete_box" onclick="deleteButtonClickHandler(this)">X</div>
                        <div class="properties__card">
                            <div class="properties__img overlay1">
                                <img src="${pageContext.request.contextPath}/resources/img/gallery/featured<%=(i%6)+1%>.png"
                                     alt="">
                            </div>
                            <div class="properties__caption">
                                <h3 class="quiz_title"><%=quizTitle%>
                                </h3>
                                <div class="properties__footer d-flex justify-content-between align-items-center"></div>

                                <a href="${pageContext.request.contextPath}/updateUserQuizPage.do?quiz_title=<%=quizTitle%>"
                                   class="border-btn border-btn2 cursor_pointer">수정하기</a>
                            </div>
                        </div>
                    </div>
                </div>
                <%
                        }
                    }
                %>

            </div>

            <% if (!SS_USER_TYPE.equals("ADMIN")) { %>
            <div class="row justify-content-center">
                <div class="col-xl-7 col-lg-8">
                    <div class="section-tittle text-center" style="margin-bottom: 40px;margin-top: 25px">
                        <button type="submit" class="boxed-btn" onclick="location.href='create.do'"
                                style="font-weight: bold;">생성하기
                        </button>
                    </div>
                </div>
            </div>
            <% } %>
        </div>
    </div>
    <!-- Courses area End -->
</main>

<!-- footer&Scroll Up start -->
<%@include file="/WEB-INF/view/inc/footer.jsp" %>
<!-- footer&Scroll Up end -->

<!-- js file start -->
<%@include file="/WEB-INF/view/inc/jsfile.jsp" %>
<!-- js file end -->

</body>
</html>