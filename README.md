Лабораторная задача 4

Вариант 1: Приложение "Путеводитель по достопримечательностям"
Функциональные требования:
Приложение должно позволять пользователю добавлять и просматривать достопримечательности, указывая название, описание, город, адрес и тип (например, музей, парк, памятник).
Пользователь должен иметь возможность отмечать посещенные достопримечательности и добавлять личные заметки об их посещении.
Учесть, что много пользователей может хранить свои личные предпочтения и заметки и спроектировать схему БД в соответствии с этим  (3 таблицы должно быть достаточно, отношение)
SQLite База данных с таблицами:
для хранения информации о достопримечательностях с полями title, description, location, type 
для хранения личных заметок о посещении и статус посещения , visited, notes
таблица для связи между достопримечательностями и личными заметками для каждого пользователя
Приложение должно использовать локальный ContentProvider для взаимодействия с SQLite Базой данных
Room фреймворк должен быть использован для интеграции с Базой Данных SQLite
Корутины: должны быть использованы для выполнения асинхронных операции по сохранению и обновлению данных в базе.
SOLID: Приложение должно быть построено с разделением ответственности между слоями модели, представления и данных.
DI (Koin) фреймворк внедрение зависимостей использован для управления данными и бизнес-логикой.
MVVM Архитектурный шаблон должен быть использован. ViewModel должна использоваться для управления списком достопримечательностей и их состоянием.
Kotlin Flow должен обеспечивать реактивное обновление интерфейса при изменении списка достопримечательностей.
