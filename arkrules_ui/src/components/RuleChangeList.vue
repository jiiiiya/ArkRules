<script>
import axios from 'axios'

export default {
  data() {
    return {
      changes: [],
      ruleId: '',
      errorMessage: ''
    }
  },
  methods: {
    fetchChanges() {
      axios.get(`/api/changes/${this.ruleId}`).then(response => {
        this.changes = response.data
        if(this.changes.length === 0) {
          this.errorMessage = 'No changes.'
        }
      }).catch(error => {
        this.errorMessage = error.response.data
      })
    }
  },
  created() {
    this.ruleId = history.state.id
    this.fetchChanges()
  }
}
</script>

<template>
  <div>
    <h2>Rule Change History</h2>
    <ul>
      <li v-for="change in changes" :key="change.id">
        <p>Changed: {{ change.registerDate }}</p>
        <p>Type: {{ change.ruleType === "C" ? "Classify" : change.ruleType === "V" ? "Validate" : "Convert" }}</p>
        <p>code: {{ change.ruleCode }}</p>
        <p>conditions: </p>
        <textarea readonly v-model="change.ruleConditions"></textarea>
        <p></p>
      </li>
    </ul>
    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
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
</style>