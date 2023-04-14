CREATE TABLE user_tb (
	id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(30) NOT NULL,
    fullname VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE account_tb ( -- 테이블은 모델 객체라고 함 ..??
	id INT AUTO_INCREMENT PRIMARY KEY,
    number VARCHAR(30) NOT NULL UNIQUE COMMENT '계좌 번호', 
    password VARCHAR(20) NOT NULL,
    balance BIGINT NOT NULL COMMENT '계좌 잔액', -- COMMENT : 테이블에 주석을 포함시킴
	user_id INT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE history_tb (
	id INT AUTO_INCREMENT PRIMARY KEY COMMENT '거래 내역 ID',
    amount BIGINT NOT NULL COMMENT '거래 금액',
    w_account_id INT COMMENT '출금 계좌 ID',
    d_account_id INT COMMENT '입금 계좌 ID',
    w_balance BIGINT COMMENT '출금 요청된 계좌의 잔액',
    d_balance BIGINT COMMENT '입금 요청된 계좌의 잔액',
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);