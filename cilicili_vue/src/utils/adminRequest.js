import axios from 'axios'
import global from '@/assets/global';
import Cookies from 'js-cookie';
import router from '@/router';


const request = axios.create({
    baseURL: global.apihost,
    timeout: 5000
})

request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    const adminJSON=Cookies.get('admin');
    if(adminJSON){
        config.headers['token'] = JSON.parse(adminJSON).token;
    }
    return config;
}, error => {
    return Promise.reject(error);
});


request.interceptors.response.use(
    response => {
        let res = response.data;
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        if(res.code==='401'){
            Cookies.remove('admin');
            router.push('/admin/login').catch();
        }
        return res;
    }, error => {
        console.log('err' + error)
        return Promise.reject(error)
    }
)

export default request