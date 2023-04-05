-- ensure that the table with this name is removed before creating a new one.
DROP TABLE IF EXISTS uni_rate;

-- Create tg_user table
CREATE TABLE IF NOT EXISTS uni_rate (
                                     id             bigserial primary key,
                                     country        varchar(255) not null,
                                     currency       varchar(255) not null,
                                     rate           numeric(8, 2) not null,
                                     date           timestamp not null
                                     );