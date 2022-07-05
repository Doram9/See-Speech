<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <!-- head Start -->
    <%@include file="/WEB-INF/view/inc/head.jsp" %>
    <!-- head end -->

    <!-- chat CSS -->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/chatcore.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/chatstyle.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/page/userChat.css">
</head>

<body>
<!-- Preloader Start -->
<%@include file="/WEB-INF/view/inc/preloader.jsp" %>
<!-- Preloader end -->

<!-- header start -->
<%@include file="/WEB-INF/view/inc/header.jsp" %>
<!-- header end -->

<main style="overflow-x:hidden">
    <!-- slider Area Start-->
    <%@include file="/WEB-INF/view/inc/section.jsp" %>
    <!-- slider Area end-->

    <div class="main-container" style="padding-top: 0;">
        <div class="pd-ltr-20 xs-pd-20-10">
            <div class="min-height-200px">
                <div class="bg-white border-radius-4 box-shadow mb-30">
                    <div class="row no-gutters">
                        <div class="col-lg-9 col-md-8 col-sm-12">
                            <div class="chat-detail">
                                <div class="chat-profile-header clearfix">
                                    <div class="left">
                                        <div class="clearfix">
                                            <div class="chat-profile-name chat_head_box">
                                                <i class="fas fa-angle-left fa-2x" style="cursor: pointer;"
                                                   onclick="history.back();closeSocket();"></i>
                                                <h3>단체 채팅</h3>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="chat-box">
                                    <div class="chat-desc customscroll">
                                        <ul id="chat__ul">

                                        </ul>
                                    </div>
                                    <div class="chat-footer">
                                        <div id="micButtonBox"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
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

<!-- chat js -->
<script src="${pageContext.request.contextPath}/resources/js/chatcore.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/chatscript.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/chatlayout-settings.js"></script>

<!-- annyang js & speechkitt -->
<script src="${pageContext.request.contextPath}/resources/js/annyang.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/speechkitt.js"></script>

<script type="text/javascript">
    function getDatetime() {
        let _date = new Date();
        let _hours = _date.getHours();
        let _min = _date.getMinutes();
        _hours = _hours < 10 ? '0' + _hours : _hours
        _min = _min < 10 ? '0' + _min : _min
        return _hours + ':' + _min;
    }

    /* ##### websocket js start ##### */
    let ws;

    function openSocket() {
        console.log("openSocket");
        if (ws !== undefined && ws.readyState !== WebSocket.CLOSED) {
            console.log("WebSocket is already opened.");
            return;
        }
        //웹소켓 객체 만드는 코드
        ws = new WebSocket("wss://joinspeech.com/echo.do");

        ws.onopen = function (event) {
            if (event.data === undefined)
                return;

            console.log(event.data);
        };
        ws.onmessage = function (event) {
            console.log("onmessage: " + event.data);
            let msg = event.data;
            let splitMsg = msg.split(",");
            let msgNum = splitMsg[0]
            let msgText = splitMsg[1];

            if (msgNum === '0') {
                let resHTML = '<li class="user_come_msg_container"><div class="user_come_msg_div">' + msgText + '</div</li>';
                $("#chat__ul").append(resHTML);
            } else {
                let msg_list = msg.split(":")
                let user_email = msg_list[0]
                let result_msg = msg_list[1]
                if (user_email !== '${SS_USER_EMAIL}') {
                    writeResponse(result_msg);
                }
            }
        };
        ws.onclose = function (event) {
            console.log(event);
            console.log("Connection closed");
        };
    }

    function send(msg) {
        ws.send(msg);
    }

    function closeSocket() {
        ws.close();
    }

    function writeResponse(text) {
        let _dateTime = getDatetime();

        // console.log(json.res_msg);
        let resHTML = "";
        resHTML += '<li class="clearfix">';
        resHTML += '<span class="chat-img">';
        resHTML += '<img src="${pageContext.request.contextPath}/resources/img/chat/basicUser.png">';
        resHTML += '</span>';
        resHTML += '<div class="chat-body clearfix">';
        resHTML += '<p>' + text + '</p>';
        resHTML += '<div class="chat_time">' + _dateTime + '</div>';
        resHTML += '</div>';
        resHTML += '</li>';
        $("#chat__ul").append(resHTML);
    }

    openSocket(); // websocket open
    window.onbeforeunload = function (e) {
        closeSocket() // 페이지 벗어나면 websocket close
    };
    /* ##### websocket js end ##### */

    /* ##### 음성 인식 Annyang.js start ##### */
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
            console.log("final :" + final_transcript);

            // 사용자 음성 html append
            let dateTime = getDatetime();
            let resHTML = "";
            resHTML += '<li class="clearfix admin_chat">';
            resHTML += '<span class="chat-img">';
            resHTML += '<img src="${pageContext.request.contextPath}/resources/img/chat/chat-img.jpg">';
            resHTML += '</span>';
            resHTML += '<div class="chat-body clearfix">';
            resHTML += '<p>' + final_transcript + '</p>';
            resHTML += '<div class="chat_time">' + dateTime + '</div>';
            resHTML += '</div>';
            resHTML += '</li>';
            $("#chat__ul").append(resHTML);

            let send_msg = '${SS_USER_EMAIL}' + ":" + final_transcript;
            send(send_msg);
        };

        // Tell KITT to use annyang
        SpeechKITT.annyang();

        // Define a stylesheet for KITT to use
        SpeechKITT.setStylesheet('${pageContext.request.contextPath}/resources/css/themes/flat.css');

        // Render KITT's interface
        SpeechKITT.vroom();
    }
    /* ##### 음성 인식 Annyang.js end ##### */
</script>

</body>
</html>
