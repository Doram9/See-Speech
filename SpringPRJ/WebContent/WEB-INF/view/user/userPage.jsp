<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String SS_USER_TYPE = CmmUtil.nvl((String) session.getAttribute("SS_USER_TYPE"), "0");
%>
<html>
<head>
    <!-- head Start -->
    <%@include file="/WEB-INF/view/inc/head.jsp" %>
    <!-- head end -->

    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/page/userPage.css">
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

    <section class="blog_area single-post-area section-padding" style="padding-top: 60px; padding-bottom: 60px">
        <div class="container">
            <h3 class="mb-30">User</h3>

            <div class="blog-author">
                <div class="media align-items-center">
                    <img src="${pageContext.request.contextPath}/resources/img/chat/basicUser.png" alt="">
                    <div class="media-body">
                        <h4>${SS_USER_EMAIL} (${SS_USER_NAME})
                        </h4>
                    </div>
                </div>
            </div>

            <div class="userInfo_buttonBox">
                <% if (SS_USER_TYPE.equals("google")) { %>
                <button onclick="deleteUser()" class="boxed-btn form__button delete_userButton">
                    계정삭제
                </button>
                <% } else { %>
                <button onclick="location.href='/updatePwPage.do'" class="boxed-btn form__button update_pwButton">암호수정
                </button>
                <button onclick="deleteUser()" class="boxed-btn form__button delete_userButton">
                    계정삭제
                </button>
                <% } %>

            </div>
        </div>

        <div class="container" style="padding-top: 60px">
            <h3 class="mb-30">Score</h3>

            <div class="blog-author">
                <div class="media align-items-center">
                    <canvas id="myChart" width="1100" height="500"></canvas>
                    <script src="${pageContext.request.contextPath}/resources/js/chart.min.js"></script>
                    <script>
                        const labels = [
                            'Sat',
                            'Mon',
                            'Tue',
                            'Wed',
                            'Thu',
                            'Fri',
                            'Sun'
                        ];

                        const data = {
                            labels: labels,
                            datasets: [{
                                label: 'My Score',
                                backgroundColor: 'rgb(255, 99, 132)',
                                borderColor: 'rgb(255, 99, 132)',
                                data: [0, 10, 5, 2, 20, 30, 45],
                            }]
                        };

                        const config = {
                            type: 'line',
                            data,
                            options: {}
                        };
                        // === include 'setup' then 'config' above ===

                        let myChart = new Chart(
                            document.getElementById('myChart'),
                            config
                        );
                    </script>
                </div>
            </div>

        </div>
    </section>

</main>

<!-- footer&Scroll Up start -->
<%@include file="/WEB-INF/view/inc/footer.jsp" %>
<!-- footer&Scroll Up end -->

<!-- js file start -->
<%@include file="/WEB-INF/view/inc/jsfile.jsp" %>
<!-- js file end -->

<script>
    function deleteUser() {
        let check = confirm("정말로 삭제하시겠습니까?");

        if (check) {
            location.href = "/deleteUser.do";
        }
    }
</script>

</body>
</html>