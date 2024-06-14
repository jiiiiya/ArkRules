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
    },
    testRule(id) {
      this.$router.push({
        path: `/rules/${id}/edit`,
        state: {
          id: id,
          isTest: true
        }
      })
    },
  },
  created() {
    this.fetchRules()
  }
}
</script>

<template>
  <div>
    <h1>Rule List</h1>
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
    <table>
      <thead>
        <tr>
          <th>Id</th>
          <th>Type</th>
          <th>Code</th>
          <th>Rules</th>
          <th>Edit</th>
          <th>Delete</th>
          <th>History</th>
          <th>Test</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="rule in rules" :key="rule.id">
          <td>{{ rule.id }}</td>
          <td>{{ rule.ruleType === "C" ? "Classify" : rule.ruleType === "V" ? "Validate" : "Convert" }}</td>
          <td>{{ rule.ruleCode }}</td>
          <td style="word-break:break-all">
            {{ rule.ruleConditions }}
          </td>
          <td><button @click="editRule(rule.id)">Edit</button></td>
          <td><button @click="deleteRule(rule.id)">Delete</button></td>
          <td><button @click="historyRule(rule.id)">History</button></td>
          <td><button @click="testRule(rule.id)">Test</button></td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<style scoped>

table {
  text-align: center;
}
table th {
  font-weight: bold;
  color: #0f1f32;
  padding: 8px;
  border-bottom: 1px solid #C8CBCF;
}

table td {
  padding: 5px;
  text-align: left;
  border-bottom: 1px solid #DFE2E7;
}

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