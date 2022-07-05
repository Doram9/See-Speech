<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <!-- login head Start -->
    <%@include file="/WEB-INF/view/inc/loginhead.jsp" %>
    <!-- login head end -->

    <script>
        function googleLoginInit() {
            gapi.load('auth2', function () {
                /* Ready. Make a call to gapi.auth2.init or some other API */
                let gAuth = gapi.auth2.init({
                    client_id: '672099978957-s43vd5sfmed6nev135tr6huc4rvcege9.apps.googleusercontent.com'
                });

                gAuth.then(function () {
                    gAuth.signIn().then(function () {
                        let profile = gAuth.currentUser.get().getBasicProfile();

                        $.ajax({
                            url: '/login/loginGoogleForAJAX.do',
                            type: 'post',
                            data: {
                                "id" : profile.getId(),
                                "username": profile.getName(),
                                "email": profile.getEmail()
                            },
                            success: function () {
                                location.href="/index.do";
                            }
                        });
                    });

                }, function (error) {
                    alert(JSON.stringify(error, undefined, 2));
                })
            });
        }
    </script>

</head>
<body>
<!-- Preloader Start -->
<%@include file="/WEB-INF/view/inc/preloader.jsp" %>
<!-- Preloader end -->

<div class="container-login100">
    <div class="wrap-login100 p-l-55 p-r-55 p-t-80 p-b-30">
        <form class="login100-form validate-form" action="login.do" method="POST">
            <span class="login100-form-title p-b-37">
                Sign In
            </span>

            <div class="wrap-input100 validate-input m-b-20" data-validate="Enter email">
                <input class="input100" type="email" name="user_email" placeholder="email" autocomplete="off">
                <span class="focus-input100"></span>
            </div>

            <div class="wrap-input100 validate-input m-b-25" data-validate="Enter password">
                <input class="input100" type="password" name="user_pw" placeholder="password">
                <span class="focus-input100"></span>
            </div>

            <div class="container-login100-form-btn">
                <button class="login100-form-btn">
                    Sign In
                </button>
            </div>
            <div class="text-center p-t-57 p-b-20">
					<span class="txt1">
						Or login with
					</span>
            </div>

            <div class="flex-c p-b-112" style="padding-bottom: 90px;">

                <a href="#" id="google_login" class="login100-social-item" onclick="googleLoginInit()">
                    <img src="${pageContext.request.contextPath}/resources/img/icon/icon-google.png" alt="GOOGLE">
                </a>


            </div>

            <div class="text-center">
                <a href="register.do" id="loginBtn" class="txt2 hov1">
                    Sign Up
                </a>
                <div>/</div>
                <a href="findPwPage.do" class="txt2 hov1">
                    Forgot Password?
                </a>
            </div>
        </form>
    </div>
</div>

<!-- login js Start -->
<%@include file="/WEB-INF/view/inc/loginjs.jsp" %>
<!-- login js end -->

<script src="https://apis.google.com/js/platform.js" async defer></script>

</body>
</html>