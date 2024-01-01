import axios from "axios";

export const GET_VIDEO = "/getVideo";

// TODO - auto replace this on build
export default axios.create({baseURL: 'http://localhost:8081'});
//export default axios.create({baseURL: 'https://converter-api.domza.xyz'});