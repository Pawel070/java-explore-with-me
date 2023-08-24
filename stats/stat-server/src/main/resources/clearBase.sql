-- очистка базы без удаления таблиц и обнуление счетчиков

DELETE FROM stats WHERE stats.id > 0;
ALTER TABLE stats ALTER COLUMN id RESTART WITH 1;

