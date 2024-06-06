<script>
import axios from 'axios'

export default {
  data() {
    return {
      rule: {},
      id: ''
    }
  },
  methods: {
    fetchRule() {
      axios.get(`/api/rules/${this.id}`).then(response => {
        this.rule = response.data
      })
    }
  },
  created() {
    this.id = history.state.id
    this.fetchRule()
  }
}
</script>

<template>
  <div>
    <h1>Rule Detail</h1>
    <div>
      <strong>Name:</strong> {{ rule.name }}
    </div>
    <div>
      <strong>Description:</strong> {{ rule.description }}
    </div>
    <div>
      <strong>Condition:</strong> {{ rule.conditions }}
    </div>
  </div>
</template>

<style scoped>
div {
  margin-bottom: 15px;
}

textarea {
  margin-bottom: 10px;
}

button {
  margin-top: 10px;
}
</style>