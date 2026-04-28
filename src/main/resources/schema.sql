CREATE TABLE product
(
    id    BIGINT       NOT NULL,
    name  VARCHAR(255) NULL,
    price DECIMAL      NULL,
    CONSTRAINT pk_product PRIMARY KEY (id)
);

INSERT INTO product (id, name, price) VALUES (1, 'Wireless Mouse', 25.99);
INSERT INTO product (id, name, price) VALUES (2, 'Mechanical Keyboard', 89.50);
INSERT INTO product (id, name, price) VALUES (3, 'USB-C Hub', 45.00);
