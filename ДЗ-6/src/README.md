###Чек-лист проверок API сервиса продуктов

| Проверка | Ожидаемый результат | Модуль |
| --- | --- | --- |
| Получение категории по валидному id | Получено название категории | CategoryTests |
| Получение категории по невалидному id | Ошибка 404 Not Found | CategoryTests |
| Создание продукта с валидными данными | Продукт создан | ProductCreateTestsPositive |
| Создание продукта с непустым id | Ошибка 400 Bad Request | ProductCreateTestsNegative |
| Создание продукта с пустым заголовком | Ошибка 400 Bad Request | ProductCreateTestsNegative |
| Создание продукта с пустой ценой | Ошибка 400 Bad Request | ProductCreateTestsNegative |
| Создание продукта с пустой категорией | Ошибка 400 Bad Request | ProductCreateTestsNegative |
| Создание продукта с нулевой ценой | Ошибка 400 Bad Request | ProductCreateTestsNegative |
| Создание продукта с отрицательной ценой | Ошибка 400 Bad Request | ProductCreateTestsNegative |
| Получение всех продуктов | Получен список продуктов | ProductReadTests |
| Получение продукта по валидному id | Получен продукт | ProductReadTests |
| Получение продукта по нулевому id | Ошибка 404 Not Found | ProductReadTests |
| Получение продукта по отрицательному id | Ошибка 404 Not Found | ProductReadTests |
| Модификация продукта валидными данными | Продукт изменен | ProductUpdateTests |
| Модификация продукта с невалидным id | Ошибка 404 Not Found | ProductUpdateTests |
| Удаление названия продукта | Ошибка 400 Bad Request | ProductUpdateTests |
| Удаление цены продукта | Ошибка 400 Bad Request | ProductUpdateTests |
| Удаление категории продукта | Ошибка 400 Bad Request | ProductUpdateTests |
| Обнуление цены продукта | Ошибка 400 Bad Request | ProductUpdateTests |
| Ввод отрицательной цены продукта | Ошибка 400 Bad Request | ProductUpdateTests |
| Удаление продукта с валидным id | Продукт удален | ProductDeleteTests |
| Удаление продукта с невалидным id | Ошибка 404 Not Found | ProductDeleteTests |
