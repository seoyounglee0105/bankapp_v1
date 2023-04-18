-- 트랜잭션 처리해보기

START TRANSACTION;
-- 일련의 작업 지정

INSERT INTO user_tb(username, password, fullname)
VALUES ('john', '1234', 'kim');

INSERT INTO user_tb(username, password, fullname)
VALUES ('mike', '222', 'lee');

ROLLBACK;

SELECT * FROM user_tb;

-- -----------------
/*
	계좌 간 이체
    계좌 A의 잔액은 3,000원입니다.
    계좌 B의 잔액은 0원입니다.
    목적 : 계좌 A에서 B로 3,000원을 이체하기
*/
-- 테스트 데이터 설정
UPDATE account_tb SET balance = 3000 WHERE id = 1;
UPDATE account_tb SET balance = 0 WHERE id = 2;

START TRANSACTION;
UPDATE account_tb SET balance = balance - 3000 WHERE id = 1;
UPDATE account_tb SET balance = balance + 3000 WHERE id = 2;

INSERT INTO history_tb(amount, w_balance, d_balance, w_account_id, d_account_id)
VALUES (3000, 
				  (SELECT balance FROM account_tb WHERE id = 1), 
				  (SELECT balance FROM account_tb WHERE id = 2), 
				  1, 2);


COMMIT;

SELECT * FROM history_tb;


