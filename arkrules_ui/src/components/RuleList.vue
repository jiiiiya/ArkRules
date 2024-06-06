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
    },
    historyRule(id) {
      this.$router.push({
        path: `/changes/${id}`,
        state: {
          id: id
        }
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
        <p>Type: {{ rule.ruleType === "C" ? "Classify" : rule.ruleType === "V" ? "Validate" : "Convert" }}</p>
        <p>Code: {{ rule.ruleCode }}</p>
        <button @click="editRule(rule.id)">Edit</button>
        <button @click="deleteRule(rule.id)">Delete</button>
        <button @click="historyRule(rule.id)">History</button>
      </li>
    </ul>
    <div>
      <ul>
        <li>
          <router-link to="/rules/new">Add Rule</router-link>
        </li>
        <li>
          <router-link to="/rules/test">Test Rule</router-link>
        </li>
      </ul>
    </div>
  </div>
</template>

<style scoped>
ul {
  padding: 0;
}

li {
  margin-bottom: 20px;
  padding: 10px;
  background: #f9f9f9;
  border-radius: 4px;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
}

p {
  margin: 5px 0;
}

button {
  margin-right: 10px;
}

</style>