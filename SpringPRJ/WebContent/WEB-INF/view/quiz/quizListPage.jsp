<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    List<String> SS_QUIZ_CONT_LIST = (List<String>) session.getAttribute("SS_QUIZ_CONT_LIST");

    if (SS_QUIZ_CONT_LIST == null) {
        SS_QUIZ_CONT_LIST = new ArrayList<>();
    }
%>
<html>
<head>
    <!-- head Start -->
    <%@include file="/WEB-INF/view/inc/head.jsp" %>
    <!-- head end -->

    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/page/quizListPage.css">

    <script type="text/javascript">
        function onePracticeHandler(e) {
            let quizContTitle = e.innerText;
            console.log(quizContTitle);

            let isConfirm = confirm("개별 학습하겠습니까?");
            if (isConfirm) {
                location.href = "play.do?quizContTitle=" + quizContTitle;
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

    <div class="whole-wrap">
        <div class="container box_1170">
            <div class="section-top-border">
                <h3 class="mb-30">${quizTitle}</h3>
                <div class="progress-table-wrap">
                    <div class="progress-table" style="padding-bottom: 15px;">
                        <div class="table-head">
                            <div class="serial number">NO</div>
                            <div class="country quiz">QUIZ</div>
                        </div>

                        <%
                            for (int i = 0; i < SS_QUIZ_CONT_LIST.size(); i++) {
                                int j = i + 1;
                                String num = j < 10 ? "0" + j : String.valueOf(j);
                        %>

                        <div class="table-row">
                            <div class="serial number"><%=num %>
                            </div>
                            <div class="country quiz" onclick="onePracticeHandler(this)"><%=SS_QUIZ_CONT_LIST.get(i) %>
                            </div>
                        </div>

                        <% }%>

                    </div>
                </div>
            </div>

            <div id="table_button_box">
                <button type="submit" class="boxed-btn practice_button" onclick="randomClickHandler()">랜덤학습</button>
                <button type="submit" class="boxed-btn history_button" onclick="history.back()">이전으로</button>
            </div>
        </div>
    </div>
</main>

<!-- footer&Scroll Up start -->
<%@include file="/WEB-INF/view/inc/footer.jsp" %>
<!-- footer&Scroll Up end -->

<!-- js file start -->
<%@include file="/WEB-INF/view/inc/jsfile.jsp" %>
<!-- js file end -->

<script>
    // make js quiz list
    let quizContList = [];

    <% for(String quizCont:SS_QUIZ_CONT_LIST) { %>
    quizContList.push("<%=quizCont %>");
    <%} %>

    function random_item(items) {
        return items[Math.floor(Math.random() * items.length)];
    }

    function randomClickHandler() {
        let cnt = quizContList.length;

        if (cnt !== 0) {
            let quizContTitle = random_item(quizContList);
            location.href = "play.do?quizContTitle=" + quizContTitle;
        }
    }
</script>

</body>
</html>