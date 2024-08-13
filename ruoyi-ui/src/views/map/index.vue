
<template>
  <div class="container" style="width: 800px; height: 800px"></div>
</template>

<script>
// 这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
// 例如：import 《组件名称》 from '《组件路径》';

import AMapLoader from "@amap/amap-jsapi-loader";

export default {
  // import引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    // 这里存放数据
    return {
      map: "",
    };
  },
  // 监听属性 类似于data概念
  computed: {},
  // 监控data中的数据变化
  watch: {},
  // 方法集合
  methods: {
    initMap() {
      AMapLoader.load({
        key: "f17a184e00fb274c1da2ded76880be23", // 申请好的Web端开发者Key，首次调用 load 时必填
        //2.0版本太卡了 ，所以使用的1.4.0版本  其插件也有不同  如：ToolBar
        version: "1.4.0", // 指定要加载的 JSAPI 的版本，缺省时默认为 1.4.15
        resizeEnable: true,

        plugins: [
          "AMap.ToolBar", //工具条
          "AMap.Scale", // 比例尺
          "AMap.Geolocation", //定位
        ], // 需要使用的的插件列表，如比例尺'AMap.Scale'等
      })
        .then((AMap) => {
          console.log(AMap);
          this.map = new AMap.Map("Map", {
            //设置地图容器id
            resizeEnable: true,
            rotateEnable: true,
            pitchEnable: true,
            zoom: 15,
            pitch: 80,
            rotation: -15,
            viewMode: "3D", //开启3D视图,默认为关闭
            buildingAnimation: true, //楼块出现是否带动画

            expandZoomRange: true,
            zooms: [3, 20],
            center: [108.946651, 34.222718], //初始化地图中心点位置
          });
          this.markerdom =
            "" +
            '<div class="custom-content-marker">' +
            '   <img src="/icon_bike.png">' +
            "</div>";
          for (let i = 0; i < this.markernum.length; i++) {
            this.marker.push(
              new AMap.Marker({
                position: new AMap.LngLat(
                  this.markernum[i][0],
                  this.markernum[i][1]
                ), // Marker经纬度
                content: this.markerdom, // 将 html 传给 content
                offset: new AMap.Pixel(-13, -30), // 以 icon 的 [center bottom] 为原点
              })
            );
          }

          this.map.add(this.marker);
          console.log(this.marker);
        })
        .catch((e) => {
          console.log(e);
        });
    },
  },
  // 生命周期 - 创建完成（可以访问当前this实例）
  created() {},
  // 生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {
    this.initMap();
  },
  beforeCreate() {}, // 生命周期 - 创建之前
  beforeMount() {}, // 生命周期 - 挂载之前
  beforeUpdate() {}, // 生命周期 - 更新之前
  updated() {}, // 生命周期 - 更新之后
  beforeDestroy() {}, // 生命周期 - 销毁之前
  destroyed() {}, // 生命周期 - 销毁完成
  activated() {}, // 如果页面有keep-alive缓存功能，这个函数会触发
};
</script>
<style scoped>
</style>