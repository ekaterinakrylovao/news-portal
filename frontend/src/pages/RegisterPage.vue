<template>
  <div class="auth-container">
    <div class="auth-box">
      <h2>Регистрация нового пользователя</h2>
      <div class="name-surname">
        <input v-model="name" type="text" placeholder="Имя" />
        <input v-model="surname" type="text" placeholder="Фамилия" />
      </div>
      <input v-model="email" type="email" placeholder="Email" />
      <input v-model="password" type="password" placeholder="Пароль" />
      <button @click="register">Зарегистрироваться</button>
      <p v-if="error" class="error">{{ error }}</p>
      <p v-if="success" class="success">{{ success }}</p>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      name: '',
      surname: '',
      email: '',
      password: '',
      error: '',
      success: '',
    };
  },
  methods: {
    async register() {
      if (!this.name || !this.surname || !this.email || !this.password) {
        this.error = 'Все поля должны быть заполнены.';
        return;
      }

      try {
        const result = await this.$store.dispatch('register', {
          name: this.name,
          surname: this.surname,
          email: this.email,
          password: this.password,
        });
        if (result.success) {
          this.success = 'Регистрация успешна! Перенаправляем...';

          // Явно очищаем заголовок Authorization
          this.$store.commit('CLEAR_TOKEN');

          setTimeout(() => this.$router.push('/login'), 2000);
        } else {
          this.error = result.message || 'Ошибка регистрации. Попробуйте снова.';
        }
      } catch (err) {
        if (err.response && err.response.status === 400) {
          this.error = err.response.data; // Отображаем сообщение с бэкенда
        } else {
          console.error('Ошибка при регистрации:', err);
          this.error = 'Произошла ошибка при регистрации.';
        }
      }
    },
  },
};
</script>

<style>
.auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f9f9f9;
}

.auth-box {
  background-color: white;
  padding: 30px; /* Увеличено для симметрии */
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  text-align: center;
  width: 360px; /* Немного шире из-за двух полей в строке */
}

.auth-box .name-surname {
  display: flex;
  justify-content: space-between;
  gap: 10px; /* Расстояние между полями */
  margin-bottom: 10px;
}

.auth-box .name-surname input {
  width: calc(50% - 5px); /* Два поля равной ширины */
}

.auth-box input {
  width: 90%;
  margin-bottom: 10px;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 5px;
}

.auth-box button {
  width: 90%;
  padding: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.auth-box button:hover {
  background-color: #0056b3;
}

.error {
  color: red;
  font-size: 14px;
  margin-top: 10px;
}

.success {
  color: green;
  font-size: 14px;
  margin-top: 10px;
}
</style>
