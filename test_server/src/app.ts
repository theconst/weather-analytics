import express from "express";
import * as TemperatureDataController from "./controllers/TemperatureDataController";

const app = express();

app.get("/temperatureData", TemperatureDataController.getTemparatureData);

export default app;