<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="properties pb-20">
    <div class="properties__card">
        <div class="properties__img overlay1">
            <img src="${pageContext.request.contextPath}/resources/img/gallery/featured<%=(i%6)+1%>.png"
                 alt="">
        </div>
        <div class="properties__caption">
            <h3><%=rMap.get("quiz_title") %>
            </h3>
            <div class="properties__footer d-flex justify-content-between align-items-center">
            </div>
            <a href="listPage.do?quizTitle=<%=rMap.get("quiz_title") %>&quizSort=<%=rMap.get("quiz_sort") %>"
               class="border-btn border-btn2 cursor_pointer">학습하기</a>
        </div>
    </div>
</div>
