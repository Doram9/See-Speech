<%@ page import="poly.util.CmmUtil" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String SS_USER_EMAIL = CmmUtil.nvl((String) session.getAttribute("SS_USER_EMAIL"));
%>
<header>
    <div class="header-area header-transparent">
        <div class="main-header ">
            <div class="header-bottom  header-sticky">
                <div class="container-fluid">
                    <div class="row align-items-center">
                        <!-- Logo -->
                        <div class="col-xl-2 col-lg-2">
                            <div class="logo">
                                <a href="index.do"><img
                                        src="${pageContext.request.contextPath}/resources/img/logo/snslogo.png" alt=""></a>
                            </div>
                        </div>
                        <div class="col-xl-10 col-lg-10">
                            <div class="menu-wrapper d-flex align-items-center justify-content-end">
                                <!-- Main-menu -->
                                <div class="main-menu d-none d-lg-block">
                                    <nav>
                                        <ul id="navigation">
                                            <li class="active"><a href="index.do">Home</a></li>
                                            <li class="active"><a href="userPage.do">User</a></li>
                                            <li class="active"><a href="rank.do">Rank</a></li>
                                            <!-- Button -->
                                            <% if (SS_USER_EMAIL.equals("")) { %>
                                            <li class="button-header"><a href="loginPage.do" class="btn btn3">Log in</a>
                                            </li>
                                            <% } else { %>
                                            <li class="button-header"><a href="logout.do" class="btn btn3">Log out</a>
                                            </li>
                                            <% } %>
                                        </ul>
                                    </nav>
                                </div>
                            </div>
                        </div>
                        <!-- Mobile Menu -->
                        <div class="col-12">
                            <div class="mobile_menu d-block d-lg-none"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</header>