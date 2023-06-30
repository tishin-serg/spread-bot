DROP TABLE IF EXISTS korona_rate;

-- Create korona_rate table
CREATE TABLE IF NOT EXISTS korona_rate (
                                     id             bigserial primary key,
                                     country        varchar(255) not null,
                                     currency_from  varchar(255) not null,
                                     currency_to    varchar(255) not null,
                                     rate           numeric(8, 2) not null,
                                     date           timestamp not null
                                     );

INSERT INTO group_sub (tittle,service,request_details_country,currency_from,currency_to) VALUES
                ('Золотая корона - Перевод в Грузию - USD/RUB', 'koronapay','GEO','840','810'),
                ('Золотая корона - Перевод в Грузию - GEL/RUB', 'koronapay','GEO','981','810'),
                ('Золотая корона - Перевод в Грузию - EUR/RUB', 'koronapay','GEO','978','810'),
                ('Золотая корона - Перевод в Турцию - USD/RUB', 'koronapay','TUR','840','810'),
                ('Золотая корона - Перевод в Турцию - EUR/RUB', 'koronapay','TUR','978','810'),
                ('Золотая корона - Перевод в Турцию - TRY/RUB', 'koronapay','TUR','949','810'),
                ('Золотая корона - Перевод в Узбекистан - USD/RUB', 'koronapay','UZB','840','810'),
                ('Золотая корона - Перевод в Казахстан - USD/RUB', 'koronapay','KAZ','840','810'),
                ('Золотая корона - Перевод в Молдавию - USD/RUB', 'koronapay','MDA','840','810'),
                ('Золотая корона - Перевод в Молдавию - EUR/RUB', 'koronapay','MDA','978','810'),
                ('Золотая корона - Перевод в Молдавию - MDL/RUB', 'koronapay','MDA','498','810'),
                ('Золотая корона - Перевод в Таджикистан - USD/RUB', 'koronapay','TJK','840','810'),
                ('Золотая корона - Перевод в Кыргызстан - USD/RUB', 'koronapay','KGZ','840','810');