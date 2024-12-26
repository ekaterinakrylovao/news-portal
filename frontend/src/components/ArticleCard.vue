<template>
  <div class="article-card">
    <!-- Добавляем картинку статьи -->
    <img :src="imageUrl" alt="Article Image" class="article-image" />
    <h2>{{ article.title }}</h2>
    <p v-if="!expanded">{{ shortText }}</p>
    <p v-else v-html="formattedText"></p>
    <button class="expand-button" @click="toggleExpand">
      {{ expanded ? 'Скрыть' : 'Показать больше' }}
    </button>
    <div class="actions">
      <button @click="toggleLike" :class="{ liked: article.liked }">
        <span>{{ article.liked ? '❤️' : '🤍' }}</span> {{ article.likes }}
      </button>
      <button @click="toggleComments">Комментарии</button>
      <span class="publication-date">{{ formattedDate }}</span>
    </div>
    <Comments
        v-if="showComments"
        :articleId="article.id"
        :comments="comments"
        :isAuthorized="isAuthorized"
        @addComment="addComment"
    />
  </div>
</template>

<script>
import Comments from './Comments.vue';

export default {
  props: ['article', 'comments', 'isAuthorized'],
  data() {
    return {
      expanded: false,
      showComments: false,
    };
  },
  computed: {
    // Получаем сокращенный текст
    shortText() {
      return this.article.text.slice(0, 300) + '...';
    },
    // Форматируем текст
    formattedText() {
      return this.article.text.replace(/\n/g, '<br>');
    },
    // Форматируем дату публикации
    formattedDate() {
      return new Date(this.article.date).toLocaleString();
    },
    // Формируем правильный URL для изображения
    imageUrl() {
      // Путь к картинке будет зависеть от того, как вы храните изображения
      return `http://localhost:8081${this.article.image}`;
    },
  },
  methods: {
    toggleExpand() {
      this.expanded = !this.expanded;
    },
    toggleComments() {
      this.showComments = !this.showComments;
      if (this.showComments) {
        this.$emit('loadComments');
      }
    },
    addComment(newComment) {
      this.$emit('addComment', newComment);
    },
    toggleLike() {
      this.$emit('like', this.article.id);
    },
  },
  components: { Comments },
};
</script>

<style>
.article-card {
  border: 1px solid #ccc;
  border-radius: 10px;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 15px;
  background-color: #f9f9f9;
}

.article-card img {
  width: 100%;
  border-radius: 5px;
}

.actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
}

.like-button span {
  font-size: 20px;
}

.publication-date {
  font-size: 14px;
  color: gray;
}

.liked {
  color: red;
}
</style>
