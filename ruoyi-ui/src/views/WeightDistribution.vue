<template>
  <div class="weight-distribution">
    <h1>Weight Distribution Chart</h1>
    <img :src="chartUrl" alt="Weight Distribution Chart" />
  </div>
</template>

<script>
export default {
  name: 'WeightDistribution',
  data() {
    return {
      chartUrl: '/chart/weightDistribution'
    };
  }
}
</script>

<style scoped>
.weight-distribution {
  text-align: center;
}
.weight-distribution img {
  width: 80%;
  height: auto;
}
</style>
