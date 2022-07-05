<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<input id="quizListHiddenInput" name="quizListHiddenInput" type="hidden"/>

<div class="progress-table-wrap">
    <div class="progress-table" style="padding-bottom: 15px;">
        <div class="table-head">
            <div class="serial">NO</div>
            <div class="country">QUIZ</div>
            <div class="percentage">Button</div>
        </div>

        <% for (String quizContOne : rUserQuizContList) { %>

        <div class="table-row">
            <div class="serial quizContNo"></div>
            <div class="country quizList"><%=quizContOne%></div>
            <div class="percentage">
                <button onclick="updateClickHandler(this)" type="button"
                        class="boxed-btn list_button update_button">수정
                </button>
                <button onclick="deleteClickHandler(this)" type="button"
                        class="boxed-btn list_button delete_button">삭제
                </button>
            </div>
        </div>

        <% } %>

        <div class="table-row plus_table_row" onclick="createQuizHandler()">
            <div class="serial"></div>
            <div class="country">문제 추가하기</div>
            <div class="percentage">
            </div>
        </div>
    </div>
</div>

<div id="table_button_box">
    <button type="submit" class="boxed-btn practice_button">저장</button>
    <button type="button" class="boxed-btn history_button" onclick="history.back()">이전</button>
</div>
