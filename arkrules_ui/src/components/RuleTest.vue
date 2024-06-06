<script>
import axios from 'axios'

export default {
  data() {
    return {
      simulator: {
        input: '',
        result: ''
      },
    }
  },
  methods: {
    testRule() {
      axios.post(`/api/rules/test`, this.simulator).then(response => {
        this.simulator.result = JSON.stringify(response.data, null, 2)
      }).catch(error => {
        this.simulator.result = error.response.data
      })
    }
  },
  created() {
  }
}
</script>

<template>
  <div>
    <h1>Rule Test...</h1>
    <form @submit.prevent="testRule">
      <button class="simulator_button">Run</button>
      <div>
        <div class="simulator">
          <textarea v-model="simulator.input" required></textarea>
        </div>
        <div class="simulator">
          <textarea v-model="simulator.result"></textarea>
        </div>
      </div>
    </form>
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

div.simulator {
  width: 50%;
  float: left;
}

button.simulator_button {
  align-self: flex-start;
  margin-left: 10px;
}
</style>