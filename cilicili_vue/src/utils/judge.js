import Cookies from "js-cookie";

const judgeNull = function (tmp) {
    return !tmp && typeof (tmp) != "undefined" && tmp != 0;
}

const judgeUndefined = function (tmp) {
    return typeof (tmp) == "undefined";
}

const judgeEmptyCollection = function (tmp) {
    return JSON.stringify(tmp) == '{}';
}

const judgeLogin = function () {
    const userJSON = Cookies.get('user');
    return (!judgeNull(userJSON) && !judgeUndefined(userJSON))
}

export default {
    judgeNull,
    judgeUndefined,
    judgeEmptyCollection,
    judgeLogin
}