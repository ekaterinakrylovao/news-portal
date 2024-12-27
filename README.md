# Newspaper Project

## Описание проекта

Этот проект представляет собой новостной портал с возможностью работы с учётными записями пользователей, публикацией статей, добавлением комментариев и поддержкой функционала лайков. Он разработан с использованием современных технологий и архитектурных подходов.

---

## Стек технологий

- **Backend**: Java, Spring Boot (Maven)
- **Frontend**: Vue.js
- **База данных**: PostgreSQL
- **Аутентификация**: JWT
- **Интеграция с контейнерами**: Docker (Docker Compose)
- **ORM**: Hibernate (JPA)
- **Управление изображениями**: Файловая система с доступом через контроллер

---

## Запуск проекта

### Предварительная настройка

Перед запуском измените значения на свои в файлах `\backend\src\main\resources\application.properties` и `docker-compose.yml` и `\backend\docker-compose.yml`.

Также, требуется выполнить следующее, чтобы у нас была `\target` для работы `Dockerfile`:
```bash
cd backend
mvn clean package -DskipTests
```

### Запуск

1. Перейдите в директорию проекта:
   ```bash
   cd path/to/project
   ```
2. Запустите Docker Compose:
   ```bash
   docker-compose up --build
   ```

Проект автоматически настроит все зависимости, включая базу данных и бэкенд-сервис.

Переходите на http://localhost:5173, чтобы увидеть сайт в работе.

---

## Работа с изображениями

Реализация загрузки изображений позволяет пользователям загружать и просматривать изображения, связанные с новостными статьями.  
Ключевые функции:
- Загрузка изображения через `POST /images/upload/{articleId}`
- Просмотр изображения через `GET /images/{filename}`

Пример запроса загрузки изображения будет приведён в разделе посвящённому тестированию с Postman.

Контроллер для управления изображениями реализован в файле `ImageController.java`.

---

## Схема базы данных

![news_portal](https://github.com/user-attachments/assets/2eea86e4-4902-4365-a332-127efe30e5ff)

---

## Взаимодействие с базой данных

Для работы с базой данных я предпочла использование командной строки через Docker. Опишу шаги для использования:

1. **Подключение к контейнеру с PostgreSQL**
   ```bash
   docker exec -it newspaper-db bash
   ```
   Замените `newspaper-db` на имя вашего контейнера с базой данных (если оно отличается).
2. **Запустите `psql`**
   ```bash
   psql -U postgres -d news_portal
   ```
   Это подключит вас к базе данных `news_portal` с пользователем `postgres` (опять же, заменить значения, если они отличаются).
3. **Работа с базой данных**
   Теперь вы можете выполнять SQL-запросы и просматривать данные в базе через `psql`.

   Примеры:

   - Список таблиц:
      ```bash
      \dt
      ```
   - Просмотр всех записей в таблице:
      ```bash
      SELECT * FROM table_name;
      ```
   - Изменить значения в таблице (пример использования в текущем проекте):
      ```bash
      UPDATE articles
      SET title = 'Новый заголовок', content = 'Обновлённое содержание'
      WHERE id = 1;
      ```
   - Удалить строку в таблице (пример использования в текущем проекте):
      ```bash
      DELETE FROM articles
      WHERE id = 2;
      ```
4. **Выход из `psql` и контейнера**
   ```bash
   \q
   ```
   Совершит выход из `psql`.
   ```bash
   exit
   ```
   Выйдет из контейнера.

---

## Тестирование в Postman

Для проверки функциональности была разработана коллекция запросов в Postman. Основные запросы:
- Регистрация пользователя: `POST /auth/register`

![Снимок экрана 2024-12-26 153918](https://github.com/user-attachments/assets/fa890883-7393-445d-a3c1-94badc51664f)

- Авторизация пользователя: `POST /auth/login`

![Снимок экрана 2024-12-26 154004](https://github.com/user-attachments/assets/accd5d89-7554-4541-971d-81f3923a0694)

- Получение новостей: `GET /articles`

![Снимок экрана 2024-12-27 043722](https://github.com/user-attachments/assets/bc1d2dcf-d64d-4b0e-bc9e-2a6bbabb5661)

- Создание новости: `POST /articles`

![Снимок экрана 2024-12-26 154252](https://github.com/user-attachments/assets/9e135166-6b86-45a0-82d7-63ef99d0d505)

- Получение комментариев для статьи: `GET /comments/article/{articleId}`

![Снимок экрана 2024-12-27 043445](https://github.com/user-attachments/assets/2739c27e-08ce-401e-a997-6cf778e6dedf)

- Добавление комментария: `POST /comments?articleId={articleId}`

![Снимок экрана 2024-12-26 172254](https://github.com/user-attachments/assets/29df8a56-3f7b-49ce-be9a-1449fcd2a1d1)

- Добавление лайка: `POST /likes/article/{articleId}`

![Снимок экрана 2024-12-26 174609](https://github.com/user-attachments/assets/b414d53c-7b84-4e7e-840b-b6dbe6e7a3eb)

- Удаление лайка: `DELETE /likes/article/{articleId}`

![Снимок экрана 2024-12-26 172428](https://github.com/user-attachments/assets/531f6bdc-cedf-45e3-bc33-ffae7d61d772)

- Получение количества лайков: `GET /likes/article/{articleId}/count`

![Снимок экрана 2024-12-26 172502](https://github.com/user-attachments/assets/ad7f52c9-950a-4d7f-94d4-de2463580e0e)

- Лайкнул ли пользователь?: `GET /likes/article/{articleId}/user`

![Снимок экрана 2024-12-26 172515](https://github.com/user-attachments/assets/40a90d2e-5b5a-481f-b27e-d2b6a8990268)

- Загрузка картинок: `POST /images/upload/{articleId}`

![Снимок экрана 2024-12-26 172535](https://github.com/user-attachments/assets/06b0c581-bc61-42d6-a901-af22c586396e)

- Получение картинки: `GET /images/{filename}`

![Снимок экрана 2024-12-26 172631](https://github.com/user-attachments/assets/539a9103-00bd-47fb-b943-b2a71e9f88fa)

Каждый запрос сопровождается примерами использования и ожидаемыми результатами.

---

## Фишки проекта

1. **Поддержка изображений**:
    - Изображения загружаются и хранятся в файловой системе контейнера.
    - Обеспечивается быстрый доступ через API.

2. **Ожидание базы данных**:
    - Используется скрипт `wait-for-it.sh` для синхронизации запуска базы данных и бэкенда.

3. **Полностью контейнеризированный проект**:
    - Локальная среда полностью изолирована с использованием Docker.

---

## Как запустить тесты

Для запуска тестов используйте команду:
```bash
mvn test
```
Все тесты, включая юнит-тесты и интеграционные тесты, настроены для проверки основных функций проекта.
