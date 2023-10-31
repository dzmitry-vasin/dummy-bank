CREATE TABLE client
(
    id UUID NOT NULL,
    external_id VARCHAR(255) NOT NULL,
    CONSTRAINT pk_client PRIMARY KEY (id)
);

CREATE TABLE account
(
    id UUID NOT NULL,
    client_id UUID NOT NULL,
    external_id BIGINT NOT NULL,
    balance DECIMAL NOT NULL,
    CONSTRAINT pk_account PRIMARY KEY (id)
);

ALTER TABLE account ADD CONSTRAINT FK_ACCOUNT_ON_CLIENT FOREIGN KEY (client_id) REFERENCES client (id);
