import { createRouter, createWebHistory } from 'vue-router'
const Home = () => import('../views/RuleHome.vue')
const RuleView = () => import('../views/RuleView.vue')

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes: [
        {
            path: '/',
            component: Home
        },
        {
            path: '/rules',
            component: RuleView
        },
        {
            path: '/rules/new',
            component: () => import('../components/RuleForm.vue')
        },
        {
            path: '/rules/:id/edit',
            component: () => import('../components/RuleForm.vue')
        },
        {
            path: '/rules/:id',
            component: () => import('../components/RuleDetail.vue')
        },
        {
            path: '/changes/:id',
            component: () => import('../components/RuleChangeList.vue')
        },
        {
            path: '/rules/test',
            component: () => import('../components/RuleTest.vue')
        },
    ]
})

export default router