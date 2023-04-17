<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/layout/header.jsp"%>

<div class="col-sm-8">
	<h2>회원 가입</h2>
	<h5>어서오세요~</h5>
	<div class="bg-light p-md-5 h-75">
		<form action="/user/sign-up" method="post">
			<div class="form-group">
				<label for="username">User name :</label> 
				<input type="text" class="form-control" placeholder="Enter username" id="username" name="username">
			</div>
			<div class="form-group">
				<label for="password">Password:</label> 
				<input type="password" class="form-control" placeholder="Enter password" id="password" name="password">
			</div>
			<div class="form-group">
				<label for="fullname">Full name :</label> 
				<input type="text" class="form-control" placeholder="Enter fullname" id="fullname" name="fullname">
			</div>
			<button type="submit" class="btn btn-primary">Submit</button>
		</form>
	</div>
	<br>
</div>

<%@ include file="/WEB-INF/view/layout/footer.jsp"%>