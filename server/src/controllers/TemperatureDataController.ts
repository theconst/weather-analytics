/*
 * TemperatureDataController.ts
 * Controller for temperature API operations
 */
import { Request, Response, NextFunction } from "express";

import { TemperatureDataService } from "../services/TemperatureDataService";
import { TemperatureDataPoint, TimeInterval } from "../models/TemperatureData";

export const getTemparatureData = async (req: Request, resp: Response, next: NextFunction): Promise<void> => {
    const from: string | undefined = req.query.from as string;
    const to: string | undefined = req.query.to as string;

    try {
        let temperatureData: TemperatureDataPoint[] = undefined;
        if (!to && !from) {
            temperatureData = await TemperatureDataService.getAllTemperatureData();
        } else {
            temperatureData = await TemperatureDataService.getTemperatureDataInTimeRange([new Date(from), new Date(to)]);
        }
        resp.json(temperatureData);
    } catch (e) {
        next(e);
    }

};