# Используем более новую версию Node.js
FROM node:18 AS build

# Указываем рабочую директорию
WORKDIR /app

# Копируем файлы проекта
COPY package*.json ./
RUN npm install
COPY . .

# Сборка проекта
RUN npm run build

# Используем Nginx для обслуживания
FROM nginx:alpine

# Копируем собранные файлы в Nginx
COPY --from=build /app/dist /usr/share/nginx/html

# Открываем порт 80
EXPOSE 80

# Запуск Nginx
CMD ["nginx", "-g", "daemon off;"]
