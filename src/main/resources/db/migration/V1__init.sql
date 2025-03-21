CREATE TABLE market_price
(
    symbol     VARCHAR PRIMARY KEY NOT NULL,
    bid_price  NUMERIC(20, 2)      NOT NULL,
    bid_qty    NUMERIC(20, 2)      NOT NULL,
    ask_price  NUMERIC(20, 2)      NOT NULL,
    ask_qty    NUMERIC(20, 2)      NOT NULL,
    updated_at TIMESTAMP           NOT NULL
);

CREATE TABLE client
(
    id         VARCHAR PRIMARY KEY NOT NULL,
    username   VARCHAR             NOT NULL,
    password   VARCHAR             NOT NULL,
    created_at TIMESTAMP           NOT NULL,
    updated_at TIMESTAMP           NOT NULL
);

INSERT INTO client(id, username, password, created_at, updated_at)
VALUES ('client-id', 'demo', '1234', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

CREATE TABLE account
(
    id         VARCHAR PRIMARY KEY NOT NULL,
    client_id  VARCHAR             NOT NULL,
    currency   VARCHAR             NOT NULL,
    balance    NUMERIC(20, 2)      NOT NULL,
    created_at TIMESTAMP           NOT NULL,
    updated_at TIMESTAMP           NOT NULL,
    FOREIGN KEY (client_id) REFERENCES client (id)
);

INSERT INTO account(id, client_id, currency, balance, created_at, updated_at)
VALUES ('account-id', 'client-id', 'USDT', 50000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

CREATE TABLE order_type
(
    id VARCHAR PRIMARY KEY NOT NULL
);

INSERT INTO order_type
VALUES ('MARKET');

CREATE TABLE orders
(
    id         VARCHAR PRIMARY KEY NOT NULL,
    symbol     VARCHAR             NOT NULL,
    side       VARCHAR             NOT NULL,
    type       VARCHAR             NOT NULL,
    status     VARCHAR             NOT NULL,
    qty        NUMERIC(20, 2)      NOT NULL,
    filled_qty NUMERIC(20, 2)      NOT NULL,
    currency   VARCHAR             NOT NULL,
    price      NUMERIC(20, 2),
    client_id  VARCHAR             NOT NULL,
    created_at TIMESTAMP           NOT NULL,
    updated_at TIMESTAMP           NOT NULL,
    FOREIGN KEY (client_id) REFERENCES client (id)
);

CREATE TABLE trade
(
    id         VARCHAR PRIMARY KEY NOT NULL,
    order_id   VARCHAR             NOT NULL,
    symbol     VARCHAR             NOT NULL,
    side       VARCHAR             NOT NULL,
    currency   VARCHAR             NOT NULL,
    status     VARCHAR             NOT NULL,
    price      NUMERIC(20, 2)      NOT NULL,
    qty        NUMERIC(20, 2)      NOT NULL,
    client_id  VARCHAR             NOT NULL,
    created_at TIMESTAMP           NOT NULL,
    updated_at TIMESTAMP           NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders (id),
    FOREIGN KEY (client_id) REFERENCES client (id)
);

CREATE TABLE position
(
    client_id VARCHAR        NOT NULL,
    symbol    VARCHAR        NOT NULL,
    currency  VARCHAR        NOT NULL,
    qty       NUMERIC(20, 2) NOT NULL,
    amt       NUMERIC(20, 2) NOT NULL,
    PRIMARY KEY (client_id, symbol, currency),
    FOREIGN KEY (client_id) REFERENCES client (id)
);