# Используем официальный образ Python
FROM python:3.9-slim

# Устанавливаем необходимые библиотеки
RUN pip install requests

# Копируем скрипты
COPY wait-for-it.sh /wait-for-it.sh
COPY post_requests.py /post_requests.py

# Делаем wait-for-it.sh исполнимым
RUN chmod +x /wait-for-it.sh

# Проверяем формат файла
RUN apt-get update && apt-get install -y dos2unix && dos2unix /wait-for-it.sh

# Определяем команду для выполнения скрипта, используя wait-for-it.sh для ожидания backend
CMD ["/wait-for-it.sh", "newspaper-backend:8081", "--", "python", "/post_requests.py"]
