import { createStore } from 'vuex';
import axios from 'axios';

axios.defaults.baseURL = 'http://localhost:8081'; // Установка базового URL

const store = createStore({
    state: {
        articles: [],
        comments: {}, // Комментарии по id статьи
        user: JSON.parse(localStorage.getItem('user')) || null,  // Загружаем данные пользователя из localStorage
        token: localStorage.getItem('token') || '',
    },
    mutations: {
        SET_ARTICLES(state, articles) {
            state.articles = articles;
        },
        SET_LIKES(state, { articleId, likes }) {
            const article = state.articles.find(a => a.id === articleId);
            if (article) {
                article.likes = likes;
            }
        },
        TOGGLE_LIKE(state, articleId) {
            const article = state.articles.find(a => a.id === articleId);
            if (article) {
                article.liked = !article.liked;
            }
        },
        SET_COMMENTS(state, { articleId, comments }) {
            state.comments = {
                ...state.comments,
                [articleId]: comments,
            };
        },
        SET_USER(state, user) {
            state.user = user;
            localStorage.setItem('user', JSON.stringify(user)); // Сохраняем данные пользователя в localStorage
        },
        SET_TOKEN(state, token) {
            state.token = token;
            localStorage.setItem('token', token);
            axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        },
        CLEAR_TOKEN(state) {
            state.token = '';
            localStorage.removeItem('token');
            delete axios.defaults.headers.common['Authorization'];
            state.user = null;  // Очистить данные пользователя при выходе
            localStorage.removeItem('user'); // Удаляем данные пользователя из localStorage

            // Сбрасываем лайки для всех статей
            state.articles.forEach(article => {
                article.liked = false; // Убираем лайки
            });
        },
    },
    actions: {
        async fetchArticles({ commit, state }) {
            try {
                const response = await axios.get('/articles');
                const articles = await Promise.all(response.data.map(async (article) => {
                    // Получаем количество лайков для каждой статьи
                    const likesResponse = await axios.get(`/likes/article/${article.id}/count`);
                    const likes = likesResponse.data;

                    // Проверяем, поставил ли пользователь лайк
                    const likedResponse = state.token
                        ? await axios.get(`/likes/article/${article.id}/user`, {
                            headers: { Authorization: `${state.token}` } // Передаем токен в заголовке
                        })
                        : { data: false }; // Если токена нет, предполагаем, что лайк не поставлен

                    return {
                        id: article.id,
                        title: article.title,
                        text: article.content,
                        image: article.imageUrl,
                        date: article.publishedDate,
                        liked: likedResponse.data, // Состояние лайка (true/false)
                        likes: likes, // Количество лайков
                    };
                }));

                commit('SET_ARTICLES', articles);  // Сохраняем статьи в Vuex
            } catch (error) {
                console.error('Ошибка при загрузке статей:', error);
            }
        },
        async toggleLike({ commit, state }, articleId) {
            if (!state.token) {
                throw new Error('Необходима авторизация');
            }

            try {
                const isLiked = state.articles.find(article => article.id === articleId).liked;
                const url = `/likes/article/${articleId}`;

                if (isLiked) {
                    // Убираем лайк
                    await axios.delete(url, {
                        headers: {
                            Authorization: `${state.token}`,
                        },
                    });
                } else {
                    // Ставим лайк
                    await axios.post(url, {}, {
                        headers: {
                            Authorization: `${state.token}`,
                        },
                    });
                }

                // После изменения лайка обновляем состояние статьи
                commit('TOGGLE_LIKE', articleId);

                // Получаем обновленное количество лайков
                const response = await axios.get(`/likes/article/${articleId}/count`); // используем правильный API для получения лайков
                const updatedLikes = response.data; // предполагается, что API возвращает { number }
                commit('SET_LIKES', { articleId, likes: updatedLikes });
            } catch (error) {
                console.error('Ошибка при изменении лайка:', error);
            }
        },
        async fetchComments({ commit }, articleId) {
            try {
                const response = await axios.get(`/comments/article/${articleId}`);
                commit('SET_COMMENTS', { articleId, comments: response.data });
            } catch (error) {
                console.error('Ошибка при загрузке комментариев:', error);
            }
        },
        async addComment({ commit, state }, { content, articleId }) {
            if (!state.token) {
                throw new Error('Необходима авторизация');
            }

            try {
                // Отправка комментария
                await axios.post('/comments', { content }, {
                    headers: {
                        Authorization: `${state.token}`,
                    },
                    params: { articleId },
                });

                // Добавляем новый комментарий в состояние сразу после отправки
                const newComment = { content, username: state.user.firstName, timestamp: new Date().toISOString() };
                commit('SET_COMMENTS', { articleId, comments: [...state.comments[articleId] || [], newComment] });
            } catch (error) {
                console.error('Ошибка при отправке комментария:', error);
            }
        },
        async register({ commit }, userData) {
            try {
                const response = await axios.post('/auth/register', {
                    firstName: userData.name,
                    lastName: userData.surname,
                    email: userData.email,
                    password: userData.password
                });

                const user = response.data.user; // Данные пользователя
                const token = response.data.token; // Токен

                commit('SET_USER', user); // Сохраняем данные пользователя
                commit('SET_TOKEN', token); // Сохраняем токен

                return { success: true }; // Возвращаем успех
            } catch (error) {
                return { success: false, message: error.response.data };
            }
        },
        async login({ commit }, credentials) {
            try {
                const response = await axios.post('/auth/login', credentials);
                const token = response.data.token;
                const user = response.data.user; // Данные пользователя

                // Сохраняем токен в localStorage
                localStorage.setItem('token', token);

                commit('SET_TOKEN', token);
                commit('SET_USER', user); // Сохраняем данные пользователя

                // костыль, потому что мне надо было, чтобы состояние лайка возвращалось тот час же
                window.location.href = '/';

                return { success: true };
            } catch (error) {
                return { success: false, message: error.response?.data?.message || 'Ошибка авторизации' };
            }
        },
        logout({ commit }) {
            // Очищаем localStorage
            localStorage.clear();

            commit('CLEAR_TOKEN');
        },
    },
    getters: {
        isAuthenticated: state => !!state.token,
        user: state => state.user,
        articles: state => state.articles,
        comments: state => articleId => state.comments[articleId] || [],
    },
});

export default store;
