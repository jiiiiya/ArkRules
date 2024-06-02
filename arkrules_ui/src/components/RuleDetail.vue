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
    console.log(history.state)
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
    <div>
      <strong>Action:</strong> {{ rule.action }}
    </div>
  </div>
</template>