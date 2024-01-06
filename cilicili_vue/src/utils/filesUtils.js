import SparkMD5 from "spark-md5";
import judge from "./judge";
import Cookies from "js-cookie";
import global from "@/assets/global";
import axios from "axios";
import request from "./request";

export function filePare(chunks) {
    return new Promise(resolve => {
        const spark = new SparkMD5();
        function _read(i) {
            if (i >= chunks.length) {
                resolve(spark.end())
                return;
            }
            const blob = chunks[i];
            const reader = new FileReader();
            reader.onload = (e) => {
                const bytes = e.target.result;
                spark.append(bytes);
                _read(i + 1);
            }
            reader.readAsDataURL(blob);
        }
        _read(0)
    });
}

