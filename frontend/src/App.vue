<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
import { mapState } from 'vuex';

export default {
  computed: {
    ...mapState(['user']),
  },
};
</script>
