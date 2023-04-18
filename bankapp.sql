CREATE DATABASE bank;
USE bank;

-- 사용자 정보
CREATE TABLE user_tb (
	id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(30) NOT NULL,
    fullname VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- 계좌 정보
CREATE TABLE account_tb ( -- 테이블은 모델 객체라고 함 ..? 
	id INT AUTO_INCREMENT PRIMARY KEY,
    number VARCHAR(30) NOT NULL UNIQUE COMMENT '계좌 번호', 
    password VARCHAR(20) NOT NULL,
    balance BIGINT NOT NULL COMMENT '계좌 잔액', -- COMMENT : 테이블에 주석을 포함시킴
	user_id INT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- 계좌 입출금 내역 정보 (history)
CREATE TABLE history_tb (
	id INT AUTO_INCREMENT PRIMARY KEY COMMENT '거래 내역 ID',
    amount BIGINT NOT NULL COMMENT '거래 금액',
    w_account_id INT COMMENT '출금 계좌 ID',
    d_account_id INT COMMENT '입금 계좌 ID',
    w_balance BIGINT COMMENT '출금 요청된 계좌의 잔액',
    d_balance BIGINT COMMENT '입금 요청된 계좌의 잔액',
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- 유저 샘플 데이터
INSERT INTO user_tb(username, password, fullname, created_at) values('길동', '1234', '고', now());
INSERT INTO user_tb(username, password, fullname, created_at) values('둘리', '1234', '애기공룡', now());
INSERT INTO user_tb(username, password, fullname, created_at) values('콜', '1234', '마이', now());

-- 계좌 샘플 데이터
INSERT INTO account_tb(number, password, balance, user_id, created_at) values('1111', '1234', 900, 1, now());
INSERT INTO account_tb(number, password, balance, user_id, created_at) values('2222', '1234', 1100, 2, now());
INSERT INTO account_tb(number, password, balance, user_id, created_at) values('333', '1234', 0, 3, now());

-- 히스토리 추적 내용 (이체, 입금, 출금)
-- 히스토리 테이블에 이체 내역 기록 (ex. 1번 계좌가 2번 계좌에 100원을 이체)
SELECT * FROM account_tb;
INSERT INTO history_tb (amount, w_balance, d_balance, w_account_id, d_account_id, created_at)
VALUES (100, 900, 1100, 1, 2, NOW());

-- 히스토리 테이블에 출금 내역 기록 (ex. 1번 계좌에서 100원 출금)
INSERT INTO history_tb (amount, w_balance, d_balance, w_account_id, d_account_id, created_at)
VALUES (100, 800, null, 1, null, NOW());

-- 히스토리 테이블에 입금 내역 기록 (ex. 1번 계쫘에서 500원 입금)
INSERT INTO history_tb (amount, w_balance, d_balance, w_account_id, d_account_id, created_at)
VALUES (500, null, 700, null, 1, NOW());

SELECT * FROM history_tb;

-- DELETE 해서 번호가 2 이상부터 시작할 때 1부터 시작하도록 초기화
ALTER TABLE history_tb AUTO_INCREMENT = 1;

-- 출금 내역 (3번이 1000원 출금, 잔액 0원인 상태를 거래 내역 기록)
INSERT INTO history_tb (amount, w_balance, d_balance, w_account_id, d_account_id, created_at)
VALUES (1000, 1000, null, 3, null, NOW());

SELECT * FROM user_tb;
SELECT * FROM history_tb;
SELECT * FROM account_tb;

SELECT * FROM history_tb AS h
LEFT JOIN user_tb AS u
ON h.w_account_id;


