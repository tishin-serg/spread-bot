-- ensure that the table with this name is removed before creating a new one.
DROP TABLE IF EXISTS binance_rate;

-- Create binance_rate table
CREATE TABLE IF NOT EXISTS binance_rate (
                                     id             bigserial primary key,
                                     currency_from       varchar(255) not null,
                                     currency_to         varchar(255) not null,
                                     rate                numeric(8, 2) not null,
                                     payment_method      varchar(255) not null,
                                     trade_type          varchar(255) not null,
                                     date                timestamp not null
                                     );

INSERT INTO group_sub (tittle,service,currency_from,currency_to,request_details_payment_method,request_details_trade_type) VALUES
                ('Binance p2p Тинькофф - Тейкер - USDT/RUB', 'binance','USDT','RUB','TinkoffNew','SELL'),
                ('Binance p2p Тинькофф- Мейкер - USDT/RUB', 'binance','USDT','RUB','TinkoffNew','BUY'),
                ('Binance p2p Росбанк - Тейкер - USDT/RUB', 'binance','USDT','RUB','RosBankNew','SELL'),
                ('Binance p2p Росбанк - Мейкер - USDT/RUB', 'binance','USDT','RUB','RosBankNew','BUY'),
                ('Binance p2p Райффайзенбанк - Тейкер - USDT/RUB', 'binance','USDT','RUB','RaiffeisenBank','SELL'),
                ('Binance p2p Райффайзенбанк - Мейкер - USDT/RUB', 'binance','USDT','RUB','RaiffeisenBank','BUY');