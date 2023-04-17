<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/layout/header.jsp"%>

<div class="col-sm-8">
	<h2>출금 페이지 (todo 인증 처리)</h2>
	<h5>어서오세요~</h5>
	<div class="bg-light p-md-5 h-75">
		<form action="#" method="post">
			<div class="form-group">
				<label for="amount">출금 금액 :</label> 
				<input type="text" class="form-control" placeholder="출금 금액을 입력하시오." id="amount" name="amount">
			</div>
			<div class="form-group">
				<label for="wAccountNumber">출금 계좌번호 :</label> 
				<input type="text" class="form-control" placeholder="출금 계좌번호를 입력하시오." id="wAccountNumber" name="wAccountNumber">
			</div>
			<div class="form-group">
				<label for="wAccountPassword">계좌 비밀번호 :</label> 
				<input type="password" class="form-control" placeholder="계좌 비밀번호를 입력하시오." id="wAccountPassword" name="wAccountPassword">
			</div>
			
			<button type="submit" class="btn btn-primary">출금</button>
		</form>
	</div>
	<br>
</div>

<%@ include file="/WEB-INF/view/layout/footer.jsp"%>