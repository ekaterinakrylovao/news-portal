<template>
  <div class="comments">
    <h3>Комментарии</h3>
    <div v-if="comments.length > 0">
      <ul>
        <li v-for="comment in comments" :key="comment.id">
          <p>{{ comment.content }}</p>
          <small>Автор: {{ comment.username }} | {{ formattedDate(comment.timestamp) }}</small>
        </li>
      </ul>
    </div>
    <div v-else>Комментариев пока нет</div>
    <div v-if="isAuthorized" class="new-comment">
      <textarea v-model="newCommentText" placeholder="Напишите комментарий..."></textarea>
      <button @click="submitComment">Отправить</button>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    articleId: Number,
    comments: Array,
    isAuthorized: Boolean,
  },
  data() {
    return {
      newCommentText: '',
    };
  },
  methods: {
    submitComment() {
      if (this.newCommentText.trim()) {
        this.$emit('addComment', {
          content: this.newCommentText,
          articleId: this.articleId,
        });
        this.newCommentText = '';  // Очищаем текстовое поле
      }
    },
    formattedDate(date) {
      return new Date(date).toLocaleString();
    },
  },
};
</script>

<style>
.comments {
  border-top: 1px solid #ddd;
  margin-top: 20px;
  padding-top: 10px;
}

.new-comment {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-top: 10px;
  width: 100%;
}

.new-comment textarea {
  width: 100%;    /* Устанавливаем ширину на 100% от родительского контейнера */
  height: 50px;
  resize: none;
  padding: 5px;
  box-sizing: border-box;
}

.new-comment button {
  align-self: flex-end;
  padding: 5px 15px;
  background-color: #007bff;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.new-comment button:hover {
  background-color: #0056b3;
}
</style>
