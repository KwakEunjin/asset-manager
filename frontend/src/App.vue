<template>
  <div id="app">
    <nav class="navbar">
      <h1>가계부 & 자산관리</h1>
      <div class="nav-links">
        <router-link to="/">대시보드</router-link>
        <router-link to="/income">수입관리</router-link>
        <router-link to="/fixed-expense">고정지출</router-link>
        <router-link to="/living-expense">생활비</router-link>
        <router-link to="/savings">저축관리</router-link>
        <router-link to="/short-savings">단기저축</router-link>
        <router-link to="/codes">코드관리</router-link>
      </div>
    </nav>
    <main>
      <router-view />
    </main>
  </div>
</template>

<script setup lang="ts">
</script>

<style lang="scss">
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 2rem;
  background: #2c3e50;
  color: white;

  h1 {
    margin: 0;
    font-size: 1.5rem;
  }

  .nav-links {
    display: flex;
    gap: 1rem;

    a {
      color: white;
      text-decoration: none;
      padding: 0.5rem 1rem;
      border-radius: 4px;
      transition: background 0.3s;

      &:hover, &.router-link-active {
        background: #34495e;
      }
    }
  }
}

main {
  padding: 2rem;
}
</style>