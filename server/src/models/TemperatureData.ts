/**
 * TemperatureData.ts
 * Models of temperature data
 */

export type TimeInterval = [Date, Date];
export class TemperatureDataPoint {
    readonly time: Date;
    readonly temperature: number;

    constructor(thePoint: Date, theTemperature: number) {
        this.time = thePoint;
        this.temperature = theTemperature;
    }

    static fromObj(obj: any): TemperatureDataPoint {
        const rawTime = obj.time;
        const rawTemperature: number = obj.temperature;
        if (rawTime instanceof Date) {
            return new TemperatureDataPoint(rawTime, rawTemperature);
        } else if (typeof rawTemperature === "string") {
            return new TemperatureDataPoint(new Date(rawTime), rawTemperature);
        } else {
            return undefined;
        }
    }

}