DROP TABLE IF EXISTS TRANSACTIONS CASCADE;
DROP TABLE IF EXISTS ACCOUNTS CASCADE;


CREATE TABLE ACCOUNTS
(
    id_account     INT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(250) NOT NULL,
    balance        NUMERIC(10, 4),
    currency       VARCHAR(3)
);

CREATE TABLE TRANSACTIONS
(
    id_transaction     INT AUTO_INCREMENT PRIMARY KEY,
    account_origin     INT NOT NULL,
    account_destiny    INT NOT NULL,
    amount_transaction NUMERIC(10, 4),
    tax_transaction    NUMERIC(10, 4),
    description        VARCHAR(5000),
    date_transaction   DATE
);

INSERT INTO ACCOUNTS (account_number, balance, currency)
VALUES ('123456', 1000, 'USD'),
       ('789012', 200, 'USD');