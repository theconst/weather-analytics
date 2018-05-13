/**
 * TemperatureDataService.ts
 * Service for obtaining temperature data
 */
import { TemperatureDataPoint, TimeInterval } from "../models/TemperatureData";
import { QueryTemplate } from "./QueryTemplate";
import logger from "../util/logger";
import { RowDataPacket } from "mysql2/promise";

export namespace TemperatureDataService {

    // TODO: modify data to contain year
    const temperatureSelect = "SELECT `day` AS day, `month` as month," +
        "2012 AS year, UTC AS time, T AS temperature FROM `weather`";

    const mapper = (rows: RowDataPacket[]): TemperatureDataPoint[] => {
        return rows.map(row => {
            const time: Date = new Date(row.time);
            time.setFullYear(row.year, row.month, row.day);
            return new TemperatureDataPoint(time, row.temperature);
        });
    };

    const temperatureQueryTemplate = new QueryTemplate(
        temperatureSelect + // TODO: change this bullshit to one date during import
        "WHERE (`day` BETWEEN DAY(?) AND DAY(?))" +
        "AND (`month` BETWEEN MONTH(?) AND MONTH(?))" +
        "AND (`UTC` BETWEEN TIME(?) AND TIME(?))" +
        "ORDER BY `day`, `UTC`", mapper);

    const allTemperatureQueryTemplate = new QueryTemplate(temperatureSelect, mapper);

    /**
     * @param timeRange range to obtain data points
     * @returns points in time range from database
     */
    export const getTemperatureDataInTimeRange = async (timeRange: TimeInterval): Promise<TemperatureDataPoint[]> => {
        return await temperatureQueryTemplate.execute(
            timeRange[0], timeRange[1],
            timeRange[0], timeRange[1],
            timeRange[0], timeRange[1]);
    };

    export const getAllTemperatureData = async (): Promise<TemperatureDataPoint[]> => {
        return await allTemperatureQueryTemplate.execute();
    };

}