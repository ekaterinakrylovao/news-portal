<template>
  <div class="homepage">
    <header class="header">
      <div v-if="$store.getters.isAuthenticated">
        Добро пожаловать, {{ userFirstName }} {{ userLastName }}!
        <button @click="logout">Выйти</button>
      </div>
      <div v-else>
        <router-link to="/login">Войти</router-link>
      </div>
    </header>
    <h1 class="title">Новости</h1>
    <div class="articles">
      <ArticleCard
          v-for="article in sortedArticles"
          :key="article.id"
          :article="article"
          :comments="$store.getters.comments(article.id)"
          :isAuthorized="$store.getters.isAuthenticated"
          @like="toggleLike(article.id)"
          @loadComments="loadComments(article.id)"
          @addComment="addComment"
      />
    </div>
  </div>
</template>

<script>
import ArticleCard from '../components/ArticleCard.vue';

export default {
  components: { ArticleCard },
  computed: {
    sortedArticles() {
      return this.$store.getters.articles.sort((a, b) => new Date(b.date) - new Date(a.date));
    },
    userFirstName() {
      return this.$store.state.user?.firstName || '';
    },
    userLastName() {
      return this.$store.state.user?.lastName || '';
    }
  },
  created() {
    this.$store.dispatch('fetchArticles');
  },
  methods: {
    async toggleLike(articleId) {
      await this.$store.dispatch('toggleLike', articleId);
    },
    loadComments(articleId) {
      this.$store.dispatch('fetchComments', articleId);
    },
    async addComment({ content, articleId }) {
      await this.$store.dispatch('addComment', { content, articleId });
      // После добавления нового комментария подгружаем обновленный список комментариев
      this.$store.dispatch('fetchComments', articleId);
    },
    logout() {
      this.$store.dispatch('logout');
    },
  },
};
</script>

<style>
.homepage {
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.title {
  font-size: 2rem;
  font-weight: bold;
  text-align: center;
  margin-bottom: 20px;
}

.articles {
  width: 66%;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.header {
  width: 100%;
  display: flex;
  justify-content: flex-end;
  padding: 10px;
  margin-bottom: 20px;
}
</style>
