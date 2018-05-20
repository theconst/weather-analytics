/**
 * Gets total hours by time data and funtion name, argument is assumed to be named time
 */
export function  getTotalHoursBy(timeData, fName) {
    const totalHours = timeData
                .reduce((map, point, i) => {
                    const fValue = point[fName];

                    // time frame of the first item is always zero
                    const prevDate = new Date(timeData[Math.max(0, i - 1)].time);
                    const nextDate = new Date(timeData[i].time);
                    const samplingHours = (nextDate - prevDate) / 1000 / 3600;
                    return map.set(fValue, (map.get(fValue) || 0.0) + samplingHours);
                }, new Map());

    const labels = Array.from(totalHours.keys()).sort((a, b) => a - b);
    return {
        labels : labels,
        data : labels.map(l => totalHours.get(l))
    };
}

export function getTimeDataBy(timeData, fName) {
    return timeData.map(point => {
            return { x: point.time, y: point[fName] };
    });
}