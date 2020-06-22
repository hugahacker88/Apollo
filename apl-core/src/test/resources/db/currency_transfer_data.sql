delete from CURRENCY_TRANSFER;

INSERT INTO CURRENCY_TRANSFER
(DB_ID, ID,                 CURRENCY_ID,            SENDER_ID,          RECIPIENT_ID,           UNITS,      TIMESTAMP, HEIGHT) VALUES
(1,    -3137476791973044178, 4218591999071029966, -7396849795322372927, -7099351498114210634, 100000000000, 45754723, 615771),
(2,    -5531815795332533947, -5453448652141572559, -208393164898941117, 9211698109297098287,    100,        59691252, 1400759),
(3,    -4785197605511459631, -5127181510543094263, -7396849795322372927, -208393164898941117,    200,        59691265, 1400761),
(4,    5100021336113941260, -1742414430179786871, -208393164898941117, 9211698109297098287,     300,        59691275, 1400763)
;