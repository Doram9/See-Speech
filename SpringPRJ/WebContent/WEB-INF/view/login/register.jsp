<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <!-- login head Start -->
    <%@include file="/WEB-INF/view/inc/loginhead.jsp" %>
    <!-- login head end -->
</head>

<body>
<!-- Preloader Start -->
<%@include file="/WEB-INF/view/inc/preloader.jsp" %>
<!-- Preloader end -->

<div class="container-login100">
    <div class="wrap-login100 p-l-55 p-r-55 p-t-80 p-b-30">
        <form class="login100-form validate-form" action="${pageContext.request.contextPath}/insertUser.do" method="post">
            <span class="login100-form-title p-b-37">
                Sign Up
            </span>

            <div class="wrap-input100 validate-input m-b-20" id="user_email" data-validate="Enter email">
                <input class="input100" type="email" name="user_email" placeholder="email" autocomplete="off"
                       onkeyup="emailCheckHandler(this)">
                <span class="focus-input100"></span>
            </div>
            <div class="wrap-input100 validate-input m-b-20" data-validate="Enter user name">
                <input class="input100" type="text" name="user_name" placeholder="name" autocomplete="off">
                <span class="focus-input100"></span>
            </div>

            <div class="wrap-input100 validate-input m-b-25" data-validate="Enter password">
                <input class="input100" id="user_pw" type="password" name="user_pw" placeholder="password">
                <span class="focus-input100"></span>
            </div>
            <div class="wrap-input100 validate-input m-b-25" data-validate="Enter password check">
                <input class="input100" id="user_pw_check" type="password" name="user_pw_check"
                       placeholder="password check">
                <span class="focus-input100"></span>
            </div>

            <div class="container-login100-form-btn" style="padding-top: 50px;">
                <button type="submit" class="login100-form-btn">
                    Sign Up
                </button>
            </div>

            <div class="text-center" style="padding-top: 20px;">
                <a href="loginPage.do" class="txt2 hov1">
                    Back
                </a>
            </div>
        </form>
    </div>
</div>

<!-- login js Start -->
<%@include file="/WEB-INF/view/inc/loginjs.jsp" %>
<!-- login js end -->

<script>
    function emailCheckHandler(e) {
        console.log("key up")
        let user_email = e.value;

        $.ajax({
            url: "/getUserExistForAJAX.do",
            type: "post",
            dataType: "JSON",
            data: {"user_email": user_email},
            success: function (json) {
                let checkRes = json.res;
                let user_email_node = document.querySelector("#user_email");
                // console.log(json);

                if (checkRes === 'exist') {
                    user_email_node.classList.add("exist_user");
                    user_email_node.setAttribute("data-validate", "Email Already Exists!!")
                } else if (checkRes === 'exception') {
                    alert("오류입니다. 잠시 후 다시 시도해주세요.");
                    user_email_node.setAttribute("data-validate", "Enter email");
                } else {
                    user_email_node.classList.remove("exist_user");
                    user_email_node.setAttribute("data-validate", "Enter email");
                }
            }
        })
    }
</script>

</body>
</html>