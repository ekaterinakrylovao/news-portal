<template>
  <div class="auth-container">
    <div class="auth-box">
      <h2>Вход в личный аккаунт</h2>
      <input v-model="email" type="email" placeholder="Email" />
      <input v-model="password" type="password" placeholder="Пароль" />
      <button @click="login">Войти</button>
      <p class="redirect">
        Нет аккаунта? <router-link to="/register">Создать</router-link>
      </p>
      <p v-if="error" class="error">{{ error }}</p>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      email: '',
      password: '',
      error: '',
    };
  },
  methods: {
    async login() {
      if (!this.email || !this.password) {
        this.error = 'Все поля должны быть заполнены.';
        return;
      }

      try {
        const result = await this.$store.dispatch('login', {
          email: this.email,
          password: this.password,
        });
        if (result.success) {
          console.log('Авторизация успешна, редирект на главную');
          this.$router.push('/'); // Переход на главную страницу после логина
        } else {
          this.error = result.message || 'Ошибка входа. Попробуйте снова.';
        }
      } catch (err) {
        console.error('Ошибка при авторизации:', err);
        this.error = 'Произошла ошибка при авторизации.';
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
  padding: 30px; /* Увеличено для визуального баланса */
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  text-align: center;
  width: 320px; /* Увеличено для большей пропорции */
}
.auth-box input {
  width: 90%; /* Уменьшено для симметрии */
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
.redirect {
  margin-top: 10px;
  font-size: 14px;
}
.error {
  color: red;
  font-size: 14px;
  margin-top: 10px;
}
</style>
