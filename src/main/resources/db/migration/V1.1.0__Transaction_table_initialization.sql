CREATE TABLE transaction
(
    id UUID NOT NULL,
    from_account_id UUID NOT NULL,
    to_account_id UUID NOT NULL,
    amount DECIMAL NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    CONSTRAINT pk_transaction PRIMARY KEY (id)
);

ALTER TABLE transaction
    ADD CONSTRAINT FK_TRANSACTION_ON_FROM_ACCOUNT FOREIGN KEY (from_account_id) REFERENCES account (id);

ALTER TABLE transaction
    ADD CONSTRAINT FK_TRANSACTION_ON_TO_ACCOUNT FOREIGN KEY (to_account_id) REFERENCES account (id);