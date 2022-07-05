<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <!-- head Start -->
    <%@include file="/WEB-INF/view/inc/head.jsp" %>
    <!-- head end -->

    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/page/quizPlay.css">
</head>

<body>
<!-- Preloader Start -->
<%@include file="/WEB-INF/view/inc/preloader.jsp" %>
<!-- Preloader end -->

<!-- right loader start-->
<div id="right-loader-active" style="display: none">
    <div class="right_loader d-flex align-items-center justify-content-center">
        <div class="preloader-inner position-relative">
            <div class="right_loader_icon pere-text">
                <i class="far fa-check-circle fa-3x"></i>
            </div>
        </div>
    </div>
</div>
<!-- right loader end-->

<!-- header start -->
<%@include file="/WEB-INF/view/inc/header.jsp" %>
<!-- header end -->

<main>
    <!-- slider Area Start-->
    <%@include file="/WEB-INF/view/inc/section.jsp" %>
    <!-- slider Area end-->

    <!-- ? services-area -->
    <div class="services-area services-area2 section-padding40">
        <div class="container">

            <div class="row justify-content-sm-center">
                <div class="col-lg-4 col-md-6 col-sm-8">
                    <div class="single-services mb-30">
                        <div class="features-caption">
                            <h3>Speech</h3>
                            <p id="quizBox">사과</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row justify-content-sm-center">
                <div class="col-lg-4 col-md-6 col-sm-8">
                    <div class="single-services mb-30">
                        <div class="features-caption">
                            <h3>See</h3>
                            <p id="sttBox"></p>
                        </div>
                    </div>
                </div>
            </div>

            <div id="micButtonBox" class="row justify-content-sm-center mic_box">
            </div>

            <div class="row justify-content-sm-center" style="margin-bottom: 20px">
                <div class="col-lg-4 col-md-6 col-sm-8 button_container">
                    <button class="boxed-btn play_button next_button" onclick="nextQuizHandler();">Next</button>
                    <button class="boxed-btn play_button" onclick="history.back()">Back</button>
                </div>
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

<!-- annyang js & speechkitt -->
<script src="${pageContext.request.contextPath}/resources/js/annyang.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/speechkitt.js"></script>
<script>
    let WRONG_CNT = 0;
    let TMP_CNT = 0;

    if (annyang) {
        // Add our commands to annyang
        annyang.addCommands({});

        //음성인식 값 받아오기위한 객체 생성
        let recognition = annyang.getSpeechRecognizer();
        //최종 음성인식 결과값 저장 변수
        let final_transcript = "";
        //말하는 동안에 인식되는 값 가져오기(허용)
        recognition.interimResults = false;
        //음성 인식결과 가져오기
        recognition.onresult = function (event) {
            final_transcript = "";
            for (let i = event.resultIndex; i < event.results.length; ++i) {
                if (event.results[i].isFinal) {
                    final_transcript += event.results[i][0].transcript;
                }
            }
            final_transcript = final_transcript.trim();
            console.log("final :" + final_transcript);

            let _quiz = document.getElementById("quizBox").innerHTML;
            let last_word = _quiz.charAt(_quiz.length - 1)
            let first_word = final_transcript.charAt(0);

            if (last_word === first_word) {
                let rightLoader = $('#right-loader-active');
                rightLoader.show();
                rightLoader.delay(450).fadeOut('slow', function () {

                });
                $("#quizBox").text(final_transcript)
                $("#sttBox").text("");
            } else {
                $("#sttBox").text(final_transcript);
            }

        };

        // Tell KITT to use annyang
        SpeechKITT.annyang();

        // Define a stylesheet for KITT to use
        SpeechKITT.setStylesheet('${pageContext.request.contextPath}/resources/css/themes/flat.css');

        // Render KITT's interface
        SpeechKITT.vroom();
    }


</script>

</body>
</html>