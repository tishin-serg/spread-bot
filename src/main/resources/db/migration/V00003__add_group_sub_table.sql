DROP TABLE IF EXISTS group_sub;
DROP TABLE IF EXISTS group_x_user;

CREATE TABLE IF NOT EXISTS group_sub (
	id bigserial PRIMARY KEY,
	tittle varchar NOT NULL,
	service varchar NOT NULL,
	country varchar NOT NULL,
	currency_from varchar NOT NULL,
	currency_to varchar NOT NULL,
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

INSERT INTO group_sub (tittle, service,country,currency_from,currency_to) VALUES
                ('Юнистрим - Перевод в Грузию - USD/RUB', 'unistream','GEO','USD','RUB'),
                ('Юнистрим - Перевод в Грузию - GEL/RUB', 'unistream','GEO','GEL','RUB'),
                ('Юнистрим - Перевод в Грузию - EUR/RUB', 'unistream','GEO','EUR','RUB');







