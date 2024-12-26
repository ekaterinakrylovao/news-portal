# Используем официальный образ с JDK
FROM openjdk:17-jdk-slim

# Указываем рабочую директорию
WORKDIR /app

# Копируем файлы проекта в контейнер
COPY target/*.jar app.jar

# Копируем скрипт wait-for-it.sh в контейнер
COPY wait-for-it.sh /wait-for-it.sh

# Делаем скрипт исполнимым
RUN chmod +x /wait-for-it.sh

# Создаем директорию для загрузки изображений
RUN mkdir -p /uploads && chmod 777 /uploads

# Указываем команду для запуска приложения с ожиданием базы данных
ENTRYPOINT ["/wait-for-it.sh", "newspaper-db:5432", "--", "java", "-jar", "/app/app.jar"]

# Открываем порт 8081 для доступа
EXPOSE 8081
