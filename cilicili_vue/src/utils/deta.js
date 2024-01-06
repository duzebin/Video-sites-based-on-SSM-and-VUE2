const parseTime = function (time, cFormat) {
    if (arguments.length === 0) {
        return null
    }
    const format = cFormat || '{y}-{m}-{d} {h}:{i}:{s}'
    let date
    if (typeof time === 'object') {
        date = time
    } else {
        if ((typeof time === 'string') && (/^[0-9]+$/.test(time))) {
            time = parseInt(time)
        }
        if ((typeof time === 'number') && (time.toString().length === 10)) {
            time = time * 1000
        }
        date = new Date(time)
    }
    const formatObj = {
        y: date.getFullYear(),
        m: date.getMonth() + 1,
        d: date.getDate(),
        h: date.getHours(),
        i: date.getMinutes(),
        s: date.getSeconds(),
        a: date.getDay()
    }
    const time_str = format.replace(/{([ymdhisa])+}/g, (result, key) => {
        const value = formatObj[key]
        if (key === 'a') { return ['日', '一', '二', '三', '四', '五', '六'][value] }
        return value.toString().padStart(2, '0')
    })
    return time_str
}


const getTimes = function () {
    var getTime = new Date().getTime(); 
    var time = new Date(getTime); 
    function nowDate(time) {
        var year = time.getFullYear(); 
        var month = (time.getMonth() + 1).toString().padStart(2, '0');
        var date = time.getDate().toString().padStart(2, '0'); 
        var hour = time.getHours().toString().padStart(2, '0'); 
        var minute = time.getMinutes().toString().padStart(2, '0'); 
        var second = time.getSeconds().toString().padStart(2, '0'); 
        return (
            year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second
        )
    }
    return nowDate(time);
}

export default {
    parseTime,
    getTimes
}