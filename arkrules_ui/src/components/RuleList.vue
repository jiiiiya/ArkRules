<script>
import axios from 'axios'

export default {
  data() {
    return {
      rules: []
    }
  },
  methods: {
    fetchRules() {
      axios.get('/api/rules').then(response => {
        this.rules = response.data
        console.log(response.data)
      })
    },
    editRule(id) {
      this.$router.push({
        path: `/rules/${id}/edit`,
        state: {
          id: id
        }
      })
    },
    deleteRule(id) {
      axios.delete(`/api/rules/${id}`).then(() => {
        this.fetchRules()
      })
    }
  },
  created() {
    this.fetchRules()
  }
}
</script>

<template>
  <div>
    <h1>Rule List</h1>
    <ul>
      <li v-for="rule in rules" :key="rule.id">
        <router-link :to="'/rules/' + rule.id" :state="{id: rule.id}">{{ rule.name }}</router-link>
        <button @click="editRule(rule.id)">Edit</button>
        <button @click="deleteRule(rule.id)">Delete</button>
      </li>
    </ul>
    <router-link to="/rules/new">Add New Rule</router-link>
  </div>
</template>