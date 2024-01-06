<template>
    <div id="Admin">
        <keep-alive>
            <router-view v-if="$route.meta.keepAlive"></router-view>
        </keep-alive>
        <router-view v-if="!$route.meta.keepAlive"></router-view>
    </div>
</template>
<script>

export default {
    data() {
        return {

        };
    }

}
</script>
<style></style>