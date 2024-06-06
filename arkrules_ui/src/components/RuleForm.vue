<script>
import axios from 'axios'

export default {
  data() {
    return {
      rule: {
        ruleType: '',
        ruleCode: '',
        ruleConditions: ''
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
      if (this.isEdit) {
        axios.put(`/api/rules/${this.id}`, this.rule).then(() => {
          this.$router.push('/rules')
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
  },
  created() {
    this.id = history.state.id
    if (this.id) {
      this.isEdit = true
      this.fetchRule()
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
        <label>Conditions {{ rule.ruleType === 'C' ? '' : ' (JSON)' }}</label>
        <textarea class="classify" v-model="rule.ruleConditions" required></textarea>
      </div>
      <button type="submit">Save</button>
    </form>
    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
  </div>
</template>

<style scoped>
form {
  display: flex;
  flex-direction: column;
}

form div {
  margin-bottom: 15px;
}

label {
  margin-bottom: 5px;
  font-weight: bold;
}

button {
  align-self: flex-start;
}

select {
  width: 200px;
}

</style>