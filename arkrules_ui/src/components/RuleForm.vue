<script>
import axios from 'axios'

export default {
  data() {
    return {
      rule: {
        ruleType: 'C',
        ruleCode: '',
        ruleConditions: ''
      },
      simulator: {
        input: '',
        result: ''
      },
      isEdit: false,
      errorMessage: '',
      id: ''
    }
  },
  methods: {
    fetchRule() {
      axios.get(`/api/rules/${this.id}`).then(response => {
        this.rule = response.data
      })
    },
    submitForm() {
      this.errorMessage = ''
      if (this.isEdit) {
        axios.put(`/api/rules/${this.id}`, this.rule).then(() => {
          this.fetchRule()
        }).catch(error => {
          this.errorMessage = error.response.data
        })
      } else {
        axios.post('/api/rules', this.rule).then(() => {
          this.$router.push('/rules')
        }).catch(error => {
          this.errorMessage = error.response.data
        })
      }
    },
    testRule() {
      axios.post(`/api/rules/test/${this.id}`, this.simulator).then(response => {
        this.simulator.result = JSON.stringify(response.data, null, 2)
      }).catch(error => {
        this.simulator.result = error.response.data
      })
    },
    scrollToBottom() {
      window.scrollTo(0, document.body.scrollHeight)
    }
  },
  created() {
    this.id = history.state.id
    if (this.id) {
      this.isEdit = true
      this.fetchRule()
    }
  },
  mounted() {
    if(history.state.isTest) {
      this.scrollToBottom()
    }
  }
}
</script>

<template>
  <div>
    <h1>{{ isEdit ? 'Edit Rule' : 'Add Rule' }}</h1>
    <form @submit.prevent="submitForm">
      <div>
        <label>Type</label>
        <select v-model="rule.ruleType">
          <option value="C">Classify</option>
          <option value="V">Validate</option>
          <option value="T">Convert</option>
        </select>
      </div>
      <div>
        <label>Code (find target)</label>
        <input v-model="rule.ruleCode" />
      </div>
      <div>
        <label>Conditions {{ !rule.ruleType || rule.ruleType === 'C' ? '' : ' (JSON)' }}</label>
        <textarea class="classify" v-model="rule.ruleConditions" required></textarea>
      </div>
      <button type="submit">Save</button>
    </form>
    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
  </div>
  <div v-if="isEdit">
    <h1>Rule Test...</h1>
    <form @submit.prevent="testRule">
      <button class="simulator_button">Run</button>
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

select {
  width: 200px;
}

div.simulator {
  width: 50%;
  float: left;
}

button.simulator_button {
  align-self: flex-start;
  margin-left: 10px;
}
</style>