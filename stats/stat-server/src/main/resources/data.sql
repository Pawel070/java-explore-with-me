-- очистка настройка и загрузка базы первичными данными

DELETE FROM stats WHERE stats.id > 0;
ALTER TABLE stats ALTER COLUMN id RESTART WITH 1;
