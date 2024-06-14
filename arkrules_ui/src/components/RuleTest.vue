<script>
import axios from 'axios'

export default {
  data() {
    return {
      simulator: {
        code: '',
        input: '',
        result: ''
      },
      rules: []
    }
  },
  methods: {
    testRule() {
      axios.post(`/api/rules/test`, this.simulator).then(response => {
        this.simulator.result = JSON.stringify(response.data, null, 2)
      }).catch(error => {
        this.simulator.result = error.response.data
      })
    },
    fetchRules() {
      axios.get('/api/rules').then(response => {
        this.rules = new Set(response.data.filter(rule => rule.ruleType === 'C'))
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
    <h1>Rule Test</h1>
    <form @submit.prevent="testRule">
      <button class="simulator_button">Run</button>
      <select v-model="simulator.code">
        <option value="">--- Code (find target) ---</option>
        <option v-for="rule in rules" :key="rule.id" :value="rule.ruleCode">
          {{ rule.ruleCode }}
        </option>
      </select>
      <div>
        <div class="simulator">
          <textarea v-model="simulator.input"></textarea>
        </div>
        <div class="simulator">
          <textarea v-model="simulator.result"></textarea>
        </div>
      </div>
    </form>
  </div>
</template>

<style scoped>

button {
  align-self: flex-start;
}

div.simulator {
  width: 50%;
  float: left;
}

input, select {
  width: 30%;
  float: left;
}

button.simulator_button {
  align-self: flex-start;
  margin-left: 10px;
}


</style>