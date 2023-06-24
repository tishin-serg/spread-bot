DROP TABLE IF EXISTS group_sub;
DROP TABLE IF EXISTS group_x_user;

CREATE TABLE IF NOT EXISTS group_sub (
	id bigserial PRIMARY KEY,
	tittle varchar NOT NULL,
	service varchar NOT NULL,
	request_details_country varchar,
	currency_from varchar NOT NULL,
	currency_to varchar NOT NULL,
	request_details_payment_method varchar,
	request_details_trade_type varchar,
	last_rate numeric(8, 2) null
);

ALTER TABLE tg_user
ADD UNIQUE (chat_id);

CREATE TABLE group_x_user (
   group_sub_id INT NOT NULL,
   user_id VARCHAR(100) NOT NULL,
   FOREIGN KEY (user_id) REFERENCES tg_user(chat_id),
   FOREIGN KEY (group_sub_id) REFERENCES group_sub(id),
   UNIQUE(user_id, group_sub_id)
);

INSERT INTO group_sub (tittle,service,request_details_country,currency_from,currency_to) VALUES
                ('Юнистрим - Перевод в Грузию - USD/RUB', 'unistream','GEO','USD','RUB'),
                ('Юнистрим - Перевод в Грузию - GEL/RUB', 'unistream','GEO','GEL','RUB'),
                ('Юнистрим - Перевод в Грузию - EUR/RUB', 'unistream','GEO','EUR','RUB'),
                ('Юнистрим - Перевод в Турцию - USD/RUB', 'unistream','TUR','USD','RUB'),
                ('Юнистрим - Перевод в Турцию - TRY/RUB', 'unistream','TUR','TRY','RUB'),
                ('Юнистрим - Перевод в Узбекистан - USD/RUB', 'unistream','UZB','USD','RUB'),
                ('Юнистрим - Перевод в Армения - USD/RUB', 'unistream','ARM','USD','RUB'),
                ('Юнистрим - Перевод в Армения - AMD/RUB', 'unistream','ARM','AMD','RUB'),
                ('Юнистрим - Перевод в Армения - EUR/RUB', 'unistream','ARM','EUR','RUB'),
                ('Юнистрим - Перевод в Казахстан - USD/RUB', 'unistream','KAZ','USD','RUB'),
                ('Юнистрим - Перевод в Казахстан - EUR/RUB', 'unistream','KAZ','EUR','RUB'),
                ('Юнистрим - Перевод в Казахстан - KZT/RUB', 'unistream','KAZ','KZT','RUB'),
                ('Юнистрим - Перевод в Молдавию - USD/RUB', 'unistream','MDA','USD','RUB'),
                ('Юнистрим - Перевод в Молдавию - EUR/RUB', 'unistream','MDA','EUR','RUB'),
                ('Юнистрим - Перевод в Таджикистан - USD/RUB', 'unistream','TJK','USD','RUB'),
                ('Юнистрим - Перевод в Таджикистан - EUR/RUB', 'unistream','TJK','EUR','RUB');







