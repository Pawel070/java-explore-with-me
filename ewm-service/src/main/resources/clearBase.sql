-- очистка базы без удаления таблиц и обнуление счетчиков

DELETE FROM users WHERE users.id > 0;
ALTER TABLE users ALTER COLUMN id RESTART WITH 1;
DELETE FROM categories WHERE categories.id > 0;
ALTER TABLE categories ALTER COLUMN id RESTART WITH 1;
DELETE FROM locations WHERE locations.id > 0;
ALTER TABLE locations ALTER COLUMN id RESTART WITH 1;
DELETE FROM events WHERE events.id > 0;
ALTER TABLE events ALTER COLUMN id RESTART WITH 1;
DELETE FROM requests WHERE requests.id > 0;
ALTER TABLE requests ALTER COLUMN id RESTART WITH 1;
DELETE FROM compilations WHERE compilations.id > 0;
ALTER TABLE compilations ALTER COLUMN id RESTART WITH 1;
DELETE FROM events_compilations WHERE event_id.id > 0;
ALTER TABLE events_compilations ALTER COLUMN event_id RESTART WITH 1;

