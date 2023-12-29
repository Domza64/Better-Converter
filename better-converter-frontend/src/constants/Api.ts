import axios from "axios";

export const GET_VIDEO = "/getVideo";

export default axios.create({baseURL: 'http://localhost:8081'});