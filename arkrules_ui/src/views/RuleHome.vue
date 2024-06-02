<script>
export default {
  name: 'RuleHome'
}
</script>

<template>
  <div class="home">
    <h1>Welcome to Rule Management System</h1>
    <p>
      This application allows you to manage your rules, test them with sample data, and track changes made to the rules.
    </p>
    <router-link to="/rules">Go to Rule List</router-link>
  </div>
</template>

<style scoped>
.home {
  text-align: center;
  margin-top: 50px;
}
</style>