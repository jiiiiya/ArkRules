<script>
import axios from 'axios'

export default {
  data() {
    return {
      rule: {
        name: '',
        description: '',
        conditions: ''
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
        })
      } else {
        axios.post('/api/rules', this.rule).then(() => {
          this.$router.push('/rules')
        }).catch(error => {
          this.errorMessage = error.response.data
        })
      }
    }
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
    <h1>{{ isEdit ? 'Edit Rule' : 'Create Rule' }}</h1>
    <form @submit.prevent="submitForm">
      <div>
        <label>Name</label>
        <input v-model="rule.name" />
      </div>
      <div>
        <label>Description</label>
        <input v-model="rule.description" />
      </div>
      <div>
        <label>Condition (JSON)</label>
        <textarea v-model="rule.conditions"></textarea>
      </div>
      <button type="submit">Submit</button>
    </form>
    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
  </div>
</template>